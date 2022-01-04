package com.app.NewsService.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    Integer newsId;
    @NotBlank
    @Column(name = "news_title")
    String title;
    @NotBlank
    @Column(name = "news_text")
    String text;
    @NotNull
    @Column(name = "news_creation_date")
    Date creationDate;
    @NotNull
    @Column(name = "news_valid_from")
    Date validFrom;
    @NotNull
    @Column(name = "news_valid_to")
    Date validTo;

    @OneToMany(targetEntity = Picture.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "news_id", referencedColumnName = "news_id")
    private List<Picture> pictures = new ArrayList<>();
}
