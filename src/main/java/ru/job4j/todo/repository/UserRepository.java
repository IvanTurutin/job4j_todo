package ru.job4j.todo.repository;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище пользователей
 * @see ru.job4j.todo.model.User
 */
public interface UserRepository {

    /**
     * Добавляет пользователя в базу данных с присвоением объекту идентификатора.
     * @param user объект пользователя, который необходимо добавить.
     * @return объект пользователя с присвоенным идентификатором обернутым в Optional, если пользователь успешно добавлен,
     * либо Optional.empty() если пользователь не добавлен.
     */
    Optional<User> add(User user);

    /**
     * Удаляет пользователя из базы данных
     * @param id идентификатор пользователя, которого нужно удалить
     * @return Optional с удаленным пользователем, или Optional.empty() если пользователь не был удален.
     */
    boolean delete(int id);

    /**
     * Обновляет пользователя в базе данных
     * @param user объект пользователя, который нужно обновить
     * @return Optional с обновленным пользователем, или Optional.empty() если пользователь не был обновлен.
     */
    boolean update(User user);

    /**
     * Поиск пользователя по почте и паролю
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return пользователя обернутого в Optional, если пользователь найден,
     * либо Optional.empty() если пользователь не найден.
     */
    Optional<User> findByLoginAndPassword(String login, String password);

    /**
     * Находит всех пользователей, имеющихся в базе данных
     * @return Список пользоваетелей.
     */
    List<User> findAll();

    /**
     * Находит пользователя по идентификатору
     * @param id идентификтор пользователя
     * @return пользователя обернутого в Optional, если пользователь найден,
     * либо Optional.empty() если пользователь не найден.
     */
    Optional<User> findById(int id);

}
