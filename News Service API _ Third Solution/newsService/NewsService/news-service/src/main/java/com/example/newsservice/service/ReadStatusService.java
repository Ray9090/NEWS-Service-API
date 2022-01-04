package com.example.newsservice.service;


import com.example.newsservice.model.ReadStatus;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReadStatusService implements IReadStatusService{

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public String setReadStatus(Long userId, Long newsId) {
        Optional<ReadStatus> readStatus = readStatusRepository.findByAccountIDAndNewsID(userId, newsId);

        if (readStatus.isPresent()){

            readStatus.get().setReadDate(LocalDateTime.now());
            readStatusRepository.save(readStatus.get());

            return "News already read";

        }else {

            if (newsRepository.findById(newsId).isPresent()) {
                ReadStatus readStatus1 = ReadStatus.builder()
                        .accountID(userId)
                        .newsID(newsId)
                        .readDate(LocalDateTime.now())
                        .build();
                readStatusRepository.save(readStatus1);

                return "News mark as read";

            }else {
                return "News not found!";
            }

        }
    }
}
