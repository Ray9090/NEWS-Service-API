package com.example.newsservice.controller;

import com.example.newsservice.dto.PictureDTO;
import com.example.newsservice.model.Picture;
import com.example.newsservice.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class PictureController {


    @Autowired
    private PictureService pictureService;

    @PostMapping(value = "/{id}/uploadImg")
    public ResponseEntity<Void> uploadPicture(@PathVariable("id") Long newsId,
                                          @RequestParam(name = "file") MultipartFile file) {
        pictureService.upload(file,"img",newsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(path = { "/get/{id}" })
    public ResponseEntity<PictureDTO> getPicture(@PathVariable("id") Long imageId) throws IOException {
        return new ResponseEntity<PictureDTO>(pictureService.retrieveImage(imageId), HttpStatus.OK);

    }
}
