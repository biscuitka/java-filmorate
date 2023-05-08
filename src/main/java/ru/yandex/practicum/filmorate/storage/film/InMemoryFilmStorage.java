package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public Film createFilm(Film film) {
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() != 0 && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    @Override
    public Film deleteFilmById(long id) {
        if (id != 0 && films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(long id) {
        if (id != 0 && films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }
}
