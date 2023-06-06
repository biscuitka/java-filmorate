package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    //@Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;
    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public void deleteFilmById(long id) {
        filmStorage.deleteFilmById(id);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(long id, long userId) {
        Film film = getFilmById(id);
        film.getLikes().add(userStorage.getUserById(userId).getId());
        likesStorage.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        Film film = getFilmById(id);
        if (!film.getLikes().contains(userId)) {
            log.info("Ошибка при удалении лайка от пользователя id {} у фильма {} ", userId, id);
            throw new NotFoundException("Пользователь с id " + userId + " не ставил лайк фильму " + id);
        }
        likesStorage.deleteLike(id, userId);
    }

    public List<Film> getMostLikedFilms(@Positive int count) {
        List<Film> films = new ArrayList<>();
        for (Long filmId : likesStorage.getMostLikedFilms(count)) {
            films.add(filmStorage.getFilmById(filmId));
        }
        return films;
    }

    public void addLikeInMemory(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        if (userStorage.getUserById(userId) != null) {
            film.getLikes().remove(userId);
            filmStorage.updateFilm(film);
        }
    }

    public void deleteLikeInMemory(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        if (userStorage.getUserById(userId) != null) {
            film.getLikes().remove(userId);
            filmStorage.updateFilm(film);
        }
    }

    public List<Film> getMostLikedFilmsInMemory(@Positive int count) {
        if (count == 0) {
            count = 10;
        }
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
