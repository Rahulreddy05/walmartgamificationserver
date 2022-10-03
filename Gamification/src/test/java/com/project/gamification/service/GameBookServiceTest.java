package com.project.gamification.service;

import com.project.gamification.dto.*;
import com.project.gamification.repository.IGameBookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GameBookServiceTest {
    @Mock
    IGameBookRepository iGameBookRepo;

    @InjectMocks
    GameBookServiceImpl gameBookService;

    BookRequest bookRequest = null;
    LocalDateTime time = LocalDateTime.now();


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
        bookRequest = BookRequest.builder().books(books).build();
        Mockito.when(iGameBookRepo.findByName("book 1")).thenReturn(null);
        Mockito.when(iGameBookRepo.findByName("book 2")).thenReturn(null);
        BookResponse bookResponse = gameBookService.createBook(bookRequest,time);
        Assertions.assertEquals(bookResponse.getResponse(),"Wow. You typed 1400 words on your first submission");

    }
    @Test
    public void createBook_AlreadyExisting(){
        ChapterRequest cr = ChapterRequest.builder().name("ch11").wordCount(600).build();
        ChapterRequest cr1 = ChapterRequest.builder().name("ch12").wordCount(100).build();
        ChapterRequest cr2 = ChapterRequest.builder().name("ch21").wordCount(700).build();
        ChapterRequest cr3 = ChapterRequest.builder().name("ch22").wordCount(100).build();
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
        bookRequest = BookRequest.builder().books(books).build();
        Mockito.when(iGameBookRepo.findByName("book 1")).thenReturn(bookObject1());
        Mockito.when(iGameBookRepo.findByName("book 2")).thenReturn(bookObject2());
        BookResponse bookResponse = gameBookService.createBook(bookRequest,time.plusMinutes(2));
        Assertions.assertEquals(bookResponse.getResponse(),"Wow. You typed 100 words in 2 minutes\n" +
                "Average number of words between two timestamps : 50.0");

    }

    private Book bookObject1(){
      ChapterRecord cr = ChapterRecord.builder().wordcount(500).datetimestamp(time).id(3L).build();
       ChapterRecord cr1 = ChapterRecord.builder().wordcount(200).datetimestamp(time).id(4L).build();
       List<ChapterRecord> records = new ArrayList<>();
       records.add(cr);

       Chapter c = Chapter.builder().id(2L).name("ch11").dateTimestamp(time).records(records).build();
       List<ChapterRecord> records1 = new ArrayList<>();
        records1.add(cr1);
        Chapter c2 = Chapter.builder().id(5L).name("ch12").dateTimestamp(time).records(records1).build();
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(c);
        chapters.add(c2);
        Book book= Book.builder().id(1L).name("book 1").datetimestamp(time).chapters(chapters).build();
        return book;
    }

    private Book bookObject2(){
        ChapterRecord cr = ChapterRecord.builder().wordcount(500).datetimestamp(time).id(6L).build();
        ChapterRecord cr1 = ChapterRecord.builder().wordcount(200).datetimestamp(time).id(7L).build();
        List<ChapterRecord> records = new ArrayList<>();
        records.add(cr);
        Chapter c = Chapter.builder().id(8L).name("ch21").dateTimestamp(time).records(records).build();
        List<ChapterRecord> records1 = new ArrayList<>();
        records1.add(cr1);
        Chapter c2 = Chapter.builder().id(9L).name("ch22").dateTimestamp(time).records(records1).build();
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(c);
        chapters.add(c2);
        Book book= Book.builder().id(10L).name("book 2").datetimestamp(time).chapters(chapters).build();
        return book;
    }

}
