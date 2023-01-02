package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой задач
 */
@ThreadSafe
@Service
public class SimpleTaskService implements TaskService {

    private final TaskRepository repository;

    public SimpleTaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Обрабатывает запрос при добавлении задачи в репозиторий
     * @param task добавляемая задача
     * @return задачу обернутую в Optional если добавление задачи прошло успешно, и Optional.empty() если добавить
     * задачу не удалось
     */
    @Override
    public Optional<Task> add(Task task) {
        return repository.add(task);
    }

    /**
     * Обрабатывает запрос при поиске задачи по идентификатору
     * @param id идентификатор задачи
     * @return задачу обернутую в Optional если задача найдена, и Optional.empty() если не найдена
     */
    @Override
    public Optional<Task> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Обрабатывает запрос при поиске всех задач
     * @return список всех найденных задач
     */
    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    /**
     * Обрабатывает запрос при изменении задачи
     * @param task задача, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    @Override
    public boolean update(Task task) {
        if (repository.findById(task.getId()).isEmpty()) {
            return false;
        }
        return repository.update(task);
    }

    /**
     * Обрабатывает запрос при удалении задачи
     * @param task задача, которую необходимо удалить
     * @return задачу обернутую в Optional если задача удалена, и Optional.empty() если не удалена
     */
    @Override
    public Optional<Task> delete(Task task) {
        if (repository.findById(task.getId()).isEmpty()) {
            return Optional.empty();
        }
        return repository.delete(task);
    }

    /**
     * Обрабатывает запрос при обновлении статуса задачи
     * @param task задача, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    @Override
    public boolean updateDone(Task task) {
        if (repository.findById(task.getId()).isEmpty()) {
            return false;
        }
        return repository.updateDone(task);
    }

    /**
     * Обрабатывает запрос при поиске задач в которых поле done соответствует аргументу
     * @param isDone аргумент, по которому определяется какие задачи требуется искать true - выполненные,
     *               false - не выполненные
     * @return список задач соответствующих запросу
     */
    @Override
    public List<Task> findByDone(boolean isDone) {
        return repository.findByDone(isDone);
    }
}
