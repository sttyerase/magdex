package com.dbmi.demos.magdex.model;

import javax.persistence.*;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private long     articleId;
    @Column(name = "article_title")
    private String articleTitle;
    @Column(name = "article_author")
    private String articleAuthor;
    @Column(name = "article_synopsis")
    private String articleSynopsis;
    @Column(name = "article_category")
    private String  articleCategory;
    @Column(name = "article_keywords")
    private String articleKeywords;
    @Column(name = "article_month")
    private int    articleMonth;
    @Column(name = "article_year")
    private int    articleYear;

    public Article() {}

    public Article(long id, String title, String synopsis, String category, String keywords,
                   int month, int year) {
        this.articleId          = id;
        this.articleTitle       = title;
        this.articleSynopsis    = synopsis;
        this.articleCategory    = category;
        this.articleKeywords     = keywords;
        this.articleMonth       = month;
        this.articleYear        = year;
    } // CONSTRUCTOR(INT,STRING,STRING)

    public long getArticleId() {
        return articleId;
    } // GETARTICLEID()

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleSynopsis() {
        return articleSynopsis;
    }

    public void setArticleSynopsis(String articleSynopsis) {
        this.articleSynopsis = articleSynopsis;
    }

    public String getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        this.articleCategory = articleCategory;
    }

    public String getArticleKeywords() {
        return articleKeywords;
    }

    public void setArticleKeywords(String articleKeywords) {
        this.articleKeywords = articleKeywords;
    }

    public int getArticleMonth() {
        return articleMonth;
    }

    public void setArticleMonth(int articleMonth) {
        this.articleMonth = articleMonth;
    }

    public int getArticleYear() {
        return articleYear;
    }

    public void setArticleYear(int articleYear) {
        this.articleYear = articleYear;
    }
    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

} // CLASS
