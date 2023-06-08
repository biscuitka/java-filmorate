package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

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
        User user = userStorage.getUserById(userId);
        likesStorage.addLike(film.getId(), user.getId());
    }

    public void deleteLike(long id, long userId) {
        Film film = getFilmById(id);
        if (!film.containsLike(userId)) {
            log.info("Ошибка при удалении лайка от пользователя id {} у фильма {} ", userId, id);
            throw new NotFoundException("Пользователь с id " + userId + " не ставил лайк фильму " + id);
        }
        likesStorage.deleteLike(id, userId);
    }

    public List<Film> getMostLikedFilms(@Positive int count) {
        return likesStorage.getMostLikedFilms(count).stream()
                .map(filmStorage::getFilmById)
                .collect(Collectors.toList());
    }
}
