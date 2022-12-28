package ru.job4j.todo.repository;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий задач
 * @see ru.job4j.todo.model.Task
 */
@ThreadSafe
//@AllArgsConstructor
@Repository
public class HbmTaskRepository implements TaskRepository {

    private final SessionFactory sf;

    private static final Logger LOG = LoggerFactory.getLogger(HbmTaskRepository.class.getName());
    private static final String LOG_MESSAGE = "Exception in UserRepository";

    public static final String TASK_MODEL = "Task";
    public static final String NAME = "fName";
    public static final String DESCRIPTION = "fDescription";
    public static final String DONE = "fDone";
    public static final String ID = "fID";
    public static final String UPDATE_STATEMENT = String.format(
            "UPDATE %s SET name = :%s, description = :%s, done = :%s WHERE id = :%s",
            TASK_MODEL, NAME, DESCRIPTION, DONE, ID
    );
    public static final String UPDATE_DONE_STATEMENT = String.format(
            "UPDATE %s SET done = :%s WHERE id = :%s",
            TASK_MODEL, DONE, ID
    );

    public static final String DELETE_STATEMENT = String.format(
            "DELETE %s WHERE id = :%s",
            TASK_MODEL, ID
    );
    public static final String FIND_ALL_STATEMENT = String.format("from %s", TASK_MODEL);
    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT + " order by id";
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where id = :%s", ID);
    public static final String FIND_BY_DONE_STATEMENT = FIND_ALL_STATEMENT + String.format(" where done = :%s", DONE);

    public static final String TRUNCATE_TABLE = "DELETE FROM tasks";

    public HbmTaskRepository(SessionFactory sf) {
        this.sf = sf;
    }

    /**
     * Добавляет задачу в репозиторий и назначает ей id
     * @param task добавляемая задача
     * @return задачу обернутую в Optional если добавление задачи прошло успешно, и Optional.empty() если добавить
     * задачу не удалось
     */
    @Override
    public Optional<Task> add(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
            return Optional.of(task);
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    /**
     * Ищет задачу по идентификатору
     * @param id идентификатор задачи
     * @return задачу обернутую в Optional если задача найдена, и Optional.empty() если не найдена
     */
    @Override
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(FIND_BY_ID_STATEMENT, Task.class);
            query.setParameter(ID, id);
            session.getTransaction().commit();
            return query.uniqueResultOptional();
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    /**
     * Ищет все задачи
     * @return список всех найденных задач
     */
    @Override
    public List<Task> findAll() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(FIND_ALL_ORDER_BY_ID_STATEMENT, Task.class);
            session.getTransaction().commit();
            return query.list();
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    /**
     * Изменяет задачу
     * @param task задача, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    @Override
    public boolean update(Task task) {
        boolean replace = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(UPDATE_STATEMENT)
                    .setParameter(NAME, task.getName())
                    .setParameter(DESCRIPTION, task.getDescription())
                    .setParameter(DONE, task.isDone())
                    .setParameter(ID, task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            replace = true;
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return replace;
    }

    /**
     * Удаляет задачу
     * @param task задача, которую необходимо удалить
     * @return задачу обернутую в Optional если задача удалена, и Optional.empty() если не удалена
     */
    @Override
    public Optional<Task> delete(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(DELETE_STATEMENT)
                    .setParameter(ID, task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            return Optional.of(task);
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    /**
     * Обновляет статус задачи
     * @param task задача, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    @Override
    public boolean updateDone(Task task) {
        boolean replace = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(UPDATE_DONE_STATEMENT)
                    .setParameter(DONE, task.isDone())
                    .setParameter(ID, task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            replace = true;
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return replace;
    }

    /**
     * Ищет задачи в которых поле done соответствует аргументу
     * @param isDone аргумент, по которому определяется какие задачи требуется искать true - выполненные,
     *               false - не выполненные
     * @return список задач соответствующих запросу
     */
    @Override
    public List<Task> findByDone(boolean isDone) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(FIND_BY_DONE_STATEMENT, Task.class);
            query.setParameter(DONE, isDone);
            session.getTransaction().commit();
            return query.list();
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void truncateTable() {
        Session session = sf.openSession();
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.createSQLQuery(TRUNCATE_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception in UserRepository", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

}
