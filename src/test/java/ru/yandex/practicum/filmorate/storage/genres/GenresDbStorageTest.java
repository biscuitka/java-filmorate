package ru.yandex.practicum.filmorate.storage.genres;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/DropForTest.sql","/schema.sql", "/genreDataTest.sql", "/mpaDataTest.sql", "/filmDataTest.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GenresDbStorageTest {
    private final GenresDbStorage genresDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    void getById() {
        assertThat(genresDbStorage.getById(1))
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void getAll() {
        assertThat(genresDbStorage.getAll()).hasSize(6);
    }

    @Test
    void getGenresByFilm() {
        Film film = filmDbStorage.getFilmById(1);
        film.getGenres().add(genresDbStorage.getById(1));
        film.getGenres().add(genresDbStorage.getById(2));
        filmDbStorage.updateFilm(film);
        assertThat(genresDbStorage.getGenresByFilm(1)).hasSize(2);

    }
}