package ru.job4j.todo.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HbmUserRepositoryTest {

    private static HbmUserRepository store;

    @BeforeAll
    public static void initStore() {
        CrudRepository cr = new SimpleCrudRepository(new Main().sf());
        HbmTaskRepository taskStore = new HbmTaskRepository(cr);
        taskStore.truncateTable();

        store = new HbmUserRepository(cr);
        store.truncateTable();
    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
    }

    @Test
    void whenAdd() {
        User user = new User();
        user.setId(0);
        user.setName("user1");
        user.setLogin("login1");
        user.setPassword("pass1");

        store.add(user);
        Optional<User> userInDB = store.findById(user.getId());
        assertThat(user.getId()).isNotEqualTo(0);
        assertThat(userInDB.isPresent()).isTrue();
        assertThat(userInDB.get().getName()).isEqualTo(user.getName());
        assertThat(userInDB.get().getLogin()).isEqualTo(user.getLogin());
        assertThat(userInDB.get().getPassword()).isEqualTo(user.getPassword());

    }

    @Test
    void whenDelete() {
        User user = new User();
        user.setId(0);
        user.setName("user1");
        user.setLogin("login1");
        user.setPassword("pass1");

        store.add(user);
        store.delete(user.getId());

        List<User> users = store.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    void whenUpdate() {
        User user = new User();
        user.setId(0);
        user.setName("user1");
        user.setLogin("login1");
        user.setPassword("pass1");

        store.add(user);
        user.setName("new name");
        store.update(user);

        Optional<User> userInDB = store.findById(user.getId());
        assertThat(user.getId()).isNotEqualTo(0);
        assertThat(userInDB.isPresent()).isTrue();
        assertThat(userInDB.get().getName()).isEqualTo(user.getName());
        assertThat(userInDB.get().getLogin()).isEqualTo(user.getLogin());
        assertThat(userInDB.get().getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void whenFindByLoginAndPassword() {
        User user = new User();
        user.setId(0);
        user.setName("user1");
        user.setLogin("login1");
        user.setPassword("pass1");

        store.add(user);
        Optional<User> userInDB = store.findByLoginAndPassword(user.getLogin(), user.getPassword());
        assertThat(user.getId()).isNotEqualTo(0);
        assertThat(userInDB.isPresent()).isTrue();
        assertThat(userInDB.get().getName()).isEqualTo(user.getName());

    }

    @Test
    void whenFindAll() {
        User user = new User();
        user.setId(0);
        user.setName("user1");
        user.setLogin("login1");
        user.setPassword("pass1");
        User user2 = new User();
        user2.setId(0);
        user2.setName("user2");
        user2.setLogin("login2");
        user2.setPassword("pass2");

        store.add(user);
        store.add(user2);

        List<User> users = store.findAll();
        assertThat(users).isNotEmpty().hasSize(2).contains(user, user);

    }

    @Test
    void whenDuplicateLogin() {
        User user = new User();
        user.setId(0);
        user.setName("user1");
        user.setLogin("login1");
        user.setPassword("pass1");
        User user2 = new User();
        user2.setId(0);
        user2.setName("user2");
        user2.setLogin("login1");
        user2.setPassword("pass2");

        store.add(user);
        Optional<User> user2InDB = store.add(user2);
        store.findAll().forEach(System.out::println);
        assertThat(user2InDB.isEmpty()).isTrue();
    }

}