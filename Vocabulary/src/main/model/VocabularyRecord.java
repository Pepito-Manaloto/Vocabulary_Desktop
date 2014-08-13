/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.model;

import java.sql.*;
import javax.swing.JOptionPane;
import main.view.MainFrame;

/**
 *
 * @author Aaron
 */
public class VocabularyRecord
{
    private Connection con = null;
    private CallableStatement callableProcedure;
    private ResultSet rs = null;
    private final LogManager logger = LogManager.getInstance();
    
    /**
     * Default constructor initializes database connection.
     * @throws SQLException
     */
    public VocabularyRecord() throws SQLException
    {
        this.connectToDb();
    }
    
    /**
     * adds a new vocabulary in the database and checks if it is successful.
     * @param vocabObject object containing English and foreign word counterpart, and foreign language.
     * @return boolean
     */
    public boolean addToDatabase(final Vocabulary vocabObject)
    {
        boolean success;

        try
        {      
            this.connectToDb();

            this.callableProcedure = this.con.prepareCall(" {CALL Add_Vocabulary(?,?,?)} "); 

            this.callableProcedure.setString(1, vocabObject.getEnglishWord());
            this.callableProcedure.setString(2, vocabObject.getForeignWord());
            this.callableProcedure.setString(3, vocabObject.getForeignLanguage());

            this.callableProcedure.executeUpdate();

            success = true;

            if(success)
            {
                this.con.commit();
                this.logger.info(vocabObject.toString() + " has been added to the database.");
            } 
        }
        catch(final SQLException e)
        {
            JOptionPane.showMessageDialog(null, vocabObject.getEnglishWord() + " is already in the database.", "Note", JOptionPane.INFORMATION_MESSAGE);
            success = false;
            this.logger.error(e.toString());
        }
        
        return success;
    }
    

    /**
     * Deletes a vocabulary in the database. 
     * @param englishVocab selected English vocabulary
     */
    public void deleteVocabulary(final String englishVocab)
    {
        try
        {
            this.connectToDb();

            this.callableProcedure = this.con.prepareCall(" {CALL Delete_Vocabulary(?,?)} ");

            this.callableProcedure.setString(1, englishVocab);
            this.callableProcedure.setString(2, MainFrame.getforeignLanguage());

            this.callableProcedure.executeUpdate();
            this.con.commit();
            this.logger.info(englishVocab + " in " + MainFrame.getforeignLanguage() + " has been deleted from the database.");
        }
        catch(final SQLException e)
        {
            this.logger.error(e.toString());
        }
    }
    
    /**
     * Updates a vocabulary in the database.
     * @param vocabObject object containing English and foreign word counterpart, and foreign language.
     * @param column flag that checks which vocabulary is edited<pre>            0 - English word is edited. 
            1 - Foreign word is edited.</pre>    
     */
    public void updateVocabulary(final Vocabulary vocabObject, final int column)
    {
        try
        {
            this.connectToDb();

            this.callableProcedure = this.con.prepareCall(" {CALL Update_Vocabulary(?,?,?,?)} ");

            this.callableProcedure.setString(1, vocabObject.getEnglishWord()); 
            this.callableProcedure.setString(2, vocabObject.getForeignWord()); 
            this.callableProcedure.setString(3, vocabObject.getForeignLanguage()); 
            this.callableProcedure.setInt(4, column); 

            this.callableProcedure.executeUpdate();
            this.con.commit();
            this.logger.info(vocabObject.toString() + " has been update.");
        }
        catch(final SQLException e)
        {
            final String[] newWord = vocabObject.getEnglishWord().split("/");
                    
            JOptionPane.showMessageDialog(null, newWord[newWord.length - 1] + " is already in the database.", "Note", JOptionPane.INFORMATION_MESSAGE);
            this.logger.error(e.toString());
        }
    }
    
    /**
     * Retrieves all vocabulary in the database starting with the selected letter.
     * @param letter selected letter in the sort combobox
     * @return vocabularies stored in an 2D array Object
    */
    public Vocabulary[] getVocabularies(final String letter)
    {      
        final int total;
        
        try
        {     
            this.connectToDb();

            this.callableProcedure = con.prepareCall(" {CALL Show_Vocabulary(?,?,?)} ");

            this.callableProcedure.setString(1, MainFrame.getforeignLanguage());
            this.callableProcedure.setString(2, letter);
            this.callableProcedure.registerOutParameter(3, Types.INTEGER); // total vocabulary

            this.rs = this.callableProcedure.executeQuery();

            total = this.callableProcedure.getInt(3); 

            final Vocabulary vocab[] = new Vocabulary[total];

            for(int i=0; this.rs.next(); i++) 
            {
                vocab[i] = new Vocabulary( rs.getString("english_word"), rs.getString("foreign_word"));
            }

            return vocab;   
        }
        catch(final SQLException e)
        {
            this.logger.error(e.toString());
        }
        
        return null;       
    }
    
    /**
     * Connects to the database, if not connected, and sets auto commit to false. 
     * @throws SQLException
     */
    private void connectToDb() throws SQLException 
    {       
        if(this.con == null || false == this.con.isValid(1))
        {
            this.con = MySQLConnector.connect();
            this.con.setAutoCommit(false); 
        } 
    }
    
    /**
     * Closes the connection
     */
    public void closeDB()
    {
        try
        {
            if(this.con != null)
            {
                this.con.close();
            }
            if(this.callableProcedure != null)
            {
                this.callableProcedure.close();
            }
        }
        catch (final SQLException ex)
        {
            this.logger.error(ex.toString());
        }
    }
}