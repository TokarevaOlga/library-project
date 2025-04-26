package ru.itgirl.library_project.service;

import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;

public interface BookService {
    BookDto getByNameV1(String name);

    public BookDto getByNameV2(String name);

    public BookDto getByNameV3(String name);

    BookDto createBook(BookCreateDto bookCreateDto);

    BookDto updateBook(BookUpdateDto bookUpdateDto);

    void deleteBook(Long id);
}