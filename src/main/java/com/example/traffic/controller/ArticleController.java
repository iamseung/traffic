package com.example.traffic.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.traffic.dto.EditArticleDto;
import com.example.traffic.dto.WriteArticleDto;
import com.example.traffic.entity.Article;
import com.example.traffic.service.ArticleService;
import com.example.traffic.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/boards")
public class ArticleController {
    private final AuthenticationManager authenticationManager;
    private final ArticleService articleService;

    private final CommentService commentService;

    @Autowired
    public ArticleController(AuthenticationManager authenticationManager, ArticleService articleService, CommentService commentService) {
        this.authenticationManager = authenticationManager;
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @PostMapping("/{boardId}/articles")
    public ResponseEntity<Article> writeArticle(@PathVariable Long boardId,
                                                @RequestBody WriteArticleDto writeArticleDto) throws JsonProcessingException {
        return ResponseEntity.ok(articleService.writeArticle(boardId, writeArticleDto));
    }

    @GetMapping("/{boardId}/articles")
    public ResponseEntity<List<Article>> getArticle(@PathVariable Long boardId,
                                                    @RequestParam(required = false) Long lastId,
                                                    @RequestParam(required = false) Long firstId) {
        if (lastId != null) {
            return ResponseEntity.ok(articleService.getOldArticle(boardId, lastId));
        }
        if (firstId != null) {
            return ResponseEntity.ok(articleService.getNewArticle(boardId, firstId));
        }
        return ResponseEntity.ok(articleService.firstGetArticle(boardId));
    }

    @GetMapping("/{boardId}/articles/search")
    public ResponseEntity<List<Article>> searchArticle(@PathVariable Long boardId,
                                                    @RequestParam(required = true) String keyword) {
        if (keyword != null) {
            return ResponseEntity.ok(articleService.searchArticle(keyword));
        }
        return ResponseEntity.ok(articleService.firstGetArticle(boardId));
    }

    @PutMapping("/{boardId}/articles/{articleId}")
    public ResponseEntity<Article> editArticle(@PathVariable Long boardId, @PathVariable Long articleId,
                                               @RequestBody EditArticleDto editArticleDto) throws JsonProcessingException {
        return ResponseEntity.ok(articleService.editArticle(boardId, articleId, editArticleDto));
    }

    @DeleteMapping("/{boardId}/articles/{articleId}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long boardId, @PathVariable Long articleId) throws JsonProcessingException {
        articleService.deleteArticle(boardId, articleId);
        return ResponseEntity.ok("article is deleted");
    }

    @GetMapping("/{boardId}/articles/{articleId}")
    public ResponseEntity<Article> getArticleWithComment(@PathVariable Long boardId, @PathVariable Long articleId) throws JsonProcessingException {
        CompletableFuture<Article> article = commentService.getArticleWithComment(boardId, articleId);
        return ResponseEntity.ok(article.resultNow());
    }
}
