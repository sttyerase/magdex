package com.dbmi.demos.magdex.controller;

import com.dbmi.demos.magdex.model.ArticleRepository;
import com.dbmi.demos.magdex.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/magdex")
public class ArticleController {
    private ArticleRepository articleRepository;

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
    } // GETHOME()

    @GetMapping("/articles/find/all")
    public Iterable<Article> findAllArticles() {
        return articleRepository.findAll();
    } // FINDALLarticleS()

    @GetMapping("/articles/find/id/{articleId}")
    public ResponseEntity<Article> getArticlesById(@PathVariable(value = "articleId") Long articleId)
            throws ResourceNotFoundException {
        Article myArticle =
                articleRepository
                        .findById(articleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Article information not found for id: " + articleId));
        return new ResponseEntity<Article>(myArticle,HttpStatus.OK);
    } // FINDCROPSBYID(LONG)

    @GetMapping("/articles/find/name/{articleTitle}")
    public ResponseEntity<Article> getArticlesByTitle(@PathVariable(value = "articleTitle") String articleTitle)
            throws ResourceNotFoundException {
        Article myArticle;
        Optional<Article> articleOptional = articleRepository.findByArticleTitle(articleTitle);
        if(articleOptional.isPresent()){
            myArticle = articleOptional.get();
        } else {
            throw new ResourceNotFoundException("Unable to locate article: " + articleTitle);
        } // IF-ELSE
        return new ResponseEntity<Article>(myArticle,HttpStatus.OK);
    } // FINDCROPSBYID(LONG)

    // POST METHODS
    @PostMapping("/articles/new")
    public Article createArticle(@Valid @RequestBody Article article) {
        return articleRepository.save(article);
    } // CREATECROP(article)

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
    } // UPDATECROP(@PATHVARIABLE)

    // DELETE METHODS
    @DeleteMapping("/articles/delete/{articleId}")
    public Map<String, Boolean> deleteArticle(@PathVariable(value = "articleId") Long articleId) throws Exception {
        Article article =
                articleRepository
                        .findById(articleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Article record not found for id: " + articleId));
        articleRepository.delete(article);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    } // DELETECROP(@PATHVARIABLE)

} // CLASS
