package com.app.NewsService.controller;

import com.app.NewsService.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@RestController
@RequestMapping("/pictures")
public class PictureController {

    PictureService pictureService;

    @Autowired
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

/*    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) {
        Picture picture = pictureService.getPictureById(fileId).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(picture.getPictureType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\"" + picture.getPictureName() + "\"")
                .body(new ByteArrayResource(picture.getMetadata()));
    }
*/

    @GetMapping("/byNewsId")
    public ResponseEntity<Object> listOfPictures(@RequestParam Integer newsId) {
        return pictureService.getPictureByNewsId(newsId);
    }

}
