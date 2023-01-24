package ru.job4j.todo.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
class HbmCategoryRepositoryTest {

    private static HbmCategoryRepository store;

    @BeforeAll
    public static void initStore() {
        CrudRepository cr = new SimpleCrudRepository(new Main().sf());
        store = new HbmCategoryRepository(cr);
        store.truncateTable();
    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
    }


    @Test
    void add() {
        Category category = new Category();
        category.setName("cat1");

        store.add(category);

        Optional<Category> categoryDb = store.findById(category.getId());

        assertThat(category.getId()).isNotEqualTo(0);
        assertThat(categoryDb.isPresent()).isTrue();
        assertThat(categoryDb.get().getName()).isEqualTo(category.getName());

    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void whenAllPresent() {
        Category category = new Category();
        category.setName("cat1");
        store.add(category);
        Category category2 = new Category();
        category2.setName("cat2");
        store.add(category2);
        Category category3 = new Category();
        category3.setName("cat3");
        store.add(category3);
        Category category10 = new Category();
        category10.setId(10);
        category10.setName("cat10");

        List<Integer> ids = List.of(category.getId(), category3.getId());
        List<Category> categories = ids.stream().map(id -> {
            Category cat = new Category();
            cat.setId(id);
            return cat;
        }).toList();
        List<Category> rsltTrue = store.allPresent(ids);
        List<Category> rsltFalse = store.allPresent(List.of(category.getId(), category10.getId()));

        assertThat(rsltTrue).containsAll(categories);
        assertThat(rsltFalse).containsAll(List.of(category));

    }

}