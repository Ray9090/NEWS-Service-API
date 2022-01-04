package com.example.newsservice.service;

import com.example.newsservice.dto.NewsDTO;
import com.example.newsservice.exception.NotFoundException;
import com.example.newsservice.exception.NullValueException;
import com.example.newsservice.model.News;
import com.example.newsservice.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsServiceImp implements INewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PictureService pictureService;


    @Override
    public News saveNewNews(NewsDTO news) {
        News news1 = News.builder()
                .title(news.getTitle())
                .text(news.getText())
                .roleRestriction(true)
                .creationDate(LocalDateTime.now())
                .validFrom(news.getValidFrom())
                .validTo(news.getValidTo())
                .userId(news.getUserId())
                .build();

        return newsRepository.save(news1);
    }

    @Override
    public News findNewsByIdAndUserId(Long userId, Long newsId) {
        News news = new News();
        if (userId == null && newsId == null ){
            throw new NullValueException("userID or NewsID");
        }else {
            news =  newsRepository.findByIdAndUserId(newsId, userId).get();
        }
        return news;
    }

    @Override
    public List<News> findAllNewsByUserId(Long id) {
        return newsRepository.findAllByUserIdOrderByCreationDateDesc(id);
    }

    @Override
    public List<News> findAllNews() {
        List<News> newsList = newsRepository.findAllByRoleRestrictionIsTrueOrderByCreationDateDesc();
        if (newsList.isEmpty()) {
            return newsList;
        }else {
            //Remove expired news
            List<News> newsListFiltered = newsList.stream().filter(news -> news.getValidTo().isAfter(LocalDate.now()))
                    .collect(Collectors.toList());
            //return last 10 news
            return newsListFiltered.stream().limit(10).collect(Collectors.toList());
        }
    }

    @Override
    public News findNewsById(Long newsId) {
        Optional<News> news = newsRepository.findById(newsId);

        if (news.isPresent()){
            return news.get();
        }else {
            throw new NotFoundException("News");
        }
    }

    @Override
    public News updateNews(NewsDTO newsDTO) {
        if (newsRepository.findById(newsDTO.getId()).isEmpty()) {

            throw new NotFoundException("News");

        }else {
            News news = new News();

             news = newsRepository.findById(newsDTO.getId()).get();
             news.setText(newsDTO.getText());
             news.setTitle(newsDTO.getTitle());
             news.setValidFrom(newsDTO.getValidFrom());
             news.setValidTo(news.getValidTo());
             news.setRoleRestriction(newsDTO.getRoleRestriction());

            return newsRepository.save(news);
        }
    }

    @Override
    public String deleteNews(Long id) {
        if (newsRepository.findById(id).isEmpty()) {

            throw new NotFoundException("News");

        }else {
            News news = newsRepository.findById(id).get();

            newsRepository.delete(news);
            pictureService.deletePicture(news.getPictureId());

            return "News deleted successfully";
        }
    }


}
