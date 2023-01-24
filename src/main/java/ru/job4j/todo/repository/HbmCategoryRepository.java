package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class HbmCategoryRepository implements CategoryRepository {

    private final CrudRepository cr;

    public static final String MODEL = "Category";
    public static final String ID = "fID";
    public static final String NAME = "fName";
    public static final String PREDICATE = "fPredicate";
    public static final String UPDATE_STATEMENT = String.format(
            "UPDATE %s SET name = :%s WHERE id = :%s",
            MODEL, NAME, ID
    );

    public static final String DELETE_STATEMENT = String.format(
            "DELETE %s WHERE id = :%s",
            MODEL, ID
    );
    public static final String FIND_ALL_STATEMENT = String.format("from %s", MODEL);
    public static final String FIND_ALL_PRESENT_STATEMENT = String.format("from %s where id in :%s", MODEL, PREDICATE);
    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT + " order by id";
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where id = :%s", ID);
    public static final String TRUNCATE_TABLE = String.format("DELETE FROM %s", MODEL);

    @Override
    public Optional<Category> add(Category category) {
        int id = category.getId();
        cr.run(session -> session.persist(category));
        return id == category.getId() ? Optional.empty() : Optional.of(category);
    }

    @Override
    public Optional<Category> findById(int id) {
        return cr.optional(
                FIND_BY_ID_STATEMENT, Category.class,
                Map.of(ID, id)
        );
    }

    @Override
    public List<Category> findAll() {
        return cr.query(FIND_ALL_ORDER_BY_ID_STATEMENT, Category.class);
    }

    @Override
    public boolean update(Category category) {
        return  cr.query(
                UPDATE_STATEMENT,
                Map.of(
                        NAME, category.getName(),
                        ID, category.getId()
                )
        );
    }

    @Override
    public Optional<Category> delete(Category category) {
        return cr.query(
                DELETE_STATEMENT,
                Map.of(ID, category.getId())
        )
                ? Optional.of(category)
                : Optional.empty();
    }

    /**
     * Очищает таблицу от записей
     */
    public void truncateTable() {
        cr.run(TRUNCATE_TABLE, new HashMap<>());
    }

    @Override
    public List<Category> allPresent(List<Integer> ids) {
        return cr.query(
                FIND_ALL_PRESENT_STATEMENT,
                Category.class,
                Map.of(PREDICATE, ids)
        );
    }

}
