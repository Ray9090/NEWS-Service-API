package com.example.userservice.service.client;


import com.example.userservice.dto.NewsDTO;
import com.example.userservice.dto.PictureDTO;
import com.example.userservice.model.News;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "news-service")
public interface NewsServiceClient {

    /*
     *   News Requests
     */
    @PostMapping(value = "/news")
    public ResponseEntity<News> addNewNews(@RequestBody NewsDTO news);

    @GetMapping(value = "/news/userNews")
    public ResponseEntity<News> getNewsByUserId(@RequestParam(value = "userId") Long userId,
                                                @RequestParam(value = "newsId") Long newsId );
    @GetMapping(value = "/news/allNewsByUserId")
    public ResponseEntity<List<News>> getAllNewsByUserId(@RequestParam(value = "userId") Long userId);

    @GetMapping(value = "/news/allNews")
    public ResponseEntity<List<News>> getAllNews();

    @GetMapping(value = "/news/newsId")
    public ResponseEntity<News> getNewsId(@RequestParam(value = "newsId") Long newsId );

    @PutMapping(value = "/news/updateNews")
    public ResponseEntity<News> updateNews(@RequestBody NewsDTO newsDTO);

    @DeleteMapping(value = "/news/deleteNews/{id}")
    public ResponseEntity<String> deleteNews( @PathVariable Long id);


    /*
     *   Picture Requests
     */
    @PostMapping(value = "/image/{id}/uploadImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPicture(@PathVariable("id") Long newsId,
                                              @RequestPart(name = "file") MultipartFile file);

    @GetMapping(path = { "/image/get/{id}" })
    public ResponseEntity<PictureDTO> getPicture(@PathVariable("id") Long imageId);


    /*
     *   Read Status Requests
     */
    @PostMapping(value = "/readStatus")
    public ResponseEntity<String> setReadStatus(@RequestParam(value = "userId") Long userId,
                                                @RequestParam(value = "newsId") Long newsId);
}
