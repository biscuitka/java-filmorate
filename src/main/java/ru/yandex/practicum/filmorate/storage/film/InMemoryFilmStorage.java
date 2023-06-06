package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.constraints.Positive;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    UserStorage userStorage;
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
    public void deleteFilmById(long id) {
        if (id != 0 && films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
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

    public void addLike(long id, long userId) {

        User user = userStorage.getUserById(userId);
        Film film = getFilmById(id);
        if (user != null) {
            film.getLikes().add(userId);
            updateFilm(film);
        }
    }

    public void deleteLike(long id, long userId) {
        Film film = getFilmById(id);
        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
            updateFilm(film);
        }
    }


    public List<Film> getMostLikedFilms(@Positive int count) {
        if (count == 0) {
            count = 10;
        }
        return getFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());

    }

}
