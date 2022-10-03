package com.project.gamification.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookRequestDto {

    private Long id;
    private String name;
    private List<ChapterRequest> chapters;


}
