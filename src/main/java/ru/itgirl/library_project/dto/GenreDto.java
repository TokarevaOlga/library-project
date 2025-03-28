package ru.itgirl.library_project.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GenreDto {
    private Long id;
    private String name;

    private List<BookDto> books;
}
