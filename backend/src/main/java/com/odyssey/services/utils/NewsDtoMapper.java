package com.odyssey.services.utils;

import com.odyssey.dtos.NewsDto;
import com.odyssey.models.News;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class NewsDtoMapper implements Function<News, NewsDto> {

    @Override
    public NewsDto apply(News news) {
        return new NewsDto(
                news.getId(),
                news.getTitle(),
                news.getDescription(),
                news.getPicture(),
                news.getAuthor().getId(),
                news.getAuthor().getUsername()
        );
    }
}
