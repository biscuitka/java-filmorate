package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.constraints.Positive;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        if (userStorage.getUserById(userId) != null) {
            film.getLikes().add(userId);
            filmStorage.updateFilm(film);
        }
    }

    public void deleteLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        if (userStorage.getUserById(userId) != null) {
            film.getLikes().remove(userId);
            filmStorage.updateFilm(film);
        }
    }

    public List<Film> getMostLikedFilms(@Positive int count) {
        if (count == 0) {
            count = 10;
        }
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
