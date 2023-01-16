package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class HbmPriorityRepository implements PriorityRepository {

    private final CrudRepository cr;

    public static final String MODEL = "Priority";
    public static final String ID = "fID";
    public static final String NAME = "fName";
    public static final String POSITION = "fPosition";
    public static final String UPDATE_STATEMENT = String.format(
            "UPDATE %s SET name = :%s, position = :%s WHERE id = :%s",
            MODEL, NAME, POSITION, ID
    );

    public static final String DELETE_STATEMENT = String.format(
            "DELETE %s WHERE id = :%s",
            MODEL, ID
    );
    public static final String FIND_ALL_STATEMENT = String.format("from %s", MODEL);
    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT + " order by id";
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where id = :%s", ID);
    public static final String TRUNCATE_TABLE = String.format("DELETE FROM %s", MODEL);


    @Override
    public Optional<Priority> add(Priority priority) {
        int id = priority.getId();
        cr.run(session -> session.persist(priority));
        return id == priority.getId() ? Optional.empty() : Optional.of(priority);
    }

    @Override
    public Optional<Priority> findById(int id) {
        return cr.optional(
                FIND_BY_ID_STATEMENT, Priority.class,
                Map.of(ID, id)
        );
    }

    @Override
    public List<Priority> findAll() {
        return cr.query(FIND_ALL_ORDER_BY_ID_STATEMENT, Priority.class);
    }

    @Override
    public boolean update(Priority priority) {
        return  cr.query(
                UPDATE_STATEMENT,
                Map.of(
                        NAME, priority.getName(),
                        POSITION, priority.getPosition(),
                        ID, priority.getId()
                )
        );
    }

    @Override
    public Optional<Priority> delete(Priority priority) {
        return cr.query(
                DELETE_STATEMENT,
                Map.of(ID, priority.getId())
        )
                ? Optional.of(priority)
                : Optional.empty();
    }

    /**
     * Очищает таблицу от записей
     */
    public void truncateTable() {
        cr.run(TRUNCATE_TABLE, new HashMap<>());
    }

}
