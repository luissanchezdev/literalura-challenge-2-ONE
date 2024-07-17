package com.luissdev.literalura.main;

import com.luissdev.literalura.model.Author;
import com.luissdev.literalura.model.Book;
import com.luissdev.literalura.model.BookDataDTO;
import com.luissdev.literalura.model.ResultsDTO;
import com.luissdev.literalura.repository.AuthorRepository;
import com.luissdev.literalura.repository.BookRepository;
import com.luissdev.literalura.service.ConsumptionAPI;
import com.luissdev.literalura.service.ConvertData;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private Scanner inputUser = new Scanner(System.in);
    private ConvertData convertData = new ConvertData();
    private ConsumptionAPI consumptionApi = new ConsumptionAPI();
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    List<Book> books;
    List<Author> authors;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        final var menu = """
                Selecciona una opción:
                1. Buscar libro por titulo
                2. Lista de libros registrados
                3. Lista de autores registrados
                4. Lista de autores vivos en un año determinado
                5. Lista de libros por idioma
                0. Salir
                """;
        var option = -1;

        while (option != 0) {
            System.out.println(menu);
            System.out.print("Ingrese el número de la opción: ");
            option = inputUser.nextInt();
            inputUser.nextLine();
            switch (option) {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    listRegisteredBooks();
                    break;
                case 3:
                    listRegisteredAuthors();
                    break;
                case 4:
                    listAuthorsAliveInYear();
                    break;
                case 5:
                    listBooksByLanguage();
                    break;
                case 0:
                    System.out.println("Gracias por usar la busqueda de libros Challenge");
                    break;
                default:
                    System.out.println("Opción no válida, intenta de nuevo");
                    break;
            }
        }
    }

    private void searchBookByTitle() {
        System.out.print("Buscar libro por título: ");
        String inTitle = inputUser.nextLine();
        var json = consumptionApi.getData(inTitle.replace(" ", "%20"));
        //System.out.println("json: " + json);
        var data = convertData.getData(json, ResultsDTO.class);
        //System.out.println("data: " + data);
        if (data.results().isEmpty()) {
            System.out.println("Libro no encontrado");
        } else {
            BookDataDTO bookDataDTO = data.results().get(0);
            System.out.println(bookDataDTO);
            Book book = new Book(bookDataDTO);
            Author author = new Author().getFirstAuthor(bookDataDTO);
            saveData(book, author);
        }
    }

    private void saveData(Book book, Author author) {
        Optional<Book> bookFound = bookRepository.findByTitleContains(book.getTitle());
        //System.out.println("bookFound: " + bookFound);
        if (bookFound.isPresent()) {
            System.out.println("Este libro ya fue registrado");
        } else {
            try {
                bookRepository.save(book);
                System.out.println("Libro registrado");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        Optional<Author> authorFound = authorRepository.findByNameContains(author.getName());
        //System.out.println("authorFound: " + authorFound);
        if (authorFound.isPresent()) {
            System.out.println("Este autor ya ha sido registrado");
        } else {
            try {
                authorRepository.save(author);
                System.out.println("Autor registrado");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void listRegisteredBooks() {
        System.out.println("Lista de libros registrados");
        books = bookRepository.findAll();
        books.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .forEach(System.out::println);
    }

    private void listRegisteredAuthors() {
        System.out.println("Lista de autores registrados");
        authors = authorRepository.findAll();
        authors.stream()
                .sorted(Comparator.comparing(Author::getName))
                .forEach(System.out::println);
    }

    private void listAuthorsAliveInYear() {
        System.out.print("Lista de autores vivos en un año determinado: ");
        Integer year = Integer.valueOf(inputUser.nextLine());
        authors = authorRepository
                .findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
        if (authors.isEmpty()) {
            System.out.println("Autores vivos no encontrados");
        } else {
            authors.stream()
                    .sorted(Comparator.comparing(Author::getName))
                    .forEach(System.out::println);
        }
    }

    private void listBooksByLanguage() {
        System.out.println("Lista de libros por idioma");
        System.out.println("""
                Selecciona un idioma. Ingresa las dos primeras letras de la opción en minuscula:
                en - English
                es - Spanish
                fr - French
                pt - Portuguese
                """);
        String lang = inputUser.nextLine();
        books = bookRepository.findByLanguageContains(lang);
        if (books.isEmpty()) {
            System.out.println("La busqueda de libros por el idioma seleccionado no arrojó resultados");
        } else {
            books.stream()
                    .sorted(Comparator.comparing(Book::getTitle))
                    .forEach(System.out::println);
        }
    }
}
