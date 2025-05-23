package ru.itgirl.library_project.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;
import ru.itgirl.library_project.model.Book;
import ru.itgirl.library_project.model.Genre;
import ru.itgirl.library_project.repository.BookRepository;
import ru.itgirl.library_project.repository.GenreRepository;
import ru.itgirl.library_project.service.impl.BookServiceImpl;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository; // Добавляем новый мок для genreRepository

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void testGetByNameV1() {
        String name = "JAVA from EPAM";
        Long id = 1L;
        Genre genre = new Genre(); // Создаем объект Genre
        genre.setId(1L); // Устанавливаем ID жанра
        genre.setName("Программирование"); // Устанавливаем имя жанра
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setGenre(genre); // Устанавливаем жанр
        book.setAuthors(new HashSet<>()); // Устанавливаем пустое множество авторов
        // Настройка поведения мока
        when(bookRepository.findBookByName(name)).thenReturn(Optional.of(book));
        // Вызов метода
        BookDto bookDto = bookService.getByNameV1(name);
        // Проверка результатов
        verify(bookRepository).findBookByName(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), genre.getName()); // Проверяем имя жанра
    }

    @Test
    public void testGetByNameV1NotFound() {
        String name = "JAVA Методы программирования";
        // Настройка поведения мока
        when(bookRepository.findBookByName(name)).thenReturn(Optional.empty());
        // Проверка, что выбрасывается исключение
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getByNameV1(name));
        // Проверка, что метод был вызван
        verify(bookRepository).findBookByName(name);
    }

    @Test
    public void testGetByNameV2() {
        String name = "JAVA from EPAM";
        Long id = 1L;
        Genre genre = new Genre(); // Создаем объект Genre
        genre.setId(1L); // Устанавливаем ID жанра
        genre.setName("Программирование"); // Устанавливаем имя жанра
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setGenre(genre); // Устанавливаем жанр
        book.setAuthors(new HashSet<>()); // Устанавливаем пустое множество авторов
        // Настройка поведения мока
        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.of(book));
        // Вызов метода
        BookDto bookDto = bookService.getByNameV2(name);
        // Проверка результатов
        verify(bookRepository).findBookByNameBySql(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), genre.getName()); // Проверяем имя жанра
    }

    @Test
    public void testGetByNameV2NotFound() {
        String name = "JAVA Методы программирования";
        // Настройка поведения мока
        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.empty());
        // Проверка, что выбрасывается исключение
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getByNameV2(name));
        // Проверка, что метод был вызван
        verify(bookRepository).findBookByNameBySql(name);
    }

    @Test
    //тест для неуспешного создания не делала, тк мой метод сервиса для создания книги не предусматривал обработку исключений
    public void testCreateBook() {
        // Данные для создания книги
        BookCreateDto bookCreateDto = new BookCreateDto("Философия Java", "Программирование");
        Long id = 1L;
        Genre genre = new Genre(); // Создаем объект Genre
        genre.setId(1L); // Устанавливаем ID жанра
        genre.setName("Программирование"); // Устанавливаем имя жанра
        Book book = new Book(id, bookCreateDto.getName(), genre, new HashSet<>()); // Создаем объект Book
        // Настройка мока для метода save
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        // Вызов метода сервиса
        BookDto bookDto = bookService.createBook(bookCreateDto);
        // Проверка взаимодействия с репозиторием
        verify(bookRepository).save(any(Book.class));
        // Проверка значений в BookDto
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), genre.getName()); // Проверяем имя жанра
    }

    @Test
    public void testUpdateBook() {
        // Данные для обновления книги
        Long id = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto(id, "Изучаем Java", "Программирование");

        Genre genre = new Genre(); // Создаем объект Genre
        genre.setId(1L); // Устанавливаем ID жанра
        genre.setName("Программирование"); // Устанавливаем имя жанра
        Book existingBook = new Book(id, "Learning Java", genre, new HashSet<>()); // Существующая книга
        Book updatedBook = new Book(id, bookUpdateDto.getName(), genre, new HashSet<>()); // Обновленная книга
        // Настройка мока для метода findById
        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(genreRepository.findByName(bookUpdateDto.getGenre())).thenReturn(genre);
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        // Вызов метода сервиса
        BookDto bookDto = bookService.updateBook(bookUpdateDto);
        // Проверка взаимодействия с репозиториями
        verify(bookRepository).findById(id);
        verify(genreRepository).findByName(bookUpdateDto.getGenre());
        verify(bookRepository).save(any(Book.class));
        // Проверка значений в BookDto
        Assertions.assertEquals(bookDto.getId(), updatedBook.getId());
        Assertions.assertEquals(bookDto.getName(), updatedBook.getName());
        Assertions.assertEquals(bookDto.getGenre(), genre.getName()); // Проверяем имя жанра
    }

    @Test
    public void testUpdateBookNotFound() {
        // Данные для обновления книги
        Long id = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto(id, "Java - руководство для начинающих", "Программирование");
        // Настройка мока для метода findById, чтобы вернуть пустой Optional
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        // Проверка, что выбрасывается исключение
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.updateBook(bookUpdateDto));
        // Проверка, что метод был вызван
        verify(bookRepository).findById(id);
    }

    @Test
    public void testDeleteBook() {
        Long id = 1L;
        String name = "Изучаем Java";
        Book book = new Book(id, name, null, new HashSet<>()); // Создаем объект книги
        // Настройка мока для метода findById
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        // Вызов метода сервиса
        bookService.deleteBook(id); // Используем bookService для вызова метода deleteBook
        // Проверка взаимодействия с репозиторием
        verify(bookRepository).findById(id); // Проверка, что findById был вызван
        verify(bookRepository).deleteById(id); // Проверка, что deleteById был вызван
    }

    @Test
    public void testDeleteBookNotFound() {
        Long id = 1L;
        // Настройка мока для метода findById, чтобы вернуть пустой Optional
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        // Проверка, что метод выбрасывает NoSuchElementException
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.deleteBook(id));
        // Проверка взаимодействия с репозиторием
        verify(bookRepository).findById(id); // Проверка, что findById был вызван
        verify(bookRepository, never()).deleteById(id); // Убедитесь, что deleteById не был вызван
    }

    @Test
    public void testGetAllBooks() {
        // Создаем объект жанра
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Программирование"); // Устанавливаем имя жанра
        // Создаем список книг с указанием жанра
        Long id1 = 1L;
        String name1 = "Философия Java";
        Book book1 = new Book();
        book1.setId(id1);
        book1.setName(name1);
        book1.setGenre(genre); // Устанавливаем жанр
        Long id2 = 2L;
        String name2 = "Java - руководство для начинающих";
        Book book2 = new Book();
        book2.setId(id2);
        book2.setName(name2);
        book2.setGenre(genre); // Устанавливаем жанр
        List<Book> books = Arrays.asList(book1, book2);
        // Настройка мока для метода findAll
        when(bookRepository.findAll()).thenReturn(books);
        // Вызов метода сервиса
        List<BookDto> bookDtos = bookService.getAllBooks(); // Используем bookService для вызова метода getAllBooks
        // Проверка взаимодействия с репозиторием
        verify(bookRepository).findAll(); // Проверка, что findAll был вызван
        // Проверка, что возвращаемый список не пустой и содержит правильные данные
        Assertions.assertNotNull(bookDtos);
        Assertions.assertEquals(2, bookDtos.size());
        Assertions.assertEquals(name1, bookDtos.get(0).getName());
        Assertions.assertEquals(genre.getName(), bookDtos.get(0).getGenre());
        Assertions.assertEquals(name2, bookDtos.get(1).getName());
        Assertions.assertEquals(genre.getName(), bookDtos.get(1).getGenre());
    }

    @Test
    public void testGetAllBooksNotFound() {
        // Настройка мока
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        // Вызов метода
        List<BookDto> bookDtos = bookService.getAllBooks();
        // Проверка результатов
        verify(bookRepository).findAll();
        Assertions.assertTrue(bookDtos.isEmpty(), "Expected empty list of books");
    }

}
