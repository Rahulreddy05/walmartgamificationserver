package com.project.gamification.service;

import com.project.gamification.dto.BookGetResponseDto;
import com.project.gamification.dto.BookRequest;
import com.project.gamification.dto.BookResponse;

import java.time.LocalDateTime;

public interface IGameBookService {
    public BookResponse createBook(BookRequest bookRequest, LocalDateTime timestamp);

    BookGetResponseDto getBooks();

    BookResponse updateBook(BookRequest book, LocalDateTime timestamp);
}
