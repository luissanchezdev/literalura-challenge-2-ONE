package com.luissdev.literalura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String title;
    private String author;
    private String language;
    private Double downloads;

    public Book() {
    }

    public Book(BookDataDTO bookDataDTO) {
        this.title = bookDataDTO.title();
        this.author = getFirstAuthor(bookDataDTO).getName();
        this.language = getFirstLanguage(bookDataDTO);
        this.downloads = bookDataDTO.downloads();
    }

    public Author getFirstAuthor(BookDataDTO bookDataDTO) {
        AuthorDataDTO authorDataDTO = bookDataDTO.author().get(0);
        return new Author(authorDataDTO);
    }

    public String getFirstLanguage(BookDataDTO bookDataDTO) {
        return bookDataDTO.language().get(0);
    }

    @Override
    public String toString() {
        return """
                INFORMACION DE LIBRO
                Title:  %s
                Author:  %s
                Language:  %s
                Downloads:  %s
                """.formatted(title, author, language, downloads);
    }
}
