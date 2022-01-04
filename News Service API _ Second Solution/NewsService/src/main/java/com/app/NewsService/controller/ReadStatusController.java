package com.app.NewsService.controller;

import com.app.NewsService.service.ReadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@RestController
@RequestMapping("/read-status")
public class ReadStatusController {
    ReadStatusService readStatusService;

    @Autowired
    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    @PostMapping("/set")
    public ResponseEntity<Object> setReadStatus(@RequestParam Integer accountId, @RequestParam Integer newsId) {
        return readStatusService.setReadStatus(accountId, newsId);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listAllReadStatuses(@RequestParam String role) {
        if (role.equals("admin")) {
            return readStatusService.allReadStatuses();
        } else {
            return new ResponseEntity<>("You are not an authorized user", HttpStatus.OK);
        }
    }
}
