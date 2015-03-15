/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.db;

import com.aaron.desktop.model.log.LogManager;
import java.util.List;
import javax.swing.JOptionPane;
import com.aaron.desktop.view.MainFrameView;
import org.hibernate.Criteria;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

    /**
     * adds a new vocabulary in the database and checks if it is successful.
     * @param vocabObject object containing English and foreign word counterpart, and foreign language.
     * @return boolean
     */
    public boolean addToDatabase(final Vocabulary vocabObject)
    {
        boolean success = true;

        Session session = HibernateUtil.getInstance().openSession();
        Transaction transaction = session.beginTransaction();
        try
        {      
            session.save(vocabObject);

            transaction.commit();
        }
        catch(final JDBCException e)
        {
            transaction.rollback();
            success = false;
            JOptionPane.showMessageDialog(null, vocabObject.getEnglishWord() + " is already in the database.", "Note", JOptionPane.INFORMATION_MESSAGE);
            this.logger.error(this.className, "addToDatabase(Vocabulary)", e.getSQLException().getMessage(), e);
        }
        finally
        {
            session.close();
        }

        return success;
    }
    

    /**
     * Deletes a vocabulary in the database. 
     * @param selectedWord vocabulary to delete
     */
    public void deleteVocabulary(final String selectedWord)
    {
        Session session = HibernateUtil.getInstance().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("delete Vocabulary where english_word = :word");

        try
        {  
            query.setString("word", selectedWord);
            query.executeUpdate();
            transaction.commit();
            this.logger.info(this.className, "deleteVocabulary(String)", selectedWord + " in " + MainFrameView.getforeignLanguage() + " has been deleted from the database.");
        }
        catch(final JDBCException e)
        {
            transaction.rollback();
            this.logger.error(this.className, "deleteVocabulary(String)", e.getSQLException().getMessage(), e);
        }
        finally
        {
            session.close();
        }
    }
    
    /**
     * Updates a vocabulary in the database.
     * @param vocabObject object containing English and foreign word counterpart, and foreign language.
     * @param column
     */
    public void updateVocabulary(final Vocabulary vocabObject, final int column)
    {
        Session session = HibernateUtil.getInstance().openSession();
        Transaction transaction = session.beginTransaction();

        Query query;
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
                return;
        }

        try
        {  
            query = session.createQuery(queryString);
            query.setString("eword", vocabObject.getEnglishWord());
            query.setString("fword", vocabObject.getForeignWord());
            query.setInteger("fid", vocabObject.getForeignId());
            query.executeUpdate();
            transaction.commit();
            this.logger.info(this.className, "updateVocabulary(Vocabulary, int)", vocabObject.toString() + " has been update.");
        }
        catch(final JDBCException e)
        {
            final String[] newWord = vocabObject.getEnglishWord().split("/");
            transaction.rollback();        
            JOptionPane.showMessageDialog(null, newWord[newWord.length - 1] + " is already in the database.", "Note", JOptionPane.INFORMATION_MESSAGE);
            this.logger.error(this.className, "updateVocabulary(Vocabulary, int)", e.getSQLException().getMessage(), e);
        }
        finally
        {
            session.close();
        }
    }
    
    /**
     * Retrieves all vocabulary in the database starting with the selected letter.
     * @param letter selected letter in the sort combobox
     * @return vocabularies stored in a list
    */
    public List<Vocabulary> getVocabularies(final String letter)
    {      
        Session session = HibernateUtil.getInstance().openSession();
        Criteria criteria = session.createCriteria(Vocabulary.class);
        criteria.add(Restrictions.eq("foreignId", MainFrameView.getforeignLanguage().getId()));
        try
        {      
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

        return null;       
    }
    
    /**
     * Closes the connection
     */
    public void closeDB()
    {
        HibernateUtil.close();
    }
}