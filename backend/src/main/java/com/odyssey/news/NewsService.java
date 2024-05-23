package com.odyssey.news;

import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.exception.UnprocessableEntityException;
import com.odyssey.fileService.FileService;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class NewsService {
    private final NewsDao newsDao;
    private final UserDao authorDao;
    private final CloudinaryService cloudinaryService;

    public NewsService(NewsDao newsDao, UserDao authorDao, CloudinaryService cloudinaryService) {
        this.newsDao = newsDao;
        this.authorDao = authorDao;
        this.cloudinaryService = cloudinaryService;
    }

    public List<News> getAllNews(){
        return newsDao.selectAllNews();

    }

    public News getNews(Integer id){
        return newsDao.selectNewsById(id).orElseThrow(()->
                new ResourceNotFoundException("news with id [%s] not found".formatted(id)));
    }

    public List<News>getNewsByAuthorId(Integer authorId){
        if(!authorDao.existsUserById(authorId)){
            throw new ResourceNotFoundException("author with id [%s] not found".formatted(authorId));
        }
        return newsDao.selectNewsByAuthorId(authorId);
    }

    public void addNews(NewsRegistrationDto dto) {
        if (newsDao.existsNewsByTitleAndAuthorId(dto.title(), dto.authorId())){
            throw new DuplicateResourceException("news already exists");
        }
        User author = authorDao.selectUserById(dto.authorId())
            .orElseThrow(()->
                new ResourceNotFoundException
                    ("user with id [%s] not found".formatted(dto.authorId())));

        File file = FileService.convertFile(dto.image());

        try {
            String url = cloudinaryService.uploadImage(file, "news");
            News news = new News (
                    dto.title(), dto.description(), url, author
            );
            newsDao.insertNews(news);
        } catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }
    }

    public void deleteNews(Integer id){
        if(newsDao.existsNewsById(id)){
            newsDao.deleteNewsById(id);
        }
        else {
            throw new ResourceNotFoundException("news with id [%s] not found".formatted(id));
        }
    }

    public void updateNews(Integer id, NewsUpdateDto dto) {
        News existingNews = getNews(id);

        if (newsDao.existsNewsByTitleAndAuthorId(dto.title(), existingNews.getAuthor().getId())){
            throw new DuplicateResourceException("news already exists");
        }

        boolean changes = false;

        if (dto.title() != null && !dto.title().equals(existingNews.getTitle())){
            existingNews.setTitle(dto.title());
            changes = true;
        }
        if (dto.description() != null && !dto.description().equals(existingNews.getDescription())){
            existingNews.setDescription(dto.description());
            changes = true;
        }
        if (!changes) {
            throw new RequestValidationException("no changes were found");
        }

        try {
            File file = FileService.convertFile(dto.file());
            String newUrl = cloudinaryService.uploadImage(file, "news");
            cloudinaryService.deleteImageByUrl(existingNews.getPicture());
            existingNews.setPicture(newUrl);
        } catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }

        newsDao.updateNews(existingNews);
    }
}