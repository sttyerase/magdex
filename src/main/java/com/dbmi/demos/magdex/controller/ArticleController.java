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

    @Autowired
    ArticleController(ArticleRepository aRepo){
        super();
        this.articleRepository = aRepo;
    } // DEFAULT CONSTRUCTOR

    // GET METHODS
    @GetMapping("/articles/rowcount")
    public ResponseEntity<Long> getRowCount() {
        Long tableRows =  articleRepository.count();
        return ResponseEntity.ok(tableRows);
    } // GETROWCOUNT()

    @GetMapping("/articles/find/all")
    public Iterable<Article> findAllArticles() {
        return articleRepository.findAll();
    } // FINDALLARTICLES()

    @GetMapping("/articles/find/id/{articleId}")
    public ResponseEntity<Article> findArticleById(@PathVariable(value = "articleId") Long articleId)
            throws ResourceNotFoundException {
        Article myArticle =
                articleRepository
                        .findById(articleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Article information not found for id: " + articleId));
        return new ResponseEntity<>(myArticle, HttpStatus.OK);
    } // FINDARTICLESBYID(LONG)

    @GetMapping("/articles/find/title/{articleTitle}")
    public ResponseEntity<Article> findArticleByTitle(@PathVariable(value = "articleTitle") String articleTitle) // RETURNS A SINGLE ARTICLE MATCHING TITLE EXACTLY
            throws ResourceNotFoundException {
        Article myArticle;
        Optional<Article> articleOptional = articleRepository.findByArticleTitle(articleTitle);
        if(articleOptional.isPresent()){
            myArticle = articleOptional.get();
        } else {
            throw new ResourceNotFoundException("Unable to locate article: " + articleTitle);
        } // IF-ELSE
        return new ResponseEntity<>(myArticle, HttpStatus.OK);
    } // FINDARTICLESBYID(LONG)

      // POST METHODS
    @PostMapping("/articles/find/like")
    public Iterable<Article> findArticlesLike(@Valid @RequestBody Article exampleArticle) // RETURNS A SINGLE ARTICLE MATCHING TITLE EXACTLY
            throws ResourceNotFoundException {
        String exampleArticleJSON = "";
        ObjectMapper mapper = new ObjectMapper();
                try {
                    exampleArticleJSON = mapper.writeValueAsString(exampleArticle);
                } catch (JsonProcessingException jpe) {
                    jpe.printStackTrace();
                } // TRY-CATCH
                ExampleMatcher myMatcher = ExampleMatcher.matching()
                .withIgnoreCase(true)
                .withIgnorePaths("articleId")
                .withIgnorePaths("articleMonth")
                .withIgnorePaths("articleYear")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Article> myEx = Example.of(exampleArticle, myMatcher);
        myLogger.debug("EXAMPLE PROBE TYPE: " + myEx.getProbeType());
        List<Article> articleOptional = articleRepository.findAll(myEx);
        if(articleOptional.isEmpty()){
            throw new ResourceNotFoundException("Found no matching articles: " + exampleArticleJSON);
        } // IF
        return articleOptional;
    } // FINDARTICLESBYID(LONG)

    // POST METHODS
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
