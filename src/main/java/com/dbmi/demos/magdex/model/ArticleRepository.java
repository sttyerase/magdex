package com.dbmi.demos.magdex.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByArticleTitle(String articleTitle);
} // INTERFACE

// JPAREPOSITORY EXTENDS QUERYBYEXAMPLEEXECUTOR