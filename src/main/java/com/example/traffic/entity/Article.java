package com.example.traffic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
// 게시글
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // 외래키 설정 X
    private User author;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Board board;

    @OneToMany
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @CreatedDate
    @Column(insertable = true)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    @Builder
    public Article(User author, Board board, String title, String content) {
        this.author = author;
        this.board = board;
        this.title = title;
        this.content = content;
        this.isDeleted = false;
        this.viewCount = 0L;
        this.createdDate = LocalDateTime.now();
    }
}