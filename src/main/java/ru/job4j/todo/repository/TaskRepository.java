package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий задач
 * @see ru.job4j.todo.model.Task
 */
public interface TaskRepository {
    /**
     * Добавляет задачу в репозиторий и назначает ей id
     * @param task добавляемая задача
     * @return задачу обернутую в Optional если добавление задачи прошло успешно, и Optional.empty() если добавить
     * задачу не удалось
     */
    Optional<Task> add(Task task);

    /**
     * Ищет задачу по идентификатору
     * @param id идентификатор задачи
     * @return задачу обернутую в Optional если задача найдена, и Optional.empty() если не найдена
     */
    Optional<Task> findById(int id);

    /**
     * Ищет все задачи
     * @return список всех найденных задач
     */
    List<Task> findAll();

    /**
     * Изменяет задачу
     * @param task задача, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    boolean update(Task task);

    /**
     * Удаляет задачу
     * @param task задача, которую необходимо удалить
     * @return задачу обернутую в Optional если задача удалена, и Optional.empty() если не удалена
     */
    Optional<Task> delete(Task task);
}
