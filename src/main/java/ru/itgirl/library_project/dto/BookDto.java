package ru.itgirl.library_project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookDto {
    private Long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String genre;

    @JsonInclude(JsonInclude.Include.NON_NULL)
//чтобы при поиске книги по названию выводился только id, название, жанр (week 19)
    private List<AuthorDto> authors;
}
