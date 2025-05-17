package ru.itgirl.library_project.service.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.AuthorCreateDto;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.dto.AuthorUpdateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.model.Author;
import ru.itgirl.library_project.repository.AuthorRepository;
import ru.itgirl.library_project.service.AuthorService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorDto getAuthorById(Long id) {
        log.info("Try to find author by id {}", id);
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with id {} not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto getByNameV1(String name) {
        log.info("Try to find author by name {}", name);
        Optional<Author> author = authorRepository.findAuthorByName(name);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto getByNameV2(String name) {
        log.info("Try to find author by name {} using SQL query", name);
        Optional<Author> author = authorRepository.findAuthorByNameBySql(name);

        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto getByNameV3(String name) {
        log.info("Try to find author by name using Specification: {}", name);
        Specification<Author> specification = Specification.where(new Specification<Author>() {
            @Override
            public Predicate toPredicate(Root<Author> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.get("name"), name);
            }
        });

        Optional<Author> author = authorRepository.findOne(specification);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with name {} not found using Specification", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto createAuthor(AuthorCreateDto authorCreateDto) {
        log.info("Try to create author using input data: {}", authorCreateDto.toString());
        Author author = authorRepository.save(convertDtoToEntity(authorCreateDto));
        AuthorDto authorDto = convertEntityToDto(author);
        log.info("New author created: {}", authorDto.toString());
        return authorDto;
    }

    @Override
    public AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto) {
        log.info("Try to update author with id: {}", authorUpdateDto.getId());
        Optional<Author> authorOptional = authorRepository.findById(authorUpdateDto.getId()); // authorOptional - для хранения результата поиска автора

        if (authorOptional.isPresent()) {
            Author author = authorOptional.get(); // Получаем объект Author, если он существует.
            // делаем так, тк на переменной типа Optional<Author> нельзя напрямую вызвать методы setName и setSurname
            author.setName(authorUpdateDto.getName());
            author.setSurname(authorUpdateDto.getSurname());
            Author savedAuthor = authorRepository.save(author);
            AuthorDto authorDto = convertEntityToDto(savedAuthor);
            log.info("Author updated: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with id {} not found", authorUpdateDto.getId());
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public void deleteAuthor(Long id) {
        log.info("Try to delete author with id {}", id);
        Optional<Author> author = authorRepository.findById(id);

        if (author.isPresent()) {
            authorRepository.deleteById(id);
            log.info("Author with id {} is deleted successfully", id);
        } else {
            log.error("Author with id {} not found. There is nothing to delete", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public List<AuthorDto> getAllAuthors() {
        log.info("Try to get all authors");
        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()) {
            log.error("No authors found");
        } else {
            log.info("Found {} authors", authors.size());
        }

        return authors.stream()
                .map(this::convertEntityToDto) //делаем из листа сущностей лист дто
                .collect(Collectors.toList());
    }

    private AuthorDto convertEntityToDto(Author author) { //convertEntityToDto
        List<BookDto> bookDtoList = null;
        if (author.getBooks() != null) {
            bookDtoList = author.getBooks()
                    .stream()
                    .map(book -> BookDto.builder()
                            .genre(book.getGenre().getName())
                            .name(book.getName())
                            .id(book.getId())
                            .build()
                    ).toList();
        }
        AuthorDto authorDto = AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .books(bookDtoList)
                .build();
        return authorDto;
    }

    private Author convertDtoToEntity(AuthorCreateDto authorCreateDto) {
        return Author.builder()
                .name(authorCreateDto.getName())
                .surname(authorCreateDto.getSurname())
                .build();
    }
}