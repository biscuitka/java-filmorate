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

import java.util.*;

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
        List<Film> films = filmStorage.getFilms();
        Map<Long, List<Long>> filmsLikes = likesStorage.getLikesByFilms(films);
        loadMapLikesToFilm(filmsLikes, films);
        return films;
    }

    private void loadMapLikesToFilm(Map<Long, List<Long>> filmsLikes, List<Film> films) {
        for (Film film : films) {
            List<Long> likes = filmsLikes.getOrDefault(film.getId(), new ArrayList<>());
            Set<Long> filmLikes = new HashSet<>(likes);
            film.setLikes(filmLikes);
        }
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        film.getLikes().addAll(likesStorage.getLikesFromUsers(film.getId()));
        return film;
    }

    public void addLike(long id, long userId) {
        Film film = getFilmById(id);
        User user = userStorage.getUserById(userId);
        if (!film.containsLike(user.getId())) {
            likesStorage.addLike(film.getId(), user.getId());
        }
    }

    public void deleteLike(long id, long userId) {
        Film film = getFilmById(id);
        if (!film.containsLike(userId)) {
            log.info("Ошибка при удалении лайка от пользователя id {} у фильма {} ", userId, id);
            throw new NotFoundException("Пользователь с id " + userId + " не ставил лайк фильму " + id);
        }
        likesStorage.deleteLike(id, userId);
    }
    public List<Film> getCommonFilms(long userId, long friendId){
        List<Film> films = filmStorage.getCommonFilms(userId,friendId);
        Map<Long, List<Long>> filmsLikes = likesStorage.getLikesByFilms(films);
        loadMapLikesToFilm(filmsLikes, films);
        return films;
    }

    public List<Film> getMostLikedFilms(int count) {
        List<Long> popularFilmId = likesStorage.getMostLikedFilms(count);
        List<Film> filmsByIds = filmStorage.getFilmsByIdList(popularFilmId);
        Map<Long, List<Long>> filmsLikes = likesStorage.getLikesByFilms(filmsByIds);
        loadMapLikesToFilm(filmsLikes, filmsByIds);

        return filmsByIds;
    }
}
