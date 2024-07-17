package com.luissdev.literalura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "authors")
@Data
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books;

    public Author() {
    }

    public Author(AuthorDataDTO authorDataDTO) {
        this.name = authorDataDTO.name();
        this.birthYear = Integer.valueOf(authorDataDTO.birthYear());
        this.deathYear = Integer.valueOf(authorDataDTO.deathYear());
    }

    public Author getFirstAuthor(BookDataDTO bookDataDTO) {
        AuthorDataDTO authorDataDTO = bookDataDTO.author().get(0);
        return new Author(authorDataDTO);
    }

    @Override
    public String toString() {
        return """
                INFORMACIÓN DE AUTOR:
                Nombre: %s,
                Año de nacimiento: %s,
                Año de muerte: %s
                """.formatted(name, birthYear, deathYear);
    }
}
