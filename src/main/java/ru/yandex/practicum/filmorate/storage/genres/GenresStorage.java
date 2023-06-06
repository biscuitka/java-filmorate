package ru.yandex.practicum.filmorate.storage.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresStorage {
    Genre getById(int id);

    List<Genre> getAll();

    List<Integer> getGenresByFilm(long filmId);
}
