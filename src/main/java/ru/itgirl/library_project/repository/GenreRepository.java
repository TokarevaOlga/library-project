package ru.itgirl.library_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itgirl.library_project.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}
