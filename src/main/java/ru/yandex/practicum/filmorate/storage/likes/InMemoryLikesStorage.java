package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryLikesStorage implements LikesStorage {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public void addLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        if (userStorage.getUserById(userId) != null) {
            film.getLikes().remove(userId);
            filmStorage.updateFilm(film);
        }
    }

    @Override
    public void deleteLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        if (userStorage.getUserById(userId) != null) {
            film.getLikes().remove(userId);
            filmStorage.updateFilm(film);
        }
    }

    @Override
    public List<Long> getLikesFromUsers(long filmId) {
        return new ArrayList<>(filmStorage.getFilmById(filmId).getLikes());
    }

    @Override
    public List<Long> getMostLikedFilms(int count) {
        if (count == 0) {
            count = 10;
        }
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .map(Film::getId)
                .collect(Collectors.toList());

    }


}
