package com.project.gamification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookGetResponseDto {
    public List<Book> books;
}
