package ru.itgirl.library_project.service;

import ru.itgirl.library_project.dto.BookDto;

public interface BookService {
    BookDto getByNameV1(String name);
    public BookDto getByNameV2(String name);
    public BookDto getByNameV3(String name);
}