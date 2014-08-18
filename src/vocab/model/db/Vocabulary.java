/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab.model.db;

import java.util.Objects;
import vocab.view.ForeignLanguage;

/**
 *
 * @author Aaron
 */
public final class Vocabulary
{
    private final String englishWord;
    private final String foreignWord;
    private final ForeignLanguage foreignLanguage;
    
    public Vocabulary(final String englishWord, final String foreignWord, final ForeignLanguage foreignLanguage)
    {
        this.englishWord = englishWord;
        this.foreignWord = foreignWord;
        this.foreignLanguage = foreignLanguage;
    }

    public Vocabulary(final String englishWord, final String foreignWord)
    {
        this.englishWord = englishWord;
        this.foreignWord = foreignWord;
        this.foreignLanguage = null;
    }
    
    public String getEnglishWord()
    {
        return this.englishWord;
    }

    public String getForeignWord()
    {
        return this.foreignWord;
    }

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
        return "English: " + this.englishWord + " " + this.foreignLanguage + ": " + this.foreignWord;
    }
}
