package com.project.gamification.service;

import com.project.gamification.dto.*;
import com.project.gamification.repository.IGameBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class GameBookServiceImpl implements IGameBookService{

    Logger logger = LoggerFactory.getLogger(GameBookServiceImpl.class);
    @Autowired
    IGameBookRepository iGameBookRepo;

    @Override
    public BookResponse createBook(BookRequest bookRequest, final LocalDateTime currentTimestamp) {
        if(null != bookRequest){
            List<BookRequestDto> books = bookRequest.getBooks();
            Boolean isBookExistent = false;
            Map<String,BookRecord> map = new HashMap<>();
            for(BookRequestDto book : books){
               Book existingBook = iGameBookRepo.findByName(book.getName());
               if(null == existingBook){
                   logger.info("book does not exist creating new");
                   Book b= createBook(book, currentTimestamp);
                   iGameBookRepo.save(b);
                   map.put(book.getName(), formBookRecord(b,currentTimestamp));
               }
               else{
                   isBookExistent = true;
                   logger.info("book already exists, merging chapters");
                   map.put(existingBook.getName(),mergeBookData(existingBook,book,currentTimestamp));
                   iGameBookRepo.save(existingBook);
               }

            }
            return prepareResponse(isBookExistent, map,currentTimestamp);

        }
        return null;
    }

    @Override
    public BookGetResponseDto getBooks() {
       List<Book> books= (List<Book>) iGameBookRepo.findAll();
       BookGetResponseDto bookGetResponseDto = new BookGetResponseDto();
       bookGetResponseDto.setBooks(books);
       return bookGetResponseDto;
    }



    @Override
    public BookResponse updateBook(BookRequest book, LocalDateTime timestamp) {

      return createBook(book,timestamp);

    }

    private BookResponse prepareResponse(Boolean isBookExistent, Map<String, BookRecord> map,LocalDateTime currentTimestamp) {
        StringBuilder sb = new StringBuilder();
        BookResponse bookResponse = new BookResponse();
        Map<LocalDateTime,Long> countMap = map.values().stream().collect(Collectors.groupingBy(e->e.getLastTimestamp(),Collectors.counting()));
        if(countMap.size() == 1) {
            //all updations were done at same time
            int sum = map.values().stream().map(e -> e.getWordCount()).reduce(0, Integer::sum);
            LocalDateTime lastTime = new ArrayList<>(countMap.keySet()).get(0);
            sb.append(contentFormation(sum,lastTime,currentTimestamp));
            bookResponse.setResponse(sb.toString());
        }
        else{
            for(Map.Entry<String,BookRecord> entry: map.entrySet()){
                sb.append("Book : ");
                sb.append(entry.getKey());
                sb.append(" --- \n");
                sb.append(contentFormation(entry.getValue().getWordCount(), entry.getValue().getLastTimestamp(), currentTimestamp));
                sb.append("\n");
            }
            bookResponse.setResponse(sb.toString());
        }
        return bookResponse;
    }

    private String contentFormation(int sum, LocalDateTime lastTime, LocalDateTime currentTimestamp) {
        StringBuilder sb = new StringBuilder();
        if (lastTime.compareTo(currentTimestamp) == 0){
            sb.append("Wow. You typed ");
            sb.append(sum);
            sb.append( " words on your first submission");
        }
        else{
            sb.append("Wow. You typed ");
            sb.append(sum);
            sb.append (" words in ");
            sb.append(ChronoUnit.MINUTES.between(lastTime, currentTimestamp));
            sb.append(" minutes\n");
            sb.append("Average number of words between two timestamps : ");
            double avg = ChronoUnit.MINUTES.between(lastTime, currentTimestamp) == 0 ? 0 : sum/ChronoUnit.MINUTES.between(lastTime, currentTimestamp);
            sb.append(avg);
        }
        return sb.toString();
    }

    private BookRecord mergeBookData(Book existingBook, BookRequestDto book,LocalDateTime currentTimestamp) {
        LocalDateTime lastTimestamp = existingBook.getDatetimestamp().plusHours(0);
        existingBook.setDatetimestamp(currentTimestamp);
        List<Chapter> chapters= existingBook.getChapters();
        if(null == chapters)
            chapters = new ArrayList<>();
        List<String> existingChapters = chapters.stream().map(e->e.getName()).collect(Collectors.toList());

        Map<String, Integer> map = book.getChapters()
                .stream().collect(Collectors.toMap(e-> e.getName(), v->v.getWordCount()));

        List<ChapterRequest> nonExistingChapterRequest = book.getChapters().stream()
                .filter(e-> !existingChapters.contains(e.getName()))
                .collect(Collectors.toList());


       for(Chapter cr : chapters){
            if(map.containsKey(cr.getName())){
                cr.getRecords().add(ChapterRecord.builder().datetimestamp(currentTimestamp).chapter(cr).wordcount(map.get(cr.getName())).build());
            }
       }
       for(ChapterRequest chapterRequest : nonExistingChapterRequest){
           Chapter c = Chapter.builder().
                   name(chapterRequest.getName()).
                   dateTimestamp(currentTimestamp).build();

            ChapterRecord cr = ChapterRecord.builder().datetimestamp(currentTimestamp).chapter(c).wordcount(chapterRequest.getWordCount()).build();
            List<ChapterRecord> records = new ArrayList<>();
            records.add(cr);
            c.setRecords(records);
            chapters.add(c);
       }
       return formBookRecord(existingBook, lastTimestamp);
    }

    private BookRecord formBookRecord(Book b, LocalDateTime timestamp) {
        BookRecord br = new BookRecord();
        int newWords=0;
        for(Chapter c: b.getChapters()){
            int current = c.getRecords().get(c.getRecords().size()-1).getWordcount();
            int last = c.getRecords().size() > 1? c.getRecords().get(c.getRecords().size()-2).getWordcount() : 0;
            newWords += current < 0 ? last - Math.abs(current) : current -last;
        }
        br.setWordCount(newWords);
        br.setLastTimestamp(timestamp);
        return br;
    }

    private Book createBook(BookRequestDto book, LocalDateTime currentTimestamp){
        Book bookRequest = Book.builder()
                .datetimestamp(currentTimestamp)
                .name(book.getName())
               .build();
         bookRequest.setChapters(createChapters(book.getChapters(),currentTimestamp,bookRequest));

        return bookRequest;

    }

    private List<Chapter> createChapters(List<ChapterRequest> chapterRequest, LocalDateTime currentTimestamp, Book bookRequest){
        List<Chapter> chapters = new ArrayList<>();
        if(null != chapterRequest) {
            for (ChapterRequest cr : chapterRequest) {
                Chapter c = Chapter.builder()
                        .dateTimestamp(currentTimestamp)
                        .name(cr.getName())
                        .book(bookRequest)
                        .build();
                ChapterRecord chapterRecord = ChapterRecord.builder()
                        .datetimestamp(currentTimestamp)
                        .wordcount(cr.getWordCount())
                        .chapter(c)
                        .build();
                List<ChapterRecord> records = new ArrayList<>();
                records.add(chapterRecord);
                c.setRecords(records);
                chapters.add(c);
            }
        }
        return chapters;
    }
}
