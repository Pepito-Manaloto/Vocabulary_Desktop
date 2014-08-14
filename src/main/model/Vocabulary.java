/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.model;

/**
 *
 * @author Aaron
 */
public final class Vocabulary
{
    private final String englishWord;
    private final String foreignWord;
    private final String foreignLanguage;
    
    public Vocabulary(final String englishWord, final String foreignWord, final String foreignLanguage)
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

    public String getForeignLanguage()
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
    
    @Override
    public String toString()
    {
        return "English: " + this.englishWord + " " + this.foreignLanguage + ": " + this.foreignWord;
    }
}
