package com.app.NewsService.service;

import com.app.NewsService.model.News;
import com.app.NewsService.model.Picture;
import com.app.NewsService.repository.NewsRepository;
import com.app.NewsService.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PictureService {
    PictureRepository pictureRepository;
    NewsRepository newsRepository;

    @Autowired
    public PictureService(PictureRepository pictureRepository, NewsRepository newsRepository) {
        this.pictureRepository = pictureRepository;
        this.newsRepository = newsRepository;
    }

/*
    public Optional<Picture> getPictureById(Integer pictureId) {
        return pictureRepository.findById(pictureId);
    }
*/

    public ResponseEntity<Object> getPictureByNewsId(Integer newsId) {
        HashMap<String, Object> newsResult = new HashMap<>();
        try {
            Optional<News> news = newsRepository.findById(newsId);
            if (news.isPresent()) {
                List<Picture> pictureList = news.get().getPictures();
                if (pictureList.isEmpty()) {
                    newsResult.put("Images", "There are no Images");
                } else {
                    String path = Paths.get("").toAbsolutePath().toString();
                    String downloadFolderPath = path + "/src/main/resources/downloads";
                    List<String> imagesPath = new ArrayList<>();
                    for (Picture picture : pictureList
                    ) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(picture.getMetadata());
                        BufferedImage bImage2 = ImageIO.read(bis);
                        String format = picture.getPictureType();
                        ImageIO.write(bImage2, format.substring(6), new File(downloadFolderPath + "/" + picture.getPictureName()));

                        String str = downloadFolderPath + "/" + picture.getPictureName();
                        str = str.replaceAll("\\\\", "/");
                        imagesPath.add(str);
                    }
                    newsResult.put("Pictures URL", imagesPath);
                }
                return new ResponseEntity<>(newsResult, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There is no news against this Id", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
