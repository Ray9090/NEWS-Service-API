package com.example.newsservice.service;

import com.example.newsservice.dto.NewsDTO;
import com.example.newsservice.model.News;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface INewsService {
    News saveNewNews(NewsDTO news);

    News findNewsByIdAndUserId(Long userId, Long newsId);

    List<News> findAllNewsByUserId(Long id);

    News updateNews(NewsDTO newsDTO);

    String deleteNews(Long id);

    List<News> findAllNews();

    News findNewsById(Long newsId);
}
