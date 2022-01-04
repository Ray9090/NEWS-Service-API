package com.example.newsservice.service;


import org.springframework.stereotype.Component;

@Component
public interface IReadStatusService {

    String setReadStatus(Long userId, Long newsId);
}
