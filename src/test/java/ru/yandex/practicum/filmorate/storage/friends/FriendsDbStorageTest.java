package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/friendDataTest.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FriendsDbStorageTest {
    private final FriendsDbStorage friendsDbStorage;

    @Test
    void addFriend() {
        friendsDbStorage.addFriend(1, 4);
        assertThat(friendsDbStorage.getAllFriends(1)).isEqualTo(List.of(2L, 3L, 4L));
    }

    @Test
    void deleteFriend() {
        assertThat(friendsDbStorage.getAllFriends(3)).isEqualTo(List.of(1L, 4L));
        friendsDbStorage.deleteFriend(3, 1);
        assertThat(friendsDbStorage.getAllFriends(3)).isEqualTo(List.of(4L));
    }

    @Test
    void getAllFriends() {
        assertThat(friendsDbStorage.getAllFriends(1)).isEqualTo(List.of(2L, 3L));
    }

    @Test
    void getCommonFriends() {
        List<Long> common = friendsDbStorage.getCommonFriends(1, 4);
        assertThat(common).isEqualTo(List.of(3L));
    }
}