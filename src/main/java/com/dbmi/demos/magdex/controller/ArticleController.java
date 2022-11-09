package com.dbmi.demos.magdex.controller;

import com.dbmi.demos.magdex.model.ArticleRepository;
import com.dbmi.demos.magdex.model.Article;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/magdex")
public class ArticleController {
    private final ArticleRepository articleRepository;
    private final Logger myLogger = LoggerFactory.getLogger(this.getClass());
    // ESTABLISH DEFAULT SORT AS YEAR and MONTH DESCENDING, ID ASCENDING
    private final Sort yearMonthSort = Sort.by(Sort.Order.desc("articleYear"), Sort.Order.desc("articleMonth"), Sort.Order.asc("articleId"));

    @Autowired
    ArticleController(ArticleRepository aRepo) {
        super();
        this.articleRepository = aRepo;
    } // DEFAULT CONSTRUCTOR

    // POST METHODS
    @PostMapping("/articles/rowcount")
    public ResponseEntity<Long> getRowCount() {
        Long tableRows = articleRepository.count();
        return ResponseEntity.ok(tableRows);
    } // GETROWCOUNT()

    @PostMapping("/articles/find/all") // ALLOW FOR USER SPECIFIED SORT PARAMETERS
    public Iterable<Article> findAllArticles(@RequestParam(name = "sort", required = false) String sortParam) {
        if (sortParam == null || sortParam.isEmpty()) {
            return articleRepository.findAll(yearMonthSort);
        } else {
            return articleRepository.findAll(Sort.by(sortParam));
        } // IF-ELSE
    } // FINDALLARTICLES()

    // POST METHODS
    @PostMapping("/articles/find/id")
    public Iterable<Article> findArticleById(@Valid @RequestBody Article exampleArticle)
            throws ResourceNotFoundException {
        Article myArticle =
                articleRepository
                        .findById(exampleArticle.getArticleId())
                        .orElseThrow(() -> new ResourceNotFoundException("Article information not found for id: " + exampleArticle.getArticleId()));

        List<Article> theArticleList = new LinkedList<>();
        theArticleList.add(myArticle);
        return theArticleList;
    } // FINDARTICLESBYID(LONG)

    @PostMapping("/articles/find/yearandmonth")
    public Iterable<Article> findArticlesMonthYear(@Valid @RequestBody Article exampleArticle) // RETURNS LIST OF ARTICLES THAT MATCH STRINGS IN THE EXAMPLE
            throws Exception {
        String exampleArticleJSON;
        ObjectMapper mapper = new ObjectMapper();
        try {
            exampleArticleJSON = mapper.writeValueAsString(exampleArticle);
        } catch (JsonProcessingException jpe) {
            throw new Exception("EXCEPTION MAPPING ARTICLE CONTENTS TO JSON: " + jpe.getMessage());
        } // TRY-CATCH
        ExampleMatcher myMatcher = ExampleMatcher.matching()
                .withIgnorePaths("articleId", "articleTitle", "articleAuthor", "articleCategory", "articleSynopsis", "articleKeywords");
        if (exampleArticle.getArticleMonth() == 0) myMatcher = myMatcher.withIgnorePaths("articleMonth");
        Example<Article> myEx = Example.of(exampleArticle, myMatcher);
        List<Article> theArticleList = articleRepository.findAll(myEx, yearMonthSort);
        if (theArticleList.isEmpty()) {
            throw new ResourceNotFoundException("Found no matching articles: " + exampleArticleJSON);
        } // IF
        return theArticleList;
    } // FINDARTICLESBYID(LONG)

    @PostMapping("/articles/find/like")
    public Iterable<Article> findArticlesLike(@Valid @RequestBody Article exampleArticle) // RETURNS LIST OF ARTICLES THAT MATCH STRINGS IN THE EXAMPLE
            throws Exception {
        String exampleArticleJSON;
        ObjectMapper mapper = new ObjectMapper();
        try {
            exampleArticleJSON = mapper.writeValueAsString(exampleArticle);
        } catch (JsonProcessingException jpe) {
            throw new Exception("EXCEPTION MAPPING ARTICLE CONTENTS TO JSON: " + jpe.getMessage());
        } // TRY-CATCH
        ExampleMatcher myMatcher = ExampleMatcher.matching()
                .withIgnoreCase(true)
                .withIgnorePaths("articleId", "articleMonth", "articleYear")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Article> myEx = Example.of(exampleArticle, myMatcher);
        List<Article> theArticleList = articleRepository.findAll(myEx, yearMonthSort);
        if (theArticleList.isEmpty()) {
            throw new ResourceNotFoundException("Found no matching articles: " + exampleArticleJSON);
        } // IF
        return theArticleList;
    } // FINDARTICLESBYID(LONG)

    @PostMapping("/articles/new")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody Article article) {
        return new ResponseEntity<>(articleRepository.save(article), HttpStatus.OK);
    } // CREATEARTICLE(article)

    // PUT METHODS
    @PutMapping("/articles/update/{articleId}")
    public ResponseEntity<Article> updateArticle(
            @PathVariable(value = "articleId") Long articleId, @Valid @RequestBody Article articleDetails)
            throws ResourceNotFoundException {
        Article article =
                articleRepository
                        .findById(articleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Article record not found for id: " + articleId));
        article.setArticleTitle(articleDetails.getArticleTitle());
        article.setArticleAuthor(articleDetails.getArticleAuthor());
        article.setArticleSynopsis(articleDetails.getArticleSynopsis());
        article.setArticleCategory(articleDetails.getArticleCategory());
        article.setArticleKeywords(articleDetails.getArticleKeywords());
        article.setArticleMonth(articleDetails.getArticleMonth());
        article.setArticleYear(articleDetails.getArticleYear());
        final Article updatedArticle = articleRepository.save(article);
        return ResponseEntity.ok(updatedArticle);
    } // UPDATEARTICLE(@PATHVARIABLE)

    // DELETE METHODS
    @DeleteMapping("/articles/delete/id/{articleId}")
    public Map<String, Boolean> deleteArticle(@PathVariable(value = "articleId") Long articleId) throws Exception {
        Article article =
                articleRepository
                        .findById(articleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Article record not found for id: " + articleId));
        articleRepository.delete(article);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    } // DELETEARTICLE(@PATHVARIABLE)

} // CLASS
