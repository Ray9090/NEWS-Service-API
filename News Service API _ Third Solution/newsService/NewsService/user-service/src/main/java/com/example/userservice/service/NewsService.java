package com.example.userservice.service;

import com.example.userservice.dto.NewsDTO;
import com.example.userservice.dto.PictureDTO;
import com.example.userservice.model.News;
import com.example.userservice.model.ProfileRole;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.client.NewsServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private NewsServiceClient newsServiceClient;
    @Autowired
    private UserService userService;
    /**
     * Get all News of authenticated user
     * @return List of Item
     */
    public List<News> findAllNewsById() {

        Long userId = userRepository.findByEmail(userService.getUsername()).get().getId();

        List<News> newsList= newsServiceClient.getAllNewsByUserId(userId).getBody();

        return newsList;
    }


    /**
     * Save new News
     * @param newsDto
     * @return
     */
    public News saveNewNews(NewsDTO newsDto) {
        return newsServiceClient.addNewNews(newsDto).getBody();
    }

    /**
     * Find one News article by User ID creator
     * @param newsId
     * @return
     */
    public News findNewsByUserId(Long newsId) {
        Long userId = userRepository.findByEmail(userService.getUsername()).get().getId();

        return newsServiceClient.getNewsByUserId(userId, newsId).getBody();
    }

    /**
     * Find All 10 Last news
     * @return
     */
    public List<News> findAllNews() {
        return newsServiceClient.getAllNews().getBody();
    }

    public String updateNews(NewsDTO newsDTO) {
        User user = userRepository.findByEmail(userService.getUsername()).get();

        News news = newsServiceClient.getNewsByUserId(user.getId(), newsDTO.getId()).getBody();
        if (news.getUserId()== user.getId() || user.getRole().equals(ProfileRole.ADMIN_ROLE) ) {
            ResponseEntity<News> response = newsServiceClient.updateNews(newsDTO);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                return "News Updated";
            }else {
                return "News not Updated";
            }

        }else {
            return "You have not the right to updated this article";
        }

    }

    /**
     * Delete an article(news) by ID
     * @param newsId
     * @return
     */
    public String deleteNews(Long newsId) {
        Optional<User> user = userRepository.findByEmail(userService.getUsername());

        if( user.get().getRole().equals(ProfileRole.ADMIN_ROLE)){
            return newsServiceClient.deleteNews(newsId).getBody();
        }else {
            return "You have not the right to delete this article";
        }
    }

    /*$*********************************  Picture   *************************************/


    /**
     * Upload Picture
     * @param newsId
     * @param file
     * @return
     */
    public String uploadPicture(Long newsId, MultipartFile file) {
        ResponseEntity<News> response = newsServiceClient.getNewsId(newsId);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            ResponseEntity<Void> uploadResponse = newsServiceClient.uploadPicture(newsId, file);
            if (uploadResponse.getStatusCode().equals(HttpStatus.OK)) {
                return "Picture uploaded successfully";
            }else {
                return uploadResponse.getStatusCode().toString();
            }
        }else {
            return "No news article with this ID";
        }
    }

    /**
     * Retrieve Image
     * @param imageId
     * @return
     */
    public PictureDTO retrieveImage(Long imageId) {
        return newsServiceClient.getPicture(imageId).getBody();
    }



    /*$*********************************  Read Status   *************************************/

    /**
     * Set Read status
     * @param newsId
     * @return
     */
    public String setReadStatus(Long newsId) {
        Optional<User> user = userRepository.findByEmail(userService.getUsername());
        return newsServiceClient.setReadStatus(user.get().getId(), newsId).getBody();
    }


}
