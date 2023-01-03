package ru.job4j.todo.repository;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий пользователей
 * @see ru.job4j.todo.model.User
 */
@ThreadSafe
@Repository
public class HbmUserRepository implements UserRepository {

    private final SessionFactory sf;

    private static final Logger LOG = LoggerFactory.getLogger(HbmUserRepository.class.getName());
    private static final String LOG_MESSAGE = "Exception in UserRepository";

    public static final String MODEL = "User";
    public static final String ID = "fID";
    public static final String DELETE_STATEMENT = String.format(
            "DELETE %s WHERE id = :%s",
            MODEL, ID
    );
    public static final String LOGIN = "fLogin";
    public static final String PASSWORD = "fPassword";
    public static final String FIND_ALL_STATEMENT = String.format("from %s", MODEL);
    private static final String FIND_BY_LOGIN_AND_PASSWORD_STATEMENT = FIND_ALL_STATEMENT
            + String.format(" WHERE login = :%s AND password = :%s",  LOGIN, PASSWORD);

    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT + " order by id";
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where id = :%s", ID);
    public static final String TRUNCATE_TABLE = "DELETE FROM users";

    public HbmUserRepository(SessionFactory sf) {
        this.sf = sf;
    }

    /**
     * Добавляет пользователя в базу данных с присвоением объекту идентификатора.
     * @param user объект пользователя, который необходимо добавить.
     * @return объект пользователя с присвоенным идентификатором обернутым в Optional, если пользователь успешно добавлен,
     * либо Optional.empty() если пользователь не добавлен.
     */
    @Override
    public Optional<User> add(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return Optional.of(user);
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    /**
     * Удаляет пользователя из базы данных
     * @param id идентификатор пользователя, которого нужно удалить
     * @return Optional с удаленным пользователем, или Optional.empty() если пользователь не был удален.
     */
    @Override
    public boolean delete(int id) {
        boolean delete = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(DELETE_STATEMENT)
                    .setParameter(ID, id)
                    .executeUpdate();
            session.getTransaction().commit();
            delete = true;
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return delete;
    }

    /**
     * Обновляет пользователя в базе данных
     * @param user объект пользователя, который нужно обновить
     * @return Optional с обновленным пользователем, или Optional.empty() если пользователь не был обновлен.
     */
    @Override
    public boolean update(User user) {
        boolean update = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
            update = true;
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return update;
    }

    /**
     * Осуществляет поиск пользователя по логину и паролю
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return пользоваетеля в обертке Optional, если он найден, или Optional.empty() - если не найден
     */
    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(FIND_BY_LOGIN_AND_PASSWORD_STATEMENT, User.class);
            query.setParameter(LOGIN, login);
            query.setParameter(PASSWORD, password);
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
     * Осуществляет поиск всех пользователей
     * @return список пользователей
     */
    @Override
    public List<User> findAll() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(FIND_ALL_ORDER_BY_ID_STATEMENT, User.class);
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
     * Находит пользователя по идентификатору
     * @param id идентификтор пользователя
     * @return пользователя обернутого в Optional, если пользователь найден,
     * либо Optional.empty() если пользователь не найден.
     */
    @Override
    public Optional<User> findById(int id) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(FIND_BY_ID_STATEMENT, User.class);
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
     * Очищает таблицу от записей
     */
    public void truncateTable() {
        Session session = sf.openSession();
        try {
            session = sf.openSession();
            session.beginTransaction();
            session.createSQLQuery(TRUNCATE_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error(LOG_MESSAGE, e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

}
