package com.project.gamification.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class BookRequest {
    private String _id;
    private List<BookRequestDto> books;
}
