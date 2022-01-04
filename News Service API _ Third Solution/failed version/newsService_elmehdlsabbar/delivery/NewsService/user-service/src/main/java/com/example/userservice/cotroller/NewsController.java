package com.example.userservice.cotroller;

import com.example.userservice.dto.NewsDTO;
import com.example.userservice.dto.PictureDTO;
import com.example.userservice.model.News;
import com.example.userservice.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/users-news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @Autowired
    protected PasswordEncoder passwordEncoder;


    @PostMapping(value = "/new")
    public ResponseEntity<News> addNewNews(@RequestBody NewsDTO news){
        return new ResponseEntity<News>(newsService.saveNewNews(news), HttpStatus.CREATED);
    }

    @GetMapping(value = "/userNews")
    public ResponseEntity<News> getNewsByUserId(@RequestParam(value = "newsId") Long newsId ){
        return new ResponseEntity<News>(newsService.findNewsByUserId(newsId), HttpStatus.OK);
    }

    @GetMapping(value = "/allNewsByUserId")
    public ResponseEntity<List<News>> getAllNewsByUserId(){
        return new ResponseEntity<>(newsService.findAllNewsById(), HttpStatus.OK);
    }

    @GetMapping(value = "/allNews")
    public ResponseEntity<List<News>> getAllNews(){
        return new ResponseEntity<List<News>>(newsService.findAllNews(), HttpStatus.OK);
    }

    @PutMapping(value = "/updateNews")
    public ResponseEntity<String> updateNews(@RequestBody NewsDTO newsDTO){
        return new ResponseEntity<String>(newsService.updateNews(newsDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteNews/{id}")
    public ResponseEntity<String> deleteNews( @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(newsService.deleteNews(id));
    }

    /* Upload and fetch Picture request */

    @PostMapping(value = "/{id}/uploadImg")
    public ResponseEntity<String> uploadPicture(@PathVariable("id") Long newsId,
                                              @RequestParam(name = "file") MultipartFile file) {

        return new ResponseEntity<String>(newsService.uploadPicture(newsId, file), HttpStatus.OK);
    }


    @GetMapping(path = { "/getImage/{id}" })
    public ResponseEntity<PictureDTO> getPicture(@PathVariable("id") Long imageId) {
        return new ResponseEntity<PictureDTO>(newsService.retrieveImage(imageId), HttpStatus.OK);

    }


    /**
     * Read status
     * @param newsId
     * @return
     */
    @PostMapping(value = "/readStatus")
    public ResponseEntity<String> setReadStatus( @RequestParam(value = "newsId") Long newsId){
        return new ResponseEntity<String>(newsService.setReadStatus(newsId), HttpStatus.OK);
    }

}
