/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron.desktop.entity;

import com.aaron.desktop.constant.ForeignLanguageName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Aaron
 */
@NamedQuery(name=ForeignLanguage.GET_ALL, query="SELECT f FROM ForeignLanguage f")  
@Entity
@Table(name = "foreign_language")
public class ForeignLanguage implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String GET_ALL = "ForeignLanguage.getForeignLanguages";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private short id;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", length = 30, nullable = false)
    private ForeignLanguageName language;

    @OneToMany(mappedBy = "foreignLanguage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vocabulary> vocabularies = new ArrayList<>();
    
    /**
     * No-arg constructor.
     */
    public ForeignLanguage()
    {
    }

    public short getId()
    {
        return id;
    }

    public void setId(short id)
    {
        this.id = id;
    }

    public ForeignLanguageName getLanguage()
    {
        return language;
    }
    
    public String getLanguageName()
    {
        return language.toString();
    }

    public void setLanguage(ForeignLanguageName language)
    {
        this.language = language;
    }

    public List<Vocabulary> getVocabularies()
    {
        return vocabularies;
    }

    public void setVocabularies(List<Vocabulary> vocabularies)
    {
        this.vocabularies = vocabularies;
    }
    
    public void addVocabulary(Vocabulary vocabulary)
    {
        vocabularies.add(vocabulary);
        vocabulary.setForeignLanguage(this);
    }
 
    public void removeVocabulary(Vocabulary vocabulary)
    {
        vocabularies.remove(vocabulary);
        vocabulary.setForeignLanguage(null);
    }

    public void removeAllVocabularies()
    {
        vocabularies.clear();
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.language);
        hash = 97 * hash + Objects.hashCode(this.vocabularies);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ForeignLanguage other = (ForeignLanguage) obj;
        if (this.id != other.id)
        {
            return false;
        }
        if (this.language != other.language)
        {
            return false;
        }
        if (!Objects.equals(this.vocabularies, other.vocabularies))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "ForeignLanguage{" + "id=" + id + ", language=" + language + ", vocabularies=" + vocabularies.size() + '}';
    }
}
