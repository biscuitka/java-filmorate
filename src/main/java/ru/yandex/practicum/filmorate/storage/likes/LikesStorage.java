package ru.yandex.practicum.filmorate.storage.likes;

import javax.validation.constraints.Positive;
import java.util.List;

public interface LikesStorage {
    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Long> getLikesFromUsers(long filmId);

    List<Long> getMostLikedFilms(@Positive int count);
}
