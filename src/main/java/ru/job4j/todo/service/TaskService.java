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
    boolean add(Task task);

    /**
     * Производит добавление задачи, при этом производится проверка наличия в базе данных приоритета и
     * категорий указанных в задаче
     * @param task задача для добавления
     * @param categoryIds список категорий
     * @return true если задача успешно добавлена, false если задача не добавлена
     */
    boolean add(Task task, List<Integer> categoryIds);

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
     * Производит обновление задачи, при этом производится проверка наличия в базе данных приоритета и
     * категорий указанных в задаче
     * @param task задача для добавления
     * @param categoryIds список категорий
     * @return true если задача успешно обновлена, false если задача не обновлена
     */
    boolean update(Task task, List<Integer> categoryIds);

    /**
     * Обрабатывает запрос при удалении задачи
     * @param id идентификатор задачи, которую необходимо удалить
     * @return задачу обернутую в Optional если задача удалена, и Optional.empty() если не удалена
     */
    boolean delete(int id);

    /**
     * Обрабатывает запрос при обновлении статуса задачи
     * @param id идентификатор задачи, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    boolean updateDone(int id);

    /**
     * Обрабатывает запрос при поиске задач в которых поле done соответствует аргументу
     * @param isDone аргумент, по которому определяется какие задачи требуется искать true - выполненные,
     *               false - не выполненные
     * @return список задач соответствующих запросу
     */
    List<Task> findByDone(boolean isDone);

}
