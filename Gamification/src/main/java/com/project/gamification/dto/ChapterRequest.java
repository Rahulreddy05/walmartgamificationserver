package com.project.gamification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChapterRequest {
    private Long id;
    private String name;
    private int wordCount;
}
