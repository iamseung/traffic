package com.example.traffic.service;

import com.example.traffic.dto.WriteArticleDto;
import com.example.traffic.entity.Article;
import com.example.traffic.entity.Board;
import com.example.traffic.entity.User;
import com.example.traffic.exception.ResourceNotFoundException;
import com.example.traffic.repository.ArticleRepository;
import com.example.traffic.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {
    private final BoardRepository boardRepository;;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(BoardRepository boardRepository, ArticleRepository articleRepository) {
        this.boardRepository = boardRepository;
        this.articleRepository = articleRepository;
    }

    public Article writeArticle(WriteArticleDto dto, User author) {
        Optional<Board> board = boardRepository.findById(dto.getBoardId());

        if (board.isEmpty()) {
            throw new ResourceNotFoundException("board not found");
        }

        Article article = Article.builder()
                .author(author)
                .board(board.get())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        articleRepository.save(article);

        return article;
    }
}
