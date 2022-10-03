package com.project.gamification.repository;

import com.project.gamification.dto.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IGameBookRepository extends CrudRepository<Book,Long> {

    Book findByName(String name);
}
