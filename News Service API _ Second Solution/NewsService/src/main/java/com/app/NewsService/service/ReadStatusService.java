package com.app.NewsService.service;

import com.app.NewsService.model.Account;
import com.app.NewsService.model.News;
import com.app.NewsService.model.ReadStatus;
import com.app.NewsService.repository.AccountRepository;
import com.app.NewsService.repository.NewsRepository;
import com.app.NewsService.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReadStatusService {
    AccountRepository accountRepository;
    NewsRepository newsRepository;
    ReadStatusRepository readStatusRepository;

    @Autowired
    public ReadStatusService(AccountRepository accountRepository, NewsRepository newsRepository, ReadStatusRepository readStatusRepository) {
        this.accountRepository = accountRepository;
        this.newsRepository = newsRepository;
        this.readStatusRepository = readStatusRepository;
    }

    public ResponseEntity<Object> setReadStatus(Integer accountId, Integer newsId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            Optional<News> news = newsRepository.findById(newsId);
            if (news.isPresent()) {
                ReadStatus readStatus = new ReadStatus();
                long millis = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);
                readStatus.setReadDate(date);
                readStatus.setAccount(account.get());
                readStatus.setNews(news.get());
                readStatusRepository.save(readStatus);
                return new ResponseEntity<>(account.get().getFirstName() + " successfully read the " + news.get().getTitle(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There is no news against this ID", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("There is no account against this ID", HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> allReadStatuses(){
        List<ReadStatus> readStatusList = readStatusRepository.findAll();
        if(readStatusList.isEmpty()){
            return new ResponseEntity<>("There are no read Statuses in the database", HttpStatus.OK);
        }else {
            return new ResponseEntity<>(readStatusList, HttpStatus.OK);
        }
    }
}
