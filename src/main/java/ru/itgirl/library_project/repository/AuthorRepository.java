package ru.itgirl.library_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itgirl.library_project.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}