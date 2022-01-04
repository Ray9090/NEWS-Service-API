package com.newsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsservice.model.News;
import com.newsservice.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long>{

	List<Picture> findByNews(News news);
}
