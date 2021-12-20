package com.newsservice.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.newsservice.model.News;
import com.newsservice.model.Picture;
import com.newsservice.model.ReadStatus;
import com.newsservice.model.Role;
import com.newsservice.model.User;
import com.newsservice.repository.NewsRepository;
import com.newsservice.repository.PictureRepository;
import com.newsservice.repository.ReadStatusRepository;
import com.newsservice.repository.UserRepository;

@Controller
@RequestMapping("/api-news")
public class NewsController {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    PictureRepository pictureRepository;

    @Autowired
    ReadStatusRepository readStatusRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/news")
    public String getAllNews(Model model) {
        List<News> newsList = new ArrayList<News>();

        newsList = newsRepository.findAll();
        // if user is not Admin, filter the news with : validFrom, to validTo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());
        model.addAttribute("canadd", "false");
        if(us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER"))||us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
            model.addAttribute("canadd", "true");
        }

        model.addAttribute("newsList", newsList);
        return "list-news";
    }

    @GetMapping("/newsNotRead")
    public String getAllNewsNotRead(Model model) {
        List<News> newsList = new ArrayList<News>();
        List<News> newsNotReadList = new ArrayList<News>();
        newsList = newsRepository.findAll();
        
        // if user is not Admin, filter the news with : validFrom, to validTo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());

        for (News news : newsList) {
            List<ReadStatus> rdList = new ArrayList<ReadStatus>();
            rdList = readStatusRepository.findNewsByNewsAndUser(news, us);
            if (rdList.isEmpty() || rdList == null) {
                newsNotReadList.add(news);
            }
        }
        model.addAttribute("canadd", "false");

        if(us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER"))||us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
            model.addAttribute("canadd", "true");
        }

        model.addAttribute("newsList", newsNotReadList);
        return "list-news-by-not-read";
    }

    @GetMapping("/news/{id}/photos")
    public String getAllPhotosOfNews(Model model, @PathVariable("id") long id) {
    	
        List<Picture> photosList = new ArrayList<Picture>();
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            photosList = pictureRepository.findByNews(news.get());

        }
        model.addAttribute("photosList", photosList);
        model.addAttribute("news", news.get());
        
        // if user is not Admin, filter the news with : validFrom, to validTo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());
        
        if ((us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER")) && news.get().getCreatedBy().equals(auth.getName()))
        		|| us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
        	model.addAttribute("canadd", "false");

            if(us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER")) || us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
            	model.addAttribute("canadd", "true");
            }

            return "list-photos";
        }
        RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
        String uri = ((ServletRequestAttributes) ra).getRequest().getHeader("Referer");
        uri = uri.replace("http://localhost:8080", "");
        
        return "redirect:"+ uri;
    }

    @PostMapping("/addNews")
    public String addNews(@Valid News news, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "list-news";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        News _news = newsRepository
                .save(new News(news.getId(), news.getTitle(), news.getText(), new Date(),
                        news.getValidFrom(), news.getValidTo(), false, news.getReadStatus(), news.getPicture(), auth.getName()));
        //newsRepository.save(_news);
        return "redirect:news";
    }

    @GetMapping("/news/{id}/addPhotos")
    public String addPictureForm(@PathVariable("id") Long id, Picture picture, Model model) {
        News news = newsRepository.getById(id);
        model.addAttribute("news", news);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());
        // if the user is publisher, check if it's his own news, to be able to edit it
        if ((us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER")) && news.getCreatedBy().equals(auth.getName()))
        		|| us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
        	return "add-photos";
        }
        return "redirect:/api-news/news/" + id + "/photos";
    }

