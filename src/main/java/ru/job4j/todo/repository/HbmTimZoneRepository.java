package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.TimeZone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class HbmTimZoneRepository implements TimeZoneRepository {

    private final CrudRepository cr;

    public static final String MODEL = "TimeZone";
    public static final String ID = "fID";

    public static final String DELETE_STATEMENT = String.format(
            "DELETE %s WHERE id = :%s",
            MODEL, ID
    );
    public static final String FIND_ALL_STATEMENT = String.format("from %s", MODEL);
    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT + " order by id";
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where id = :%s", ID);
    public static final String TRUNCATE_TABLE = String.format("DELETE FROM %s", MODEL);

    @Override
    public Optional<TimeZone> add(TimeZone timeZone) {
        return cr.run(session -> session.persist(timeZone)) ? Optional.of(timeZone) : Optional.empty();
    }

    @Override
    public Optional<TimeZone> findById(int id) {
        return cr.optional(
                FIND_BY_ID_STATEMENT, TimeZone.class,
                Map.of(ID, id)
        );
    }

    @Override
    public List<TimeZone> findAll() {
        return cr.query(FIND_ALL_ORDER_BY_ID_STATEMENT, TimeZone.class);
    }

    @Override
    public boolean update(TimeZone timeZone) {
        return cr.run(session -> session.merge(timeZone));
    }

    @Override
    public Optional<TimeZone> delete(TimeZone timeZone) {
        return cr.query(
                DELETE_STATEMENT,
                Map.of(ID, timeZone.getId())
        )
                ? Optional.of(timeZone)
                : Optional.empty();
    }

    /**
     * Очищает таблицу от записей
     */
    public void truncateTable() {
        cr.run(TRUNCATE_TABLE, new HashMap<>());
    }

}
