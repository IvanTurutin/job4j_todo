package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.*;

/**
 * Репозиторий задач
 * @see ru.job4j.todo.model.Task
 */
@ThreadSafe
@Repository
@AllArgsConstructor
public class HbmTaskRepository implements TaskRepository {
    private final CrudRepository cr;

    public static final String MODEL = "Task";
    public static final String DONE = "fDone";
    public static final String ID = "fID";
    public static final String NAME = "fName";
    public static final String DESCRIPTION = "fDescription";
    public static final String PRIORITY = "fPriority";
    public static final String UPDATE_DONE_STATEMENT = String.format(
            "UPDATE %s SET done = :%s WHERE id = :%s",
            MODEL, DONE, ID
    );
    public static final String UPDATE_STATEMENT = String.format(
            "UPDATE %s SET name = :%s, description = :%s, done = :%s, priority = :%s WHERE id = :%s",
            MODEL, NAME, DESCRIPTION, DONE, PRIORITY, ID
    );

    public static final String DELETE_STATEMENT = String.format(
            "DELETE %s WHERE id = :%s",
            MODEL, ID
    );
    public static final String FIND_ALL_STATEMENT = String.format("from %s t JOIN FETCH t.priority", MODEL);
    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT + " order by t.id";
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where t.id = :%s", ID);
    public static final String FIND_BY_DONE_STATEMENT = FIND_ALL_STATEMENT + String.format(" where done = :%s", DONE);
    public static final String TRUNCATE_TABLE = String.format("DELETE FROM %s", MODEL);

    /**
     * Добавляет задачу в репозиторий и назначает ей id
     * @param task добавляемая задача
     * @return задачу обернутую в Optional если добавление задачи прошло успешно, и Optional.empty() если добавить
     * задачу не удалось
     */
    @Override
    public Optional<Task> add(Task task) {
        int id = task.getId();
        cr.run(session -> session.persist(task));
        return id == task.getId() ? Optional.empty() : Optional.of(task);
    }

    /**
     * Ищет задачу по идентификатору
     * @param id идентификатор задачи
     * @return задачу обернутую в Optional если задача найдена, и Optional.empty() если не найдена
     */
    @Override
    public Optional<Task> findById(int id) {
        return cr.optional(
                FIND_BY_ID_STATEMENT, Task.class,
                Map.of(ID, id)
        );
    }

    /**
     * Ищет все задачи
     * @return список всех найденных задач
     */
    @Override
    public List<Task> findAll() {
        return cr.query(FIND_ALL_ORDER_BY_ID_STATEMENT, Task.class);
    }

    /**
     * Изменяет задачу
     * @param task задача, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    @Override
    public boolean update(Task task) {

        return  cr.query(
                UPDATE_STATEMENT,
                Map.of(
                        NAME, task.getName(),
                        DESCRIPTION, task.getDescription(),
                        DONE, task.isDone(),
                        PRIORITY, task.getPriority(),
                        ID, task.getId()
                )
        );
    }

    /**
     * Удаляет задачу
     * @param task задача, которую необходимо удалить
     * @return задачу обернутую в Optional если задача удалена, и Optional.empty() если не удалена
     */
    @Override
    public Optional<Task> delete(Task task) {
        return cr.query(
                DELETE_STATEMENT,
                Map.of(ID, task.getId())
        )
                ? Optional.of(task)
                : Optional.empty();
    }

    /**
     * Обновляет статус задачи
     * @param task задача, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    @Override
    public boolean updateDone(Task task) {

        return cr.query(
                UPDATE_DONE_STATEMENT,
                Map.of(
                        DONE, task.isDone(),
                        ID, task.getId()
                )
        );
    }

    /**
     * Ищет задачи в которых поле done соответствует аргументу
     * @param isDone аргумент, по которому определяется какие задачи требуется искать true - выполненные,
     *               false - не выполненные
     * @return список задач соответствующих запросу
     */
    @Override
    public List<Task> findByDone(boolean isDone) {

        return cr.query(
                FIND_BY_DONE_STATEMENT,
                Task.class,
                Map.of(DONE, isDone)
        );
    }

    /**
     * Очищает таблицу от записей
     */
    public void truncateTable() {
        cr.run(TRUNCATE_TABLE, new HashMap<>());
    }

}
