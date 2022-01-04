package com.newsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsservice.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {
	
	List<News> findNewsByIsPublished(Boolean isPublished);

}
