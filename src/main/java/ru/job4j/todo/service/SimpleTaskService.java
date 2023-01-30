package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.TaskRepository;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Сервисный слой задач
 */
@ThreadSafe
@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository repository;
    private final CategoryService categoryService;
    private final PriorityService priorityService;
    private final static Logger LOG = LoggerFactory.getLogger(SimpleTaskService.class.getName());

    /**
     * Обрабатывает запрос при добавлении задачи в репозиторий
     * @param task добавляемая задача
     * @return задачу обернутую в Optional если добавление задачи прошло успешно, и Optional.empty() если добавить
     * задачу не удалось
     */
    @Override
    public boolean add(Task task) {
        if (task.getName().isEmpty()) {
            task.setName("Not specified");
        }
        return repository.add(task).isPresent();
    }

    @Override
    public boolean add(Task task, List<Integer> categoryIds) {
        if (!checkCategory(task, categoryIds) || !checkPriority(task)) {
            return false;
        }
        return add(task);
    }

    private boolean checkCategory(Task task, List<Integer> categoryIds) {
        List<Category> categories = categoryIds.stream().map(id -> {
            Category cat = new Category();
            cat.setId(id);
            return cat;
        }).toList();
        if (!new HashSet<>(categoryService.allPresent(categoryIds)).containsAll(categories)) {
            LOG.warn("Не удалось найти все указаные категории.");
            return false;
        }
        task.setCategories(categories);
        return true;
    }

    private boolean checkPriority(Task task) {
        Optional<Priority> optPriority = priorityService.findById(task.getPriority().getId());
        if (optPriority.isEmpty()) {
            LOG.warn("Не удалось найти такой уровень приоритета.");
            return false;
        }
        return true;
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

    @Override
    public Optional<Task> findById(int id, User user) {
        Optional<Task> task = findById(id);
        if (user.getTimeZone() != null) {
            task.ifPresent(t -> setTimeZone(t, user.getTimeZone().getZoneId()));
        } else {
            task.ifPresent(this::setDefaultTimeZone);
        }
        return task;
    }

    /**
     * Обрабатывает запрос при поиске всех задач
     * @return список всех найденных задач
     */
    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Task> findAll(User user) {
        List<Task> tasks = findAll();
        if (user.getTimeZone() != null) {
            tasks.forEach(t -> setTimeZone(t, user.getTimeZone().getZoneId()));
        } else {
            tasks.forEach(this::setDefaultTimeZone);
        }
        return tasks;
    }

    private void setTimeZone(Task task, String zoneId) {
        task.setCreated(
                task.getCreated().atZone(
                                ZoneId.of(TimeZone.getDefault().toZoneId().getId()))
                        .withZoneSameInstant(ZoneId.of(zoneId))
                        .toLocalDateTime()
        );
    }

    private void setDefaultTimeZone(Task task) {
        task.setCreated(
                task.getCreated().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()
        );
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

    @Override
    public boolean update(Task task, List<Integer> categoryIds) {
        if (!checkCategory(task, categoryIds) || !checkPriority(task)) {
            return false;
        }
        return update(task);
    }

    /**
     * Обрабатывает запрос при удалении задачи
     * @param id идентификатор задачи, которую необходимо удалить
     * @return true если задача удалена, и false если не удалена
     */
    @Override
    public boolean delete(int id) {
        Optional<Task> optionalTask = repository.findById(id);
        return optionalTask.isPresent() && repository.delete(optionalTask.get()).isPresent();
    }

    /**
     * Обрабатывает запрос при обновлении статуса задачи
     * @param id идентификатор задачи, которую требуется изменить
     * @return true если задача обновлена, false если не обновлена
     */
    @Override
    public boolean updateDone(int id) {
        Optional<Task> optionalTask = repository.findById(id);
        if (optionalTask.isEmpty()) {
            return false;
        }
        optionalTask.get().setDone(true);
        return repository.updateDone(optionalTask.get());
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
