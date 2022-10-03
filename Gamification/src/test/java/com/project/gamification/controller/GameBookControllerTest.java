package com.project.gamification.controller;

import com.project.gamification.dto.BookRequest;
import com.project.gamification.dto.BookRequestDto;
import com.project.gamification.dto.BookResponse;
import com.project.gamification.dto.ChapterRequest;
import com.project.gamification.service.GameBookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GameBookControllerTest {

    @InjectMocks
    GameBookController gameBookController;

    @Mock
    GameBookServiceImpl gameBookService;


    @Test
    public void createBook(){
        ChapterRequest cr = ChapterRequest.builder().name("ch11").wordCount(500).build();
        ChapterRequest cr1 = ChapterRequest.builder().name("ch12").wordCount(200).build();
        ChapterRequest cr2 = ChapterRequest.builder().name("ch21").wordCount(500).build();
        ChapterRequest cr3 = ChapterRequest.builder().name("ch22").wordCount(200).build();
        List<ChapterRequest> chapters1 = new ArrayList<>();
        chapters1.add(cr);
        chapters1.add(cr1);
        List<ChapterRequest> chapters2 = new ArrayList<>();
        chapters2.add(cr2);
        chapters2.add(cr3);
        BookRequestDto br = BookRequestDto.builder().name("book 1").chapters(chapters1).build();
        BookRequestDto br1 = BookRequestDto.builder().name("book 2").chapters(chapters2).build();
        List<BookRequestDto> books = new ArrayList<>();
        books.add(br);
        books.add(br1);
        BookRequest req = BookRequest.builder().books(books).build();
         BookResponse bookResponse = new BookResponse();
        bookResponse.setResponse("Wow. You typed 1400 words on your first submission");
        Mockito.when(gameBookService.createBook(Mockito.any(),Mockito.any(LocalDateTime.class))).thenReturn(bookResponse);
        ResponseEntity<BookResponse> response = gameBookController.createBook(req);
        Assertions.assertEquals(response.getBody().getResponse(),"Wow. You typed 1400 words on your first submission");
    }

}
