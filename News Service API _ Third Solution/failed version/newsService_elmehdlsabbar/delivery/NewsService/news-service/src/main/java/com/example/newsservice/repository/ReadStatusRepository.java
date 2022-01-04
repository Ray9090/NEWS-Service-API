package com.example.newsservice.repository;

import com.example.newsservice.model.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {

    Optional<ReadStatus> findByAccountIDAndNewsID(Long userId, Long newsId);
}
