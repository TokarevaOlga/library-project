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
public class AuthorDto {
    private Long id;
    private String name;
    private String surname;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BookDto> books;
}
