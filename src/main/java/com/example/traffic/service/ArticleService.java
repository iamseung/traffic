package com.example.traffic.service;

import com.example.traffic.dto.WriteArticleDto;
import com.example.traffic.entity.Article;
import com.example.traffic.entity.Board;
import com.example.traffic.entity.User;
import com.example.traffic.exception.ResourceNotFoundException;
import com.example.traffic.repository.ArticleRepository;
import com.example.traffic.repository.BoardRepository;
import com.example.traffic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final BoardRepository boardRepository;;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(BoardRepository boardRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public Article writeArticle(WriteArticleDto dto) {
        // 작성자 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<User> author = userRepository.findByUsername(userDetails.getUsername());
        Optional<Board> board = boardRepository.findById(dto.getBoardId());

        if (author.isEmpty()) {
            throw new ResourceNotFoundException("author not found");
        }

        if (board.isEmpty()) {
            throw new ResourceNotFoundException("board not found");
        }

        Article article = Article.builder()
                .author(author.get())
                .board(board.get())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        articleRepository.save(article);

        return article;
    }

    public List<Article> getFirstArticle(Long boardId) {
        return articleRepository.findTop100ByBoardIdOrOrderByCreatedDateDesc(boardId);
    }

    public List<Article> getOldArticle(Long boardId, Long articleId) {
        return articleRepository.findTop10ByBoardIdAndArticleIdLessThanOrderByCreatedDateDesc(boardId, articleId);
    }

    public List<Article> getNewArticle(Long boardId, Long articleId) {
        return articleRepository.findTop10ByBoardIdAndArticleIdGreaterThanOrderByCreatedDateDesc(boardId, articleId);
    }
}
