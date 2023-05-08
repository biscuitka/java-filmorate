package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Добавление фильма: {}", film);
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Вывод всех фильмов: {}", filmStorage.getFilms().size());
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        log.info("Запрос фильма по id: {}", id);
        return filmStorage.getFilmById(id);
    }

    @DeleteMapping("/{id}")
    public Film deleteById(@PathVariable long id) {
        log.info("Удаление фильма по id: {}", id);
        return filmStorage.deleteFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Лайк фильму по id: {}, от пользователя id: {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Удалить лайк фильму по id: {}, от пользователя id: {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilm(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("Запрос {} самых популярных фильмов", count);
        return filmService.getMostLikedFilms(count);
    }
}
