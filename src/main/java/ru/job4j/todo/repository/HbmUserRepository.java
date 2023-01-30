package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.*;

/**
 * Репозиторий пользователей
 * @see ru.job4j.todo.model.User
 */
@ThreadSafe
@Repository
@AllArgsConstructor
public class HbmUserRepository implements UserRepository {
    private final CrudRepository cr;

    public static final String MODEL = "User";
    public static final String ID = "fID";
    public static final String DELETE_STATEMENT = String.format(
            "DELETE %s WHERE id = :%s",
            MODEL, ID
    );
    public static final String NAME = "fName";
    public static final String LOGIN = "fLogin";
    public static final String PASSWORD = "fPassword";

    public static final String FIND_ALL_STATEMENT = String.format("from %s", MODEL);
    private static final String FIND_BY_LOGIN_AND_PASSWORD_STATEMENT = FIND_ALL_STATEMENT
            + String.format(" WHERE login = :%s AND password = :%s",  LOGIN, PASSWORD);

    public static final String FIND_ALL_ORDER_BY_ID_STATEMENT = FIND_ALL_STATEMENT + " order by id";
    public static final String FIND_BY_ID_STATEMENT = FIND_ALL_STATEMENT + String.format(" where id = :%s", ID);
    public static final String TRUNCATE_TABLE = String.format("DELETE FROM %s", MODEL);

    /**
     * Добавляет пользователя в базу данных с присвоением объекту идентификатора.
     * @param user объект пользователя, который необходимо добавить.
     * @return объект пользователя с присвоенным идентификатором обернутым в Optional, если пользователь успешно добавлен,
     * либо Optional.empty() если пользователь не добавлен.
     */
    @Override
    public Optional<User> add(User user) {
        int id = user.getId();
        cr.run(session -> session.persist(user));
        return id == user.getId() ? Optional.empty() : Optional.of(user);
    }

    /**
     * Удаляет пользователя из базы данных
     * @param id идентификатор пользователя, которого нужно удалить
     * @return Optional с удаленным пользователем, или Optional.empty() если пользователь не был удален.
     */
    @Override
    public boolean delete(int id) {
        return cr.query(
                DELETE_STATEMENT,
                Map.of(ID, id)
        );
    }

    /**
     * Обновляет пользователя в базе данных
     * @param user объект пользователя, который нужно обновить
     * @return Optional с обновленным пользователем, или Optional.empty() если пользователь не был обновлен.
     */
    @Override
    public boolean update(User user) {
        return cr.run(session -> session.merge(user));
    }

    /**
     * Осуществляет поиск пользователя по логину и паролю
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return пользоваетеля в обертке Optional, если он найден, или Optional.empty() - если не найден
     */
    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return cr.optional(
                FIND_BY_LOGIN_AND_PASSWORD_STATEMENT, User.class,
                Map.of(
                        LOGIN, login,
                        PASSWORD, password
                )
        );
    }

    /**
     * Осуществляет поиск всех пользователей
     * @return список пользователей
     */
    @Override
    public List<User> findAll() {
        return cr.query(FIND_ALL_ORDER_BY_ID_STATEMENT, User.class);
    }

    /**
     * Находит пользователя по идентификатору
     * @param id идентификтор пользователя
     * @return пользователя обернутого в Optional, если пользователь найден,
     * либо Optional.empty() если пользователь не найден.
     */
    @Override
    public Optional<User> findById(int id) {
        return cr.optional(
                FIND_BY_ID_STATEMENT, User.class,
                Map.of(ID, id)
        );
    }

    /**
     * Очищает таблицу от записей
     */
    public void truncateTable() {
        cr.run(TRUNCATE_TABLE, new HashMap<>());
    }

}
