package ru.itgirl.library_project.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.library_project.dto.AuthorCreateDto;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.dto.AuthorUpdateDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAuthorById() throws Exception {
        Long authorId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");

        mockMvc.perform(MockMvcRequestBuilders.get("/author/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByName() throws Exception {
        String authorName = "Александр";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName(authorName);
        authorDto.setSurname("Пушкин");

        mockMvc.perform(MockMvcRequestBuilders.get("/author")
                        .param("name", authorName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV2() throws Exception {
        String authorName = "Александр";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName(authorName);
        authorDto.setSurname("Пушкин");
        mockMvc.perform(MockMvcRequestBuilders.get("/author/v2")
                        .param("name", authorName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV3() throws Exception {
        String authorName = "Александр";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName(authorName);
        authorDto.setSurname("Пушкин");
        mockMvc.perform(MockMvcRequestBuilders.get("/author/v3")
                        .param("name", authorName))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        // Создаем объект AuthorCreateDto
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("Вячеслав");
        authorCreateDto.setSurname("Бутусов");
        // Создаем JSON-строку
        String authorCreateDtoJson = "{\"name\":\"" + authorCreateDto.getName() + "\",\"surname\":\"" + authorCreateDto.getSurname() + "\"}";
        // Выполняем POST-запрос
        mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(MediaType.APPLICATION_JSON) // Указываем тип содержимого
                        .content(authorCreateDtoJson))
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorCreateDto.getName())) // Проверяем имя
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorCreateDto.getSurname())); // Проверяем фамилию
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        // Создаем объект AuthorUpdateDto с использованием setters
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(15L); // Указываем ID автора
        authorUpdateDto.setName("Слава");
        authorUpdateDto.setSurname("Бутусов");
        // Создаем JSON-строку
        String authorUpdateDtoJson = "{\"id\":" + authorUpdateDto.getId() + ",\"name\":\"" + authorUpdateDto.getName() + "\",\"surname\":\"" + authorUpdateDto.getSurname() + "\"}";
        // Выполняем PUT-запрос
        mockMvc.perform(MockMvcRequestBuilders.put("/author/update")
                        .contentType(MediaType.APPLICATION_JSON) // Указываем тип содержимого
                        .content(authorUpdateDtoJson))
                .andExpect(status().isOk()) // Проверяем, что статус ответа 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorUpdateDto.getId())) // Проверяем ID
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorUpdateDto.getName())) // Проверяем имя
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorUpdateDto.getSurname())); // Проверяем фамилию
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        Long authorId = 15L;
        // Выполняем DELETE-запрос
        mockMvc.perform(MockMvcRequestBuilders.delete("/author/delete/{id}", authorId))
                .andExpect(status().isOk()); // Проверяем, что статус ответа 200 OK
    }

}
