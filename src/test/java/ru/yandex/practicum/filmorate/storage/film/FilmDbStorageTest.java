package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/DropForTest.sql", "/schema.sql", "/mpaDataTest.sql", "/filmDataTest.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Test
    void createFilm() {
        Film film3 = new Film();
        film3.setName("Around the World in 80 Days");
        film3.setDescription("Лондонский изобретатель Филеас Фогг докажет, что он не сумасшедший");
        film3.setReleaseDate(LocalDate.of(2004, 6, 13));
        film3.setDuration(120L);
        film3.setMpa(mpaDbStorage.getById(2));
        filmDbStorage.createFilm(film3);

        assertThat(filmDbStorage.getFilms()).hasSize(3);

        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.getFilmById(3));
        assertThat(filmOptional).isPresent().hasValueSatisfying(film -> assertThat(film)
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("name", "Around the World in 80 Days")
                .hasFieldOrPropertyWithValue("description", "Лондонский изобретатель Филеас Фогг " +
                        "докажет, что он не сумасшедший")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2004, 6, 13))
                .hasFieldOrPropertyWithValue("duration", 120L)
        );
        assertThat(filmDbStorage.getFilmById(3).getMpa())
                .hasFieldOrPropertyWithValue("id", mpaDbStorage.getById(2).getId())
                .hasFieldOrPropertyWithValue("name", mpaDbStorage.getById(2).getName());
    }

    @Test
    void updateFilm() {
        Film film1 = filmDbStorage.getFilmById(1);
        film1.setDescription("Сэм Шикаски - бойскаут, сирота, и Сьюзи Бишоп - " +
                "замкнутая двенадцатилетняя неуравновешенная девочка.");
        assertThat(filmDbStorage.updateFilm(film1))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("description", "Сэм Шикаски - бойскаут, " +
                        "сирота, и Сьюзи Бишоп - замкнутая двенадцатилетняя неуравновешенная девочка.");
    }

    @Test
    void deleteFilmById() {
        assertThat(filmDbStorage.getFilms()).hasSize(2);
        filmDbStorage.deleteFilmById(2);
        assertThat(filmDbStorage.getFilms()).hasSize(1);
    }

    @Test
    void getFilms() {
        assertThat(filmDbStorage.getFilms()).hasSize(2);
    }

    @Test
    void getFilmById() {
        assertThat(filmDbStorage.getFilmById(2))
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "Men in Black")
                .hasFieldOrPropertyWithValue("description", "Земля кишит пришельцами, " +
                        "за которыми глаз да глаз.")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1997, 6, 25))
                .hasFieldOrPropertyWithValue("duration", 98L);
        assertThat(filmDbStorage.getFilmById(2).getMpa())
                .hasFieldOrPropertyWithValue("id", mpaDbStorage.getById(3).getId())
                .hasFieldOrPropertyWithValue("name", mpaDbStorage.getById(3).getName());
    }

    @Test
    void getFilmsByIdList() {
        List<Long> ids = List.of(1L, 2L);
        List<Film> films = filmDbStorage.getFilmsByIdList(ids);
        assertThat(films).hasSize(2);
        assertThat(films.get(0).getId()).isEqualTo(1L);
        assertThat(films.get(0).getName()).isEqualTo("Moonrise Kingdom");
        assertThat(films.get(1).getId()).isEqualTo(2L);
        assertThat(films.get(1).getName()).isEqualTo("Men in Black");
    }
}