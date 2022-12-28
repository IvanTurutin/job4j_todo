package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой задач
 */
public interface TaskService {
    /**
     * Обрабатывает запрос при добавлении задачи в репозиторий
     * @param task добавляемая задача
     * @return задачу обернутую в Optional если добавление задачи прошло успешно, и Optional.empty() если добавить
     * задачу не удалось
     */
    Optional<Task> add(Task task);

    /**
     * Обрабатывает запрос при поиске задачи по идентификатору
     * @param id идентификатор задачи
     * @return задачу обернутую в Optional если задача найдена, и Optional.empty() если не найдена
     */
    Optional<Task> findById(int id);

    /**
     * Обрабатывает запрос при поиске всех задач
     * @return список всех найденных задач
     */
    List<Task> findAll();

    /**
     * Обрабатывает запрос при изменении задачи
     * @param task задача, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    boolean update(Task task);

    /**
     * Обрабатывает запрос при удалении задачи
     * @param task задача, которую необходимо удалить
     * @return задачу обернутую в Optional если задача удалена, и Optional.empty() если не удалена
     */
    Optional<Task> delete(Task task);

    /**
     * Обрабатывает запрос при обновлении статуса задачи
     * @param task задача, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    boolean updateDone(Task task);

    /**
     * Обрабатывает запрос при поиске задач в которых поле done соответствует аргументу
     * @param isDone аргумент, по которому определяется какие задачи требуется искать true - выполненные,
     *               false - не выполненные
     * @return список задач соответствующих запросу
     */
    List<Task> findByDone(boolean isDone);

}
