package ru.itgirl.library_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookUpdateDto {
    private Long id;
    @Size(min = 1, max = 50)
    @NotBlank(message = "Необходимо указать название книги")
    private String name;
    @NotBlank(message = "Необходимо указать жанр")
    private String genre;
}
