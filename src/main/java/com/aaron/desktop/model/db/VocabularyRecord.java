/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.db;

import static java.util.Objects.*;

import com.aaron.desktop.model.log.LogManager;
import java.util.List;
import javax.swing.JOptionPane;
import com.aaron.desktop.view.MainFrameView;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.Criteria;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Aaron
 */
public class VocabularyRecord
{
    private final LogManager logger = LogManager.getInstance();
    private final String className = this.getClass().getSimpleName();
    private static final String ENGLISH_WORD_SEPARATOR = "/";
    
    /**
     * adds a new vocabulary in the database and checks if it is successful.
     * @param vocabObject object containing English and foreign word counterpart, and foreign language.
     * @return boolean
     */
    public boolean addToDatabase(final Vocabulary vocabObject)
    {
        Function<Session, Boolean> saveVocabulary = (session) ->
        {
            session.save(vocabObject);
            return true;
        };
        
        Consumer<JDBCException> jdbcExceptionHandler = (e) ->
        {
                JOptionPane.showMessageDialog(null, vocabObject.getEnglishWord() + " is already in the database.", "Note", JOptionPane.INFORMATION_MESSAGE);
                this.logger.error(this.className, "addToDatabase(Vocabulary)", e.getSQLException().getMessage(), e);
        };
        
        return HibernateUtil.wrapInTransaction(saveVocabulary, jdbcExceptionHandler);
    }

    /**
     * Deletes a vocabulary in the database. 
     * @param selectedWord vocabulary to delete
     */
    public void deleteVocabulary(final String selectedWord)
    {
        Function<Session, Boolean> deleteVocabulary = (session) ->
        {
            Query query = session.createQuery("delete Vocabulary where english_word = :word");

            query.setString("word", selectedWord);
            query.executeUpdate();
            return true;
        };
        
        Consumer<JDBCException> jdbcExceptionHandler = (e) ->
        {
            this.logger.error(this.className, "deleteVocabulary(String)", e.getSQLException().getMessage(), e);
        };

        HibernateUtil.wrapInTransaction(deleteVocabulary, jdbcExceptionHandler);
    }
    
    /**
     * Updates a vocabulary in the database.
     * @param vocabObject object containing English and foreign word counterpart, and foreign language.
     * @param column
     */
    public void updateVocabulary(final Vocabulary vocabObject, final int column)
    {
        Function<Session, Boolean> updateVocabulary = (session) ->
        {
            String queryString = buildUpdateQuery(column);
            if(isNull(queryString))
            {
                return false;
            }
            Query query = session.createQuery(queryString);
            query.setString("eword", vocabObject.getEnglishWord());
            query.setString("fword", vocabObject.getForeignWord());
            query.setInteger("fid", vocabObject.getForeignId());
            query.executeUpdate();
            return true;
        };

        Consumer<JDBCException> jdbcExceptionHandler = (e) ->
        {
            final String[] newWord = vocabObject.getEnglishWord().split(ENGLISH_WORD_SEPARATOR);    
            JOptionPane.showMessageDialog(null, newWord[newWord.length - 1] + " is already in the database.", "Note", JOptionPane.INFORMATION_MESSAGE);
            this.logger.error(this.className, "updateVocabulary(Vocabulary, int)", e.getSQLException().getMessage(), e);
        };

        HibernateUtil.wrapInTransaction(updateVocabulary, jdbcExceptionHandler);
    }
    
    private String buildUpdateQuery(int column)
    {
        String queryString = "update Vocabulary set ";
        switch(column)
        {
            case 0:
                queryString += "english_word = :eword, last_updated = NOW() where foreign_word = :fword and foreign_id = :fid";
                break;
            case 1:
                queryString += "foreign_word = :fword, last_updated = NOW() where english_word = :eword and foreign_id = :fid";
                break;
            default:
                this.logger.error(this.className, "updateVocabulary(Vocabulary, int)", "Invalid table column " + column);
                return null;
        }
        
        return queryString;
    }
    
    /**
     * Retrieves all vocabulary in the database starting with the selected letter.
     * @param letter selected letter in the sort combobox
     * @return vocabularies stored in a list
    */
    public List<Vocabulary> getVocabularies(final String letter)
    {      
        Session session = HibernateUtil.getInstance();
        if(isNull(session))
        {
            return Collections.emptyList();
        }

        try
        {      
            Criteria criteria = buildGetVocabularyListCriteria(session, letter);
            return HibernateUtil.listAndCast(criteria);
        }
        catch(final JDBCException e)
        {
            this.logger.error(this.className, "getVocabularies(String)", e.getSQLException().getMessage(), e);
        }
        finally
        {
            session.close();
        }

        return Collections.emptyList();
    }

    private Criteria buildGetVocabularyListCriteria(Session session, String letter)
    {
        Criteria criteria = session.createCriteria(Vocabulary.class);
        criteria.add(Restrictions.eq("foreignId", MainFrameView.getforeignLanguage().getId()));     
        switch(letter)
        {
            case "All":
                criteria.addOrder(Order.asc("englishWord"));
                break;
            case "Rec":
                criteria.addOrder(Order.desc("lastUpdated"));
                break;
            default: 
                criteria.add(Restrictions.ilike("englishWord", letter + "%"));
        }
        
        return criteria;
    }
    
    /**
     * Closes the connection
     */
    public void closeDB()
    {
        HibernateUtil.close();
    }
}