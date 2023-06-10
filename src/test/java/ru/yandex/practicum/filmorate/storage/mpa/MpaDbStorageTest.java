package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/DropForTest.sql", "/schema.sql", "/mpaDataTest.sql", "/filmDataTest.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void getById() {
        assertThat(mpaDbStorage.getById(1))
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");

    }

    @Test
    void getAll() {
        assertThat(mpaDbStorage.getAll()).hasSize(5);
    }

    @Test
    void getByFilmId() {
        assertThat(mpaDbStorage.getByFilmId(1))
                .hasFieldOrPropertyWithValue("id", 3)
                .hasFieldOrPropertyWithValue("name", "PG-13");
    }
}