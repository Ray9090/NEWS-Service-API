package com.newsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newsservice.model.News;
import com.newsservice.model.ReadStatus;
import com.newsservice.model.User;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
	
	List<ReadStatus> findNewsByNewsAndUser(News news, User user);

}
