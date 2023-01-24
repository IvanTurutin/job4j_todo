package ru.job4j.todo.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/*@SpringBootTest*/
class HbmTaskRepositoryTest {

    private static HbmTaskRepository store;
    private static User user;
    private static Priority priority;
    private static List<Category> categories;

    @BeforeAll
    public static void initStore() {
        CrudRepository cr = new SimpleCrudRepository(new Main().sf());
        store = new HbmTaskRepository(cr);
        store.truncateTable();

        HbmUserRepository userStore = new HbmUserRepository(cr);
        userStore.truncateTable();
        user = new User();
        user.setId(0);
        user.setName("user1");
        user.setLogin("login1");
        user.setPassword("pass1");
        userStore.add(user);

        HbmPriorityRepository priorityRepository = new HbmPriorityRepository(cr);
        priorityRepository.truncateTable();
        priority = new Priority();
        priority.setName("normal");
        priority.setPosition(2);
        priorityRepository.add(priority);

        HbmCategoryRepository categoryRepository = new HbmCategoryRepository(cr);
        categoryRepository.truncateTable();
        categories = new ArrayList<>();
        Category category = new Category();
        category.setName("category1");
        Category category2 = new Category();
        category2.setName("category2");
        categories.add(category);
        categories.add(category2);
        categoryRepository.add(category);
        categoryRepository.add(category2);

    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
    }

    @Test
    void add() {
        Task task = new Task();
        task.setId(0);
        task.setName("Task1");
        task.setDescription("Task1 description");
        task.setCreated(LocalDateTime.now().withNano(0));
        task.setDone(false);
        task.setUser(user);
        task.setPriority(priority);
        task.setCategories(categories);

        store.add(task);
        Optional<Task> taskInDB = store.findById(task.getId());
        assertThat(task.getId()).isNotEqualTo(0);
        assertThat(taskInDB.isPresent()).isTrue();
        assertThat(taskInDB.get().getName()).isEqualTo(task.getName());
        assertThat(taskInDB.get().getDescription()).isEqualTo(task.getDescription());
        assertThat(taskInDB.get().getCreated()).isEqualTo(task.getCreated());
        assertThat(taskInDB.get().isDone()).isEqualTo(task.isDone());
    }

    @Test
    void findAll() {
        Task task = new Task();
        task.setId(0);
        task.setName("Task1");
        task.setDescription("Task1 description");
        task.setCreated(LocalDateTime.now().withNano(0));
        task.setDone(false);
        task.setUser(user);
        task.setPriority(priority);
        task.setCategories(categories);

        Task task2 = new Task();
        task2.setId(0);
        task2.setName("Task2");
        task2.setDescription("Task2 description");
        task2.setCreated(LocalDateTime.now().withNano(0));
        task2.setDone(true);
        task2.setUser(user);
        task2.setPriority(priority);
        task2.setCategories(categories);

        store.add(task);
        store.add(task2);
        List<Task> tasks = store.findAll();
        tasks.stream().forEach(System.out::println);
        assertThat(tasks).isNotEmpty().hasSize(2).contains(task, task2);

    }

    @Test
    void update() {
        Task task = new Task();
        task.setId(0);
        task.setName("Task1");
        task.setDescription("Task1 description");
        task.setCreated(LocalDateTime.now().withNano(0));
        task.setDone(false);
        task.setUser(user);
        task.setPriority(priority);
        task.setCategories(categories);

        store.add(task);
        task.setDescription("new Task1 description");
        store.update(task);

        Optional<Task> taskInDB = store.findById(task.getId());
        assertThat(task.getId()).isNotEqualTo(0);
        assertThat(taskInDB.isPresent()).isTrue();
        assertThat(taskInDB.get().getName()).isEqualTo(task.getName());
        assertThat(taskInDB.get().getDescription()).isEqualTo(task.getDescription());
        assertThat(taskInDB.get().getCreated()).isEqualTo(task.getCreated());
        assertThat(taskInDB.get().isDone()).isEqualTo(task.isDone());
    }

    @Test
    void delete() {
        Task task = new Task();
        task.setId(0);
        task.setName("Task1");
        task.setDescription("Task1 description");
        task.setCreated(LocalDateTime.now().withNano(0));
        task.setDone(false);
        task.setUser(user);
        task.setPriority(priority);
        task.setCategories(categories);

        store.add(task);
        store.delete(task);

        List<Task> tasks = store.findAll();
        assertThat(tasks).isEmpty();
    }

    @Test
    void updateDone() {
        Task task = new Task();
        task.setId(0);
        task.setName("Task1");
        task.setDescription("Task1 description");
        task.setCreated(LocalDateTime.now().withNano(0));
        task.setDone(false);
        task.setUser(user);
        task.setPriority(priority);
        task.setCategories(categories);

        store.add(task);
        task.setDone(true);
        store.updateDone(task);
        Optional<Task> taskInDB = store.findById(task.getId());

        assertThat(taskInDB.isPresent()).isTrue();
        assertThat(taskInDB.get().isDone()).isEqualTo(task.isDone());
    }

    @Test
    void findByDone() {
        Task task = new Task();
        task.setId(0);
        task.setName("Task1");
        task.setDescription("Task1 description");
        task.setCreated(LocalDateTime.now().withNano(0));
        task.setDone(false);
        task.setUser(user);
        task.setPriority(priority);
        task.setCategories(categories);

        Task task2 = new Task();
        task2.setId(0);
        task2.setName("Task2");
        task2.setDescription("Task2 description");
        task2.setCreated(LocalDateTime.now().withNano(0));
        task2.setDone(true);
        task2.setUser(user);
        task2.setPriority(priority);
        task2.setCategories(categories);

        store.add(task);
        store.add(task2);
        List<Task> tasksIsDone = store.findByDone(true);
        List<Task> tasksIsNotDone = store.findByDone(false);

        assertThat(tasksIsDone).isNotEmpty().hasSize(1).contains(task2);
        assertThat(tasksIsNotDone).isNotEmpty().hasSize(1).contains(task);
    }
}