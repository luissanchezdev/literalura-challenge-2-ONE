package com.luissdev.literalura.repository;

import com.luissdev.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleContains(String title);
    List<Book> findByLanguageContains(String language);
}
