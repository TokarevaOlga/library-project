package ru.itgirl.library_project.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itgirl.library_project.dto.AuthorCreateDto;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.dto.AuthorUpdateDto;
import ru.itgirl.library_project.model.Author;
import ru.itgirl.library_project.model.Book;
import ru.itgirl.library_project.repository.AuthorRepository;
import ru.itgirl.library_project.service.impl.AuthorServiceImpl;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void testGetAuthorById() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorById(id);

        verify(authorRepository).findById(id);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByIdNotFound() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorById(id));

        verify(authorRepository).findById(id);
    }

    @Test
    public void testGetByNameV1() {
        String name = "John";
        String surname = "Doe";
        Long id = 1L;
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        // Настройка мока для метода findAuthorByName
        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.of(author));
        // Вызов метода сервиса
        AuthorDto authorDto = authorService.getByNameV1(name);
        // Проверка взаимодействия с репозиторием
        verify(authorRepository).findAuthorByName(name);

        // Проверка значений в AuthorDto
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetByNameV1NotFound() {
        String name = "John";

        // Настройка мока для метода findAuthorByName, чтобы вернуть пустой Optional
        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.empty());
        // Проверка, что метод выбрасывает NoSuchElementException
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getByNameV1(name));
        // Проверка взаимодействия с репозиторием
        verify(authorRepository).findAuthorByName(name);
    }

    @Test
    public void testGetByNameV2() {
        String name = "John";
        String surname = "Doe";
        Long id = 1L;
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        when(authorRepository.findAuthorByNameBySql(name)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getByNameV2(name);

        verify(authorRepository).findAuthorByNameBySql(name);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetByNameV2NotFound() {
        String name = "John";
        when(authorRepository.findAuthorByNameBySql(name)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getByNameV2(name));

        verify(authorRepository).findAuthorByNameBySql(name);
    }

    @Test
    //тест для неуспешного создания не делала, тк мой метод сервиса для создания автора не предусматривал обработку исключений
    public void testCreateAuthor() {
        // Данные для создания автора
        AuthorCreateDto authorCreateDto = new AuthorCreateDto("John", "Doe");
        Long id = 1L;
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, authorCreateDto.getName(), authorCreateDto.getSurname(), books);

        // Настройка мока для метода save
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        // Вызов метода сервиса
        AuthorDto authorDto = authorService.createAuthor(authorCreateDto);
        // Проверка взаимодействия с репозиторием
        verify(authorRepository).save(any(Author.class));

        // Проверка значений в AuthorDto
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testUpdateAuthor() {
        // Данные для обновления автора
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(1L, "Джон", "Доу");
        Set<Book> books = new HashSet<>();
        Author existingAuthor = new Author(1L, "John", "Doe", books);

        // Настройка мока для метода findById
        when(authorRepository.findById(authorUpdateDto.getId())).thenReturn(Optional.of(existingAuthor));

        // Настройка мока для метода save
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        // Вызов метода сервиса
        AuthorDto authorDto = authorService.updateAuthor(authorUpdateDto);

        // Проверка взаимодействия с репозиторием
        verify(authorRepository).findById(authorUpdateDto.getId());
        verify(authorRepository).save(any(Author.class));

        // Проверка значений в AuthorDto
        Assertions.assertEquals(authorDto.getId(), existingAuthor.getId());
        Assertions.assertEquals(authorDto.getName(), authorUpdateDto.getName());
        Assertions.assertEquals(authorDto.getSurname(), authorUpdateDto.getSurname());
    }

    @Test
    public void testUpdateAuthorNotFound() {
        // Данные для обновления автора
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(1L, "Джон", "Доу");

        // Настройка мока для метода findById, чтобы вернуть пустой Optional
        when(authorRepository.findById(authorUpdateDto.getId())).thenReturn(Optional.empty());

        // Проверка, что метод выбрасывает NoSuchElementException
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.updateAuthor(authorUpdateDto));

        // Проверка взаимодействия с репозиторием
        verify(authorRepository).findById(authorUpdateDto.getId());
        verify(authorRepository, never()).save(any(Author.class)); // Убедитесь, что save не был вызван
    }

    @Test
    public void testDeleteAuthor() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        // Настройка мока для метода findById
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        // Вызов метода сервиса
        authorService.deleteAuthor(id);
        // Проверка взаимодействия с репозиторием
        verify(authorRepository).findById(id);
        verify(authorRepository).deleteById(id); // Проверка, что deleteById был вызван
    }

    @Test
    public void testDeleteAuthorNotFound() {
        Long id = 1L;
        // Настройка мока для метода findById, чтобы вернуть пустой Optional
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        // Проверка, что метод выбрасывает NoSuchElementException
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.deleteAuthor(id));
        // Проверка взаимодействия с репозиторием
        verify(authorRepository).findById(id);
        verify(authorRepository, never()).deleteById(id); // Убедитесь, что deleteById не был вызван
    }

    @Test
    public void testGetAllAuthors() {
        // Подготовка данных
        Author author1 = new Author(1L, "John", "Doe", new HashSet<>());
        Author author2 = new Author(2L, "Harry", "Potter", new HashSet<>());
        List<Author> authors = new ArrayList<>();
        authors.add(author1);
        authors.add(author2);
        // Настройка поведения мока
        when(authorRepository.findAll()).thenReturn(authors);
        // Вызов метода
        List<AuthorDto> authorDtos = authorService.getAllAuthors();
        // Проверка результатов
        verify(authorRepository).findAll();
        Assertions.assertEquals(2, authorDtos.size());
        Assertions.assertEquals(author1.getId(), authorDtos.get(0).getId());
        Assertions.assertEquals(author1.getName(), authorDtos.get(0).getName());
        Assertions.assertEquals(author1.getSurname(), authorDtos.get(0).getSurname());
        Assertions.assertEquals(author2.getId(), authorDtos.get(1).getId());
        Assertions.assertEquals(author2.getName(), authorDtos.get(1).getName());
        Assertions.assertEquals(author2.getSurname(), authorDtos.get(1).getSurname());
    }

    @Test
    public void testGetAllAuthorsNotFound() {
        // Настройка поведения мока
        when(authorRepository.findAll()).thenReturn(new ArrayList<>());
        // Вызов метода
        List<AuthorDto> authorDtos = authorService.getAllAuthors();
        // Проверка результатов
        verify(authorRepository).findAll();
        Assertions.assertTrue(authorDtos.isEmpty(), "Expected empty list of authors");
    }


}
