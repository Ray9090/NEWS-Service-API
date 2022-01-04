package com.example.newsservice.controller;


import com.example.newsservice.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

    @Autowired
    ReadStatusService statusService;

    @PostMapping
    public ResponseEntity<String> setReadStatus(@RequestParam(value = "userId") Long userId,
                                                @RequestParam(value = "newsId") Long newsId){
        return new ResponseEntity<String>(statusService.setReadStatus(userId, newsId), HttpStatus.OK);

    }
}
