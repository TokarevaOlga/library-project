package ru.itgirl.library_project.service;

import ru.itgirl.library_project.dto.AuthorCreateDto;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.dto.AuthorUpdateDto;
import ru.itgirl.library_project.dto.BookDto;

import java.util.List;

public interface AuthorService {
    AuthorDto getAuthorById(Long id); //week18

    AuthorDto getByNameV1(String name); //week19

    public AuthorDto getByNameV2(String name); //week19

    public AuthorDto getByNameV3(String name); //week19

    AuthorDto createAuthor(AuthorCreateDto authorCreateDto); //week20

    AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto); //week20

    void deleteAuthor(Long id); //week20

    public List<AuthorDto> getAllAuthors(); //week21
}