    @GetMapping("/news/{id}/editPhotos/{pId}")
    @PreAuthorize("hasRole('ROLE_PUBLISHER') or hasRole('ROLE_ADMIN')")
    public String editPictureForm(@PathVariable("id") Long id, @PathVariable("pId") Long pId, Picture picture, Model model) {
        News news = newsRepository.getById(id);
        Picture pic = pictureRepository.findById(pId).get();
        model.addAttribute("news", news);
        model.addAttribute("pictureToUpdate", pic);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());
        // if the user is publisher, check if it's his own news, to be able to edit it
        if ((us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER")) && news.getCreatedBy().equals(auth.getName()))
        		|| us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {      	
            return "edit-photos";
        }
        return "redirect:/api-news/news/" + id + "/photos";
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_PUBLISHER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/news/{id}/editPhotos/{pId}")
    public String updatePhotos(@PathVariable("id") Long id, @PathVariable("pId") Long pId, Model model, @ModelAttribute("pictureToUpdate") Picture picture) {
        Optional<Picture> pictureDataDb = pictureRepository.findById(picture.getId());
        Optional<News> newsDb = newsRepository.findById(id);
        if (pictureDataDb.isPresent()) {
            Picture _picture = pictureDataDb.get();
            _picture.setNews(picture.getNews());
            _picture.setPictureData(picture.getPictureData());
            _picture.setPictureName(picture.getPictureName());
            if (newsDb.isPresent()) {
                _picture.setNews(newsDb.get());
            }
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User us = userRepository.findByUsername(auth.getName());
            // if the user is publisher, check if it's his own news, to be able to edit it
            if ((us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER")) && newsDb.get().getCreatedBy().equals(auth.getName()))
            		|| us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
            	pictureRepository.save(_picture);
            }
            
        }
        return "redirect:/api-news/news/" + id + "/photos";
    }

    @GetMapping("/news/{id}")
    public String GetCurrentNews(@PathVariable("id") Long id, News news, Model model) {
        News newsToRead = newsRepository.getById(id);

        // if user is not Admin, filter the news with : validFrom, to validTo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());
        ReadStatus rds = readStatusRepository
                .save(new ReadStatus(null, newsToRead, us, new Date()));
//		rds.setNews(newsToRead);
//		rds.setUser(us);
//		rds.setReadDate(new Date());
//		rds = readStatusRepository.saveAndFlush(rds);

        List<Picture> photosList = new ArrayList<Picture>();
        photosList = pictureRepository.findByNews(newsToRead);
        model.addAttribute("news", newsToRead);
        model.addAttribute("photosList", photosList);
        return "read-news";
    }

    @PostMapping("/news/{id}/addPhotos")
    public String addPhotos(@PathVariable("id") Long id, @Valid Picture picture, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "list-photos";
        }
        News news = newsRepository.getById(id);

        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());
        // if the user is publisher, check if it's his own news, to be able to edit it
        if ((us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER")) && news.getCreatedBy().equals(auth.getName()))
        		|| us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
        	Picture _picture = pictureRepository
                    .save(new Picture(null, picture.getPictureName(), picture.getPictureData(), news));

        }
                
        //model.addAttribute("news", news);
        //model.addAttribute("photos", _picture);
        return "redirect:photos";
    }

    @GetMapping("/addNews")
    public String addNewsForm(News news) {
        return "add-news";
    }
//	@GetMapping("/news/{id}")
//	public ResponseEntity<News> getTutorialById(@PathVariable("id") long id) {
//		Optional<News> newsData = newsRepository.findById(id);
//
//		if (newsData.isPresent()) {
//			return new ResponseEntity<>(newsData.get(), HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}

//	@PostMapping("/news")
//	public ResponseEntity<News> createTutorial(@RequestBody News news) {
//		try {
//			News _news = newsRepository
//					.save(new News(news.getId(), news.getTitle(), news.getText(), news.getCreationDate(), 
//							news.getValidFrom(), news.getValidTo(), news.getIsPublished(), news.getReadStatus(), news.getPicture()));
//			return new ResponseEntity<>(_news, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

    @GetMapping("/updateNews/{id}")
    @PreAuthorize("hasRole('ROLE_PUBLISHER') or hasRole('ROLE_ADMIN')")
    public String EditNewsForm(@PathVariable("id") Long id, News news, Model model) {
        News newsToUpdate = newsRepository.getById(id);
        model.addAttribute("newsToUpdate", newsToUpdate);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());
        // if the user is publisher, check if it's his own news, to be able to edit it
        if ((us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER")) && newsToUpdate.getCreatedBy().equals(auth.getName()))
        		|| us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
        	return "edit-news";
        }
        RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
        String uri = ((ServletRequestAttributes) ra).getRequest().getHeader("Referer");
        uri = uri.replace("http://localhost:8080", "");
    	return "redirect:" + uri;
    }

    @GetMapping("/deleteNews/{id}")
    public String DeleteNews(@PathVariable("id") Long id, News news, Model model) {
        News newsToUpdate = newsRepository.getById(id);
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());
        // if the user is publisher, check if it's his own news, to be able to edit it
        if ((us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER")) && newsToUpdate.getCreatedBy().equals(auth.getName()))
        		|| us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
        	newsRepository.delete(newsToUpdate);
        }
        RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
        String uri = ((ServletRequestAttributes) ra).getRequest().getHeader("Referer");
        uri = uri.replace("http://localhost:8080", "");
        	return "redirect:" + uri;
        
        //model.addAttribute("newsToUpdate", newsToUpdate);
        
    }

    @GetMapping("/news/{id}/deletePhotos/{pId}")
    public String DeleteNews(@PathVariable("id") Long id, @PathVariable("pId") Long pId, Picture picture, News news, Model model) {
    	Optional<News> newsCurrent = newsRepository.findById(id);
        Optional<Picture> pictureToUpdate = pictureRepository.findById(pId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = userRepository.findByUsername(auth.getName());
        // if the user is publisher, check if it's his own news, to be able to edit it
        if ((us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_PUBLISHER")) && newsCurrent.get().getCreatedBy().equals(auth.getName()))
        		|| us.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN"))) {
        	if(pictureToUpdate.isPresent()){
                pictureRepository.deleteById(pictureToUpdate.get().getId());
            }
        }
        RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
        String uri = ((ServletRequestAttributes) ra).getRequest().getHeader("Referer");
        uri = uri.replace("http://localhost:8080", "");
        return "redirect:" + uri;
    }

    @PostMapping("/updateNews/{id}")
    @PreAuthorize("hasRole('ROLE_PUBLISHER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/updateNews/{id}")
	public String updateNews(@PathVariable("id") Long id,@ModelAttribute("newsToUpdate")  News news, BindingResult result, Model model) {
		 if (result.hasErrors()) {
            return "list-news";
        }
		Optional<News> newsData = newsRepository.findById(id);

        if (newsData.isPresent()) {
            News _news = newsData.get();
            _news.setTitle(news.getTitle() == null || news.getTitle().isEmpty()? _news.getTitle() : news.getTitle());
            _news.setText(news.getText() == null || news.getText().isEmpty() ? _news.getText() : news.getText());
            _news.setCreationDate(news.getCreationDate() == null ? _news.getCreationDate() : news.getCreationDate());
            _news.setValidFrom(news.getValidFrom() == null ? _news.getValidFrom() : news.getValidFrom());
            _news.setValidTo(news.getValidTo() == null ? _news.getValidTo() : news.getValidTo());
            _news.setIsPublished(news.getIsPublished() == null ? false : news.getIsPublished());
            _news.setReadStatus(news.getReadStatus() == null ? _news.getReadStatus() : news.getReadStatus());
            _news.setPicture(news.getPicture() == null ? _news.getPicture() : news.getPicture());
            newsRepository.save(_news);
        }


			return "redirect:/api-news/news/";
			
	}
  
    @DeleteMapping("/news/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
        try {
            newsRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/news")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {
        try {
            newsRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/news/published")
    public ResponseEntity<List<News>> findByPublished() {
        try {
            List<News> newsList = newsRepository.findNewsByIsPublished(true);

            if (newsList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(newsList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
