package ru.itgirl.library_project.service;

import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;

import java.util.List;

public interface BookService {
    BookDto getByNameV1(String name); //week19

    public BookDto getByNameV2(String name); //week19

    public BookDto getByNameV3(String name); //week19

    BookDto createBook(BookCreateDto bookCreateDto); //week20

    BookDto updateBook(BookUpdateDto bookUpdateDto); //week20

    void deleteBook(Long id); //week20

    public List<BookDto> getAllBooks(); //week21
}