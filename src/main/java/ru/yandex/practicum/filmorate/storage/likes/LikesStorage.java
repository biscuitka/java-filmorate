package ru.yandex.practicum.filmorate.storage.likes;

import java.util.List;

public interface LikesStorage {
    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Long> getLikesFromUsers(long filmId);

    List<Long> getMostLikedFilms(int count);
}
