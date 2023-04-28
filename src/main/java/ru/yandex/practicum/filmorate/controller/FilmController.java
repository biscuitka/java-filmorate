package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 1;


    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Добавление фильма: {}", film);
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        if (film.getId() != 0 && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.info("Попытка обновления несуществующего фильма: {}", film);
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    @GetMapping
    public ArrayList<Film> getFilms() {
        log.info("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }
}
