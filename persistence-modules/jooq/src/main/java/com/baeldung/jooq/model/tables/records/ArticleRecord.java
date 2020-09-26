/*
 * This file is generated by jOOQ.
 */
package com.baeldung.jooq.model.tables.records;


import com.baeldung.jooq.model.tables.Article;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ArticleRecord extends UpdatableRecordImpl<ArticleRecord> implements Record4<Integer, String, String, Integer> {

    private static final long serialVersionUID = 1297442421;

    /**
     * Setter for <code>public.article.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.article.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.article.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.article.title</code>.
     */
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.article.description</code>.
     */
    public void setDescription(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.article.description</code>.
     */
    public String getDescription() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.article.author_id</code>.
     */
    public void setAuthorId(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.article.author_id</code>.
     */
    public Integer getAuthorId() {
        return (Integer) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, String, String, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Integer, String, String, Integer> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Article.ARTICLE.ID;
    }

    @Override
    public Field<String> field2() {
        return Article.ARTICLE.TITLE;
    }

    @Override
    public Field<String> field3() {
        return Article.ARTICLE.DESCRIPTION;
    }

    @Override
    public Field<Integer> field4() {
        return Article.ARTICLE.AUTHOR_ID;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getTitle();
    }

    @Override
    public String component3() {
        return getDescription();
    }

    @Override
    public Integer component4() {
        return getAuthorId();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getTitle();
    }

    @Override
    public String value3() {
        return getDescription();
    }

    @Override
    public Integer value4() {
        return getAuthorId();
    }

    @Override
    public ArticleRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public ArticleRecord value2(String value) {
        setTitle(value);
        return this;
    }

    @Override
    public ArticleRecord value3(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public ArticleRecord value4(Integer value) {
        setAuthorId(value);
        return this;
    }

    @Override
    public ArticleRecord values(Integer value1, String value2, String value3, Integer value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ArticleRecord
     */
    public ArticleRecord() {
        super(Article.ARTICLE);
    }

    /**
     * Create a detached, initialised ArticleRecord
     */
    public ArticleRecord(Integer id, String title, String description, Integer authorId) {
        super(Article.ARTICLE);

        set(0, id);
        set(1, title);
        set(2, description);
        set(3, authorId);
    }
}
