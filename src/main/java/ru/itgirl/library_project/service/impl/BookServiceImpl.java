package ru.itgirl.library_project.service.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;
import ru.itgirl.library_project.model.Book;
import ru.itgirl.library_project.model.Genre;
import ru.itgirl.library_project.repository.BookRepository;
import ru.itgirl.library_project.repository.GenreRepository;
import ru.itgirl.library_project.service.BookService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public BookDto getByNameV1(String name) {
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findBookByName(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name '{}' not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV2(String name) {
        log.info("Try to find book by name {} using SQL query", name);
        Optional<Book> book = bookRepository.findBookByNameBySql(name);

        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name '{}' not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV3(String name) {
        log.info("Try to find book by name using Specification: {}", name);
        Specification<Book> specification = Specification.where(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                return cb.equal(root.get("name"), name);
            }
        });
        Optional<Book> book = bookRepository.findOne(specification);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name '{}' not found using Specification", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto createBook(BookCreateDto bookCreateDto) {
        log.info("Try to create book using input data: {}", bookCreateDto.toString());
        Book book = bookRepository.save(convertDtoToEntity(bookCreateDto));
        BookDto bookDto = convertEntityToDto(book);
        log.info("New book created: {}", bookDto.toString());
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        log.info("Try to update book with id: {}", bookUpdateDto.getId());
        Optional<Book> bookOptional = bookRepository.findById(bookUpdateDto.getId()); // bookOptional - для хранения результата поиска книги

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get(); // Получаем объект Book, если он существует.
            Genre genre = genreRepository.findByName(bookUpdateDto.getGenre()); // Получаем жанр по имени
            book.setName(bookUpdateDto.getName());
            book.setGenre(genre);
            Book savedBook = bookRepository.save(book);
            BookDto bookDto = convertEntityToDto(savedBook);
            log.info("Book updated: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with id {} not found", bookUpdateDto.getId());
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public void deleteBook(Long id) {
        log.info("Try to delete book with id {}", id);
        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            bookRepository.deleteById(id);
            log.info("Book with id {} is deleted successfully", id);
        } else {
            log.error("Book with id {} not found. There is nothing to delete", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public List<BookDto> getAllBooks() {
        log.info("Try to get all books");
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            log.error("No books found");
        } else {
            log.info("Found {} books", books.size());
        }

        return books.stream()
                .map(this::convertEntityToDto) // делаем из листа сущностей лист DTO
                .collect(Collectors.toList());
    }
    
    private BookDto convertEntityToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .genre(book.getGenre().getName())
                .name(book.getName())
                .build();
    }

    private Book convertDtoToEntity(BookCreateDto bookCreateDto) {
        Genre genre = genreRepository.findByName(bookCreateDto.getGenre());

        return Book.builder()
                .name(bookCreateDto.getName())
                .genre(genre)
                .build();
    }
}