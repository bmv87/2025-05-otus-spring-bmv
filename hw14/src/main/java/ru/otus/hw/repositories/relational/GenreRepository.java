package ru.otus.hw.repositories.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.entities.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query(value = "SELECT g FROM Genre g WHERE g.name IN :names")
    List<Genre> findAllByName(@Param("names") List<String> names);
}
