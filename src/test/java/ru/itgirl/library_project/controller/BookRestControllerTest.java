package ru.itgirl.library_project.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetBookByName() throws Exception {
        String bookName = "Война и мир";
        BookDto bookDto = new BookDto();
        bookDto.setId(1L); //
        bookDto.setName(bookName);
        bookDto.setGenre("Роман"); //реальное имя жанра, соответствующее genre_id в базе
        // проверяем JSON с названием жанра, а не числовым id
        mockMvc.perform(MockMvcRequestBuilders.get("/book")
                        .param("name", bookName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testGetBookByNameV2() throws Exception {
        String bookName = "Война и мир";
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName(bookName);
        bookDto.setGenre("Роман");

        mockMvc.perform(MockMvcRequestBuilders.get("/book/v2")
                        .param("name", bookName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testGetBookByNameV3() throws Exception {
        String bookName = "Война и мир";
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName(bookName);
        bookDto.setGenre("Роман");

        mockMvc.perform(MockMvcRequestBuilders.get("/book/v3")
                        .param("name", bookName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testCreateBook() throws Exception {
        // Создаем объект BookCreateDto
        BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setName("Братья Карамазовы");
        bookCreateDto.setGenre("Роман");
        // Создаем JSON-строку
        String bookCreateDtoJson = "{ \"name\": \"" + bookCreateDto.getName() + "\", \"genre\": \"" + bookCreateDto.getGenre() + "\" }";
        // Выполняем POST-запрос
        mockMvc.perform(MockMvcRequestBuilders.post("/book/create")
                        .contentType(MediaType.APPLICATION_JSON) // Указываем тип содержимого
                        .content(bookCreateDtoJson)) // Передаем JSON-строку
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookCreateDto.getName())) // Проверяем имя
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookCreateDto.getGenre())); // Проверяем жанр
    }

    @Test
    public void testUpdateBook() throws Exception {
        // Создаем объект BookUpdateDto
        BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setId(12L); // Указываем ID книги
        bookUpdateDto.setName("Сёстры Карамазовы");
        bookUpdateDto.setGenre("Роман");
        // Создаем JSON-строку
        String bookUpdateDtoJson = "{\"id\":" + bookUpdateDto.getId() + ",\"name\":\"" + bookUpdateDto.getName() + "\",\"genre\":\"" + bookUpdateDto.getGenre() + "\"}";
        // Выполняем PUT-запрос
        mockMvc.perform(MockMvcRequestBuilders.put("/book/update")
                        .contentType(MediaType.APPLICATION_JSON) // Указываем тип содержимого
                        .content(bookUpdateDtoJson)) // Передаем JSON-строку
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookUpdateDto.getId())) // Проверяем ID
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookUpdateDto.getName())) // Проверяем имя
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookUpdateDto.getGenre())); // Проверяем жанр
    }

    @Test
    public void testDeleteBook() throws Exception {
        Long bookId = 12L; // Указываем ID книги
        // Выполняем DELETE-запрос
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/delete/{id}", bookId))
                .andExpect(status().isOk()); // Проверяем, что статус ответа 200 OK
    }

}
