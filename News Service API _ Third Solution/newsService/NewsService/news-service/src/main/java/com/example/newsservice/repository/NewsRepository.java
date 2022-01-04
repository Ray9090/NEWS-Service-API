package com.example.newsservice.repository;

import com.example.newsservice.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findByIdAndUserId(Long newsId, Long ownerId );

    List<News> findAllByUserIdOrderByCreationDateDesc(Long validToId);

    List<News> findAllByRoleRestrictionIsTrueOrderByCreationDateDesc();

}
