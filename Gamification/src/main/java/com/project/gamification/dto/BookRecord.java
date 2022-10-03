package com.project.gamification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookRecord {

    private LocalDateTime lastTimestamp;
    private int wordCount;
}
