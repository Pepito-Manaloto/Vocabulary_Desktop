/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.entity;

import com.aaron.desktop.model.others.VocabularyTableCell;
import static java.time.LocalDateTime.now;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
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
    private LocalDateTime dateIn;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foreign_id")
    private ForeignLanguage foreignLanguage;

    /**
     * No-arg constructor.
     */
    public Vocabulary()
    {
    }

    public Vocabulary(String englishWord, String foreignWord, ForeignLanguage foreignLanguage)
    {
        this.englishWord = englishWord;
        this.foreignWord = foreignWord;
        this.foreignLanguage = foreignLanguage;
    }
    
    @PrePersist
    protected void onCreate()
    {
        this.dateIn = now();
        this.lastUpdated = now();
    }

    @PreUpdate
    protected void onUpdate()
    {
        this.lastUpdated = now();
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
    public LocalDateTime getDateIn() 
    {
        return this.dateIn;
    }

    /**
     * @param dateIn the dateIn to set
     */
    public void setDateIn(LocalDateTime dateIn) 
    {
        this.dateIn = dateIn;
    }

    /**
     * @return the lastUpdated
     */
    public LocalDateTime getLastUpdated()
    {
        return this.lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(LocalDateTime lastUpdated) 
    {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @param foreignLanguage the foreignLanguage to set
     */
    public void setForeignLanguage(ForeignLanguage foreignLanguage) 
    {
        this.foreignLanguage = foreignLanguage;
    }

    /**
     * @return the foreignLanguage
     */
    public ForeignLanguage getForeignLanguage()
    {
        return this.foreignLanguage;
    }
    
    /**
     * Returns an array Object containing the English and Foreign word. To be used in adding a row to a JTable
     * @return a Object[]
     */
    public Object[] getVocabularyAsObject()
    {
        final Object[] vocabObject = { VocabularyTableCell.newWithIdAndWord(id, englishWord), VocabularyTableCell.newWithIdAndWord(id, foreignWord) };

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
                   this.foreignLanguage.equals(that.getForeignLanguage());
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
        hash = 47 * hash + Objects.hashCode(this.foreignLanguage);
        return hash;
    }
    
    /**
     * Returns the content of the Vocabulary object in a formatted String.
     * @return String
     */
    @Override
    public String toString()
    {
        return "English: " + this.englishWord + " " + foreignLanguage.getLanguage() + ": " + this.foreignWord;
    }
}
