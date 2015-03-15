/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.db;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Aaron
 */
@Entity
@Table(name = "vocabulary",
       uniqueConstraints = @UniqueConstraint(columnNames={"english_word", "foreign_word"}))
public class Vocabulary implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    
    @Column(name = "english_word", length = 70, nullable = false)
    private String englishWord;

    @Column(name = "foreign_word", length = 100, nullable = false)
    private String foreignWord;

    @Column(name = "datein", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIn;

    @Column(name = "last_updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Column(name = "foreign_id", nullable = false)
    private int foreignId;

    /**
     * No-arg constructor.
     */
    public Vocabulary()
    {
        this.dateIn = new Date();
        this.lastUpdated = new Date();
    }

    public Vocabulary(final String englishWord, final String foreignWord, final ForeignLanguage foreignLanguage)
    {
        this.englishWord = englishWord;
        this.foreignWord = foreignWord;
        this.foreignId = foreignLanguage.getId();
        this.dateIn = new Date();
        this.lastUpdated = new Date();
    }

    public Vocabulary(final String englishWord, final String foreignWord)
    {
        this.englishWord = englishWord;
        this.foreignWord = foreignWord;
        this.foreignId = 1;
        this.dateIn = new Date();
        this.lastUpdated = new Date();
    }

    /**
     * @param englishWord the englishWord to set
     */
    public void setEnglishWord(String englishWord)
    {
        this.englishWord = englishWord;
    }

    /**
     * @return the englishWord
     */
    public String getEnglishWord()
    {
        return this.englishWord;
    }

    /**
     * @param foreignWord the foreignWord to set
     */
    public void setForeignWord(String foreignWord) 
    {
        this.foreignWord = foreignWord;
    }

    /**
     * @return the foreignWord
     */
    public String getForeignWord()
    {
        return this.foreignWord;
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) 
    {
        this.id = id;
    }

    /**
     * @return the dateIn
     */
    public Date getDateIn() 
    {
        return this.dateIn;
    }

    /**
     * @param dateIn the dateIn to set
     */
    public void setDateIn(Date dateIn) 
    {
        this.dateIn = dateIn;
    }

    /**
     * @return the lastUpdated
     */
    public Date getLastUpdated()
    {
        return this.lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(Date lastUpdated) 
    {
        this.lastUpdated = (Date) lastUpdated.clone();
    }

    /**
     * @param foreignId the foreignId to set
     */
    public void setForeignId(int foreignId) 
    {
        this.foreignId = foreignId;
    }

    /**
     * @return the foreignId
     */
    public int getForeignId()
    {
        return this.foreignId;
    }
    
    /**
     * Returns an array Object containing the English and Foreign word. To be used in adding a row to a JTable
     * @return a Object[]
     */
    public Object[] getVocabularyAsObject()
    {
        final Object[] vocabObject = { this.getEnglishWord(), this.getForeignWord() };

        return vocabObject;
    }
    
    /**
     * Checks all attribute for equality.
     * @param o Vocabulary to compare
     * @return true if equals, else false
     */
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Vocabulary)) // object being compared is not Vocabulary
        {
            return false;
        }
        else if(o == this) // the reference of the objects being compared is equal to each other
        {
            return true;
        }
        else
        {
            Vocabulary that = (Vocabulary) o;
            
            return this.englishWord.equals(that.getEnglishWord()) && 
                   this.foreignWord.equals(that.getForeignWord()) &&
                   this.foreignId == that.getForeignId();
        }
    }

    /**
     * Returns a unique hash code of the Vocabulary object.
     * @return int
     */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.englishWord);
        hash = 47 * hash + Objects.hashCode(this.foreignWord);
        hash = 47 * hash + Objects.hashCode(this.foreignId);
        return hash;
    }
    
    /**
     * Returns the content of the Vocabulary object in a formatted String.
     * @return String
     */
    @Override
    public String toString()
    {
        return "English: " + this.englishWord + " " + ForeignLanguage.values()[this.foreignId - 1] + ": " + this.foreignWord;
    }
}
