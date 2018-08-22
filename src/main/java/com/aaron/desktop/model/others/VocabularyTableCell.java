/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.model.others;

/**
 * Represents a single cell in JTable. To keep track of the vocabulary Id.
 * @author Aaron
 */
public class VocabularyTableCell
{
    private int id;
    private String word;

    private VocabularyTableCell(int id, String word)
    {
        this.id = id;
        this.word = word;
    }
    
    public static VocabularyTableCell newWithIdAndWord(int id, String word)
    {
        return new VocabularyTableCell(id, word);
    }

    public int getId()
    {
        return id;
    }

    public String getWord()
    {
        return word;
    }

    @Override
    public String toString()
    {
        return word;
    }
}
