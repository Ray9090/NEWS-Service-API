package com.app.NewsService.service;

import com.app.NewsService.model.News;
import com.app.NewsService.model.Picture;
import com.app.NewsService.repository.AccountRepository;
import com.app.NewsService.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    AccountRepository accountRepository;
    NewsRepository newsRepository;

    @Autowired
    public NewsService(AccountRepository accountRepository, NewsRepository newsRepository) {
        this.accountRepository = accountRepository;
        this.newsRepository = newsRepository;
    }

    public ResponseEntity<Object> saveNews(News news, MultipartFile file) {
        String pictureName = file.getOriginalFilename();
        try {
            Picture picture = new Picture(pictureName, file.getContentType(), file.getBytes());
            List<Picture> pictureList = news.getPictures();
            pictureList.add(picture);
            newsRepository.save(news);
            return new ResponseEntity<>("Successfully added", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<Object> getAllNews() {
        try {
            HashMap<String, Object> newsResult = new HashMap<>();

            List<News> newsList = newsRepository.findAll();
            if (newsList.isEmpty()) {
                return new ResponseEntity<>("Empty database", HttpStatus.OK);
            } else {
                for (News news : newsList
                ) {
                    newsResult.put("News", news);
                    List<Picture> pictureList = news.getPictures();
                    if (pictureList.isEmpty()) {
                        newsResult.put("Images Path for NewsID- " + news.getNewsId(), "There are no Images");
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
                        newsResult.put("Images Path for NewsID- " + news.getNewsId(), imagesPath);
                    }

                }
                return new ResponseEntity<>(newsResult, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getCause(), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> getNewsById(Integer newsId) {
        HashMap<String, Object> newsResult = new HashMap<>();
        try {
            Optional<News> news = newsRepository.findById(newsId);
            if (news.isPresent()) {
                newsResult.put("News", news);
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
                return new ResponseEntity<>("There are no news against this Id", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<Object> updateNews(News news) {
        Optional<News> existingNews = newsRepository.findById(news.getNewsId());
        if (existingNews.isPresent()) {
            existingNews.get().setNewsId(news.getNewsId());
            existingNews.get().setTitle(news.getTitle());
            existingNews.get().setText(news.getText());
            existingNews.get().setCreationDate(news.getCreationDate());
            existingNews.get().setValidFrom(news.getValidFrom());
            existingNews.get().setValidTo(news.getValidTo());
            existingNews.get().setPictures(news.getPictures());
            newsRepository.save(existingNews.get());
            return new ResponseEntity<>("News is successfully updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("There is no news against this ID", HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> deleteNews(Integer newsId) {
        Optional<News> news = newsRepository.findById(newsId);
        if (news.isPresent()) {
            newsRepository.delete(news.get());
            return new ResponseEntity<>("News is successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("There is no news against this ID", HttpStatus.OK);
        }
    }

}
