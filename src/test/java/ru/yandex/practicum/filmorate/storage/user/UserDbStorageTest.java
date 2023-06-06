package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/userDataTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    void createUser() {
        User user3 = new User();
        user3.setName("Jim Carrey");
        user3.setLogin("StanleyIpkiss");
        user3.setBirthday(LocalDate.of(1962, 1, 17));
        user3.setEmail("сarrey@ya.ru");
        userDbStorage.createUser(user3);
        assertThat(userDbStorage.getAllUsers()).hasSize(3);

        Optional<User> userOptional = Optional.ofNullable(userDbStorage.getUserById(3));
        assertThat(userOptional).isPresent().hasValueSatisfying(user -> assertThat(user)
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("name", "Jim Carrey")
                .hasFieldOrPropertyWithValue("login", "StanleyIpkiss")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1962, 1, 17))
                .hasFieldOrPropertyWithValue("email", "сarrey@ya.ru")
        );
    }

    @Test
    void updateUser() {
        User user1 = userDbStorage.getUserById(1);
        user1.setLogin("Passepartout");
        userDbStorage.updateUser(user1);

        assertThat(userDbStorage.getUserById(1))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("login", "Passepartout");

    }

    @Test
    void deleteUserById() {
        assertThat(userDbStorage.getAllUsers()).hasSize(2);
        userDbStorage.deleteUserById(1);
        assertThat(userDbStorage.getAllUsers()).hasSize(1);
    }

    @Test
    void getAllUsers() {
        assertThat(userDbStorage.getAllUsers()).hasSize(2);
    }

    @Test
    void getUserById() {
        Optional<User> userOptional = Optional.ofNullable(userDbStorage.getUserById(2));
        assertThat(userOptional).isPresent().hasValueSatisfying(user -> assertThat(user)
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "Bill Murray")
        );
    }
}