package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/DropForTest.sql", "/schema.sql", "/likeDataTest.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LikesDbStorageTest {
    private final LikesDbStorage likesDbStorage;
    private final FilmDbStorage filmDbStorage;


    @Test
    void addLike() {
        likesDbStorage.addLike(1, 2);
        List<Long> likes = likesDbStorage.getLikesFromUsers(1);
        assertThat(likes).isEqualTo(List.of(1L, 2L));
    }

    @Test
    void deleteLike() {
        assertThat(likesDbStorage.getLikesFromUsers(2)).isEqualTo(List.of(1L, 2L));
        likesDbStorage.deleteLike(2, 1);
        likesDbStorage.deleteLike(2, 2);
        assertThat(likesDbStorage.getLikesFromUsers(2)).isEmpty();


    }

    @Test
    void getLikesFromUsers() {
        assertThat(likesDbStorage.getLikesFromUsers(2)).isEqualTo(List.of(1L, 2L));
        assertThat(likesDbStorage.getLikesFromUsers(1)).isEqualTo(List.of(1L));
    }

    @Test
    void getMostLikedFilms() {
        assertThat(likesDbStorage.getMostLikedFilms(2)).isEqualTo(List.of(2L, 1L));
    }

    @Test
    void getLikesByFilms() {
        List<Film> films = filmDbStorage.getFilms();
        Map<Long, List<Long>> likesId = likesDbStorage.getLikesByFilms(films);
        assertThat(likesId).hasSize(2);
        assertThat(likesId.get(1L)).isEqualTo(List.of(1L));
        assertThat(likesId.get(2L)).isEqualTo(List.of(1L, 2L));
    }
}