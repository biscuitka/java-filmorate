package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/DropForTest.sql", "/schema.sql", "/friendDataTest.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FriendsDbStorageTest {
    private final FriendsDbStorage friendsDbStorage;

    @Test
    void addFriend() {
        friendsDbStorage.addFriend(1, 4);
        List<User> friends = friendsDbStorage.getAllFriends(1);
        assertThat(friends.get(0).getId()).isEqualTo(2L);
        assertThat(friends.get(1).getId()).isEqualTo(3L);
        assertThat(friends.get(2).getId()).isEqualTo(4L);
    }

    @Test
    void deleteFriend() {
        List<User> friends = friendsDbStorage.getAllFriends(1);
        assertThat(friends.get(0).getId()).isEqualTo(2L);
        assertThat(friends.get(1).getId()).isEqualTo(3L);
        friendsDbStorage.deleteFriend(1, 2);
        assertThat(friendsDbStorage.getAllFriends(1).get(0).getId()).isEqualTo(3L);
    }

    @Test
    void getAllFriends() {
        List<User> friends = friendsDbStorage.getAllFriends(1);
        assertThat(friends.get(0).getId()).isEqualTo(2L);
        assertThat(friends.get(0).getName()).isEqualTo("Bill Murray");
        assertThat(friends.get(1).getId()).isEqualTo(3L);
        assertThat(friends.get(1).getName()).isEqualTo("Jack Choo");
    }

    @Test
    void getCommonFriends() {
        List<User> common = friendsDbStorage.getCommonFriends(1, 4);
        assertThat(common.get(0).getId()).isEqualTo(3L);
        assertThat(common.get(0).getName()).isEqualTo("Jack Choo");
        assertThat(common.get(0).getEmail()).isEqualTo("jj@ya.ru");
        assertThat(common).hasSize(1);
    }
}