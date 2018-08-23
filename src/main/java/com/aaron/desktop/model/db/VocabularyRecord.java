/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.db;

import com.aaron.desktop.entity.ForeignLanguage;
import com.aaron.desktop.entity.Vocabulary;
import com.aaron.desktop.entity.Vocabulary_;

import com.aaron.desktop.model.log.LogManager;
import java.util.List;
import javax.swing.JOptionPane;
import com.aaron.desktop.view.MainFrameView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Aaron
 */
public class VocabularyRecord
{
    private final LogManager logger = LogManager.getInstance();
    private final String className = this.getClass().getSimpleName();
    private static final String ENGLISH_WORD_SEPARATOR = "/";
    private Map<ForeignLanguage, List<Vocabulary>> vocabulariesMap;

    public List<ForeignLanguage> getForeignLanguages()
    {
        long start = System.currentTimeMillis();
        EntityManager em = JPAUtil.getEntityManager();
        List<ForeignLanguage> list = em.createNamedQuery(ForeignLanguage.GET_ALL, ForeignLanguage.class).getResultList();

        logger.info(className, "getForeignLanguages", "Total exec time: " + (System.currentTimeMillis() - start) + "ms");
        //vocabulariesMap = list.stream().collect(toMap(identity(), ForeignLanguage::getVocabularies));

        return list;
    }

    /**
     * saves a new vocabulary in the database and checks if it is successful.
     * @param vocabObject object containing English and foreign word counterpart, and foreign language.
     * @return true if success
     */
    public boolean saveToDatabase(final Vocabulary vocabObject)
    {
        long start = System.currentTimeMillis();

        Optional<Boolean> result = JPAUtil.transactAndReturn(em -> 
        {
            em.persist(vocabObject);
            return Optional.of(true);
        }, e -> saveVocabularyExceptionHandler(e, vocabObject));
        
        logger.info(className, "saveToDatabase", "Total exec time: " + (System.currentTimeMillis() - start) + "ms");

        return result.orElse(false);
    }

    private void saveVocabularyExceptionHandler(Exception e, Vocabulary vocabObject)
    {
        JOptionPane.showMessageDialog(null, vocabObject.getEnglishWord() + " is already in the database.", "Note", JOptionPane.INFORMATION_MESSAGE);
        this.logger.error(this.className, "addToDatabase(Vocabulary)", e.getMessage(), e);
    }

    /**
     * Deletes a vocabulary in the database. 
     * @param selectedWord vocabulary to delete
     * @return true if success
     */
    public boolean deleteVocabulary(final String selectedWord)
    {
        long start = System.currentTimeMillis();

        Optional<Boolean> result = JPAUtil.transactAndReturn(em -> Optional.of(deleteVocabulary(em, selectedWord)),
                                        e -> logger.error(this.className, "deleteVocabulary(String)", e.getMessage(), e));

        logger.info(className, "deleteVocabulary", "Total exec time: " + (System.currentTimeMillis() - start) + "ms");

        return result.orElse(false);
    }
    
    private boolean deleteVocabulary(EntityManager em, String selectedWord)
    {
        Query query = em.createQuery("DELETE FROM Vocabulary WHERE englishWord = :word");
        query.setParameter("word", selectedWord);

        return query.executeUpdate() > 0;
    }

    /**
     * Updates a vocabulary in the database.
     * @param id the primary key of the vocabulary
     * @param englishWord the new english word
     * @param foreignWord the new foreign word
     * @return managed updated Vocabulary
     */
    public Vocabulary updateVocabulary(final int id, final String englishWord, final String foreignWord)
    {
        long start = System.currentTimeMillis();
        
        Optional<Vocabulary> result = JPAUtil.transactAndReturn(em ->
        {
            Vocabulary vocabObject = em.find(Vocabulary.class, id);
            vocabObject.setEnglishWord(englishWord);
            vocabObject.setForeignWord(foreignWord);
            return Optional.of(em.merge(vocabObject));
        }, e -> updateVocabularyExceptionHandler(e, englishWord));

        logger.info(className, "updateVocabulary", "Total exec time: " + (System.currentTimeMillis() - start) + "ms");

        return result.get();
    }

    private void updateVocabularyExceptionHandler(Exception e, String englishWord)
    { 
        JOptionPane.showMessageDialog(null, englishWord + " is already in the database.", "Note", JOptionPane.INFORMATION_MESSAGE);
        logger.error(this.className, "updateVocabulary", e.getMessage(), e);
    }
    
    /**
     * Retrieves all vocabulary in the database starting with the selected letter.
     * @param letter selected letter in the sort combobox
     * @return vocabularies stored in a list
    */
    public List<Vocabulary> getVocabularies(final String letter)
    {
        long start = System.currentTimeMillis();
        try
        {
            EntityManager em = JPAUtil.getEntityManager();

            TypedQuery<Vocabulary> query = buildGetVocabularyListCriteria(em, letter);
            return query.getResultList();
        }
        catch(final Exception e)
        {
            logger.error(this.className, "getVocabularies(String)", e.getMessage(), e);
            return null;
        }
        finally
        {
            logger.info(className, "getVocabularies", "Total exec time: " + (System.currentTimeMillis() - start) + "ms");
        }
    }

    private TypedQuery<Vocabulary> buildGetVocabularyListCriteria(EntityManager em, String letter)
    {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Vocabulary> query = criteriaBuilder.createQuery(Vocabulary.class);
        Root<Vocabulary> vocabularyTable = query.from(Vocabulary.class);
        vocabularyTable.fetch(Vocabulary_.foreignLanguage, JoinType.INNER);
        query.select(vocabularyTable);

        Predicate whereClause = criteriaBuilder.equal(vocabularyTable.get(Vocabulary_.foreignLanguage), MainFrameView.getforeignLanguage());
        switch(letter)
        {
            case "All":
                query.orderBy(criteriaBuilder.asc(vocabularyTable.get(Vocabulary_.englishWord)));
                break;
            case "Rec":
                query.orderBy(criteriaBuilder.desc(vocabularyTable.get(Vocabulary_.lastUpdated)));
                break;
            default:
                whereClause = criteriaBuilder.and(whereClause, criteriaBuilder.like(vocabularyTable.get(Vocabulary_.englishWord), letter + "%"));
                query.orderBy(criteriaBuilder.asc(vocabularyTable.get(Vocabulary_.englishWord)));
        }

        query.where(whereClause);

        return em.createQuery(query);
    }
    
    /**
     * Closes the connection
     */
    public void closeDB()
    {
        JPAUtil.closeEntityManager();
    }
}