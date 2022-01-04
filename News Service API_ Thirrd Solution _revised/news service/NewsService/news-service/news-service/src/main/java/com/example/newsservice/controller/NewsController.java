package com.example.newsservice.controller;


import com.example.newsservice.dto.NewsDTO;
import com.example.newsservice.model.News;
import com.example.newsservice.service.NewsServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    @Autowired
    NewsServiceImp newsService;

    @PostMapping
    public ResponseEntity<News> addNewNews(@RequestBody NewsDTO news){
        return new ResponseEntity<News>(newsService.saveNewNews(news), HttpStatus.CREATED);
    }

    /**
     * Find one News by User ID and News ID
     * @param userId
     * @param newsId
     * @return
     */
    @GetMapping(value = "/userNews")
    public ResponseEntity<News> getNewsByUserId(@RequestParam(value = "userId") Long userId,
                                                      @RequestParam(value = "newsId") Long newsId ){
        return new ResponseEntity<>(newsService.findNewsByIdAndUserId(userId, newsId), HttpStatus.OK);
    }

    /**
     * Get all news for a publisher or admin by id
     * @param userId
     * @return
     */
    @GetMapping(value = "/allNewsByUserId")
    public ResponseEntity<List<News>> getAllNewsByUserId(@RequestParam(value = "userId") Long userId){
        return new ResponseEntity<>(newsService.findAllNewsByUserId(userId), HttpStatus.OK);
    }

    /**
     * Find All 10 Last news
     * @return
     */
    @GetMapping(value = "/allNews")
    public ResponseEntity<List<News>> getAllNews(){
        return new ResponseEntity<>(newsService.findAllNews(), HttpStatus.OK);
    }

    /**
     * Find one News by User ID and News ID
     * @param newsId
     * @return
     */
    @GetMapping(value = "/newsId")
    public ResponseEntity<News> getNewsId(@RequestParam(value = "newsId") Long newsId ){
        return new ResponseEntity<News>(newsService.findNewsById(newsId), HttpStatus.OK);
    }


    /**
     * Update a News by user Creator
     * @param newsDTO
     * @return
     */
    @PutMapping(value = "/updateNews")
    public ResponseEntity<News> updateNews(@RequestBody NewsDTO newsDTO){
        return new ResponseEntity<>(newsService.updateNews(newsDTO), HttpStatus.OK);
    }

    /**
     * Delete an article(news) by ID
     * @param id
     * @return
     */
    @DeleteMapping(value = "/deleteNews/{id}")
    public ResponseEntity<String> deleteNews( @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(newsService.deleteNews(id));
    }


}
