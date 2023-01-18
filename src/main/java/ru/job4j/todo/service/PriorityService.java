package ru.job4j.todo.service;

import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой задач
 */
public interface PriorityService {
    /**
     * Обрабатывает запрос при добавлении приоритета в репозиторий
     * @param priority добавляемый приоритет
     * @return приоритет обернутый в Optional если добавление приоритета прошло успешно, и Optional.empty() если добавить
     * приоритет не удалось
     */
    Optional<Priority> add(Priority priority);

    /**
     * Обрабатывает запрос при поиске приоритета по идентификатору
     * @param id идентификатор приоритета
     * @return приоритет обернутый в Optional если приоритет найден, и Optional.empty() если не найден
     */
    Optional<Priority> findById(int id);

    /**
     * Обрабатывает запрос при поиске всех приоритетов
     * @return список всех найденных приоритетов
     */
    List<Priority> findAll();

    /**
     * Обрабатывает запрос при изменении приоритета
     * @param priority приоритет, который требуется изменить
     * @return true если приоритет обновлен, false если не обновлен
     */
    boolean update(Priority priority);

    /**
     * Обрабатывает запрос при удалении пиоритета
     * @param id идентификатор приоритета, который необходимо удалить
     * @return приоритет обернутый в Optional если приоритет удален, и Optional.empty() если не удален
     */
    boolean delete(int id);

}
