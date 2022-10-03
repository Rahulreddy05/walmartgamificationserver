package com.project.gamification.controller;

import com.project.gamification.dto.BookGetResponseDto;
import com.project.gamification.dto.BookRequest;
import com.project.gamification.dto.BookResponse;
import com.project.gamification.service.IGameBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins= "http://localhost:3005/")
public class GameBookController {

    Logger logger = LoggerFactory.getLogger(GameBookController.class);
    @Autowired
    IGameBookService gameService;

    @PostMapping("/v1/books")
    ResponseEntity<BookResponse> createBook(@RequestBody BookRequest book){
        LocalDateTime timestamp = LocalDateTime.now();
        BookResponse response = gameService.createBook(book,timestamp);
        if(null == response) {
            logger.error("Issue in creating book. Please try again");
            throw new RuntimeException("Issue in creating book. Please try again!");
        }
        else
            return new ResponseEntity<BookResponse>(response, HttpStatus.CREATED);
    }

    @GetMapping("/v1/books")
    ResponseEntity<BookGetResponseDto> getBooks(){
        BookGetResponseDto bookGetResponseDto = gameService.getBooks();
        return new ResponseEntity<BookGetResponseDto>(bookGetResponseDto,HttpStatus.OK);
    }


    @PutMapping("/v1/books")
    ResponseEntity<BookResponse> updateBook(@RequestBody BookRequest book){
        LocalDateTime timestamp = LocalDateTime.now();
        BookResponse response = gameService.updateBook(book, timestamp);
            if (null == response)
                throw new RuntimeException("Issue in creating book. Please try again!");
            else
                return new ResponseEntity<BookResponse>(response, HttpStatus.OK);

    }





}
