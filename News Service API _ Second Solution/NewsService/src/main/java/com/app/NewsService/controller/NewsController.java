package com.app.NewsService.controller;

import com.app.NewsService.model.News;
import com.app.NewsService.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/news")
public class NewsController {
    NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllNews() {
        return newsService.getAllNews();
    }

    @PostMapping("/create")
    public ResponseEntity<Object> addNews(News news, @RequestParam("files") MultipartFile[] files, @RequestParam String role) {
        if (role.equals("admin") || role.equals("publisher")) {
            for (MultipartFile file : files) {
                newsService.saveNews(news, file);
            }
            return new ResponseEntity<>("News is successfully Added", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You are not an authorized user", HttpStatus.OK);
        }
    }

    @GetMapping("/readById")
    public ResponseEntity<Object> getNewsById(@RequestParam(value = "newsId") Integer newsId) {
        return newsService.getNewsById(newsId);
    }

    @PutMapping("/edit")
    public ResponseEntity<Object> updateNews(@RequestBody News news, @RequestParam String role) {
        if (role.equals("admin") || role.equals("publisher")) {
            return newsService.updateNews(news);
        } else {
            return new ResponseEntity<>("You are not an authorized user", HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteNews(@RequestParam(value = "newsId") Integer newsId, @RequestParam String role) {
        if (role.equals("admin")) {
            return newsService.deleteNews(newsId);

        } else {
            return new ResponseEntity<>("You are not an authorized user", HttpStatus.OK);
        }
    }
}
