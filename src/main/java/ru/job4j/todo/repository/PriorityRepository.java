package ru.job4j.todo.repository;

import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий задач
 * @see ru.job4j.todo.model.Priority
 */
public interface PriorityRepository {
    /**
     * Добавляет проритет в репозиторий и назначает ей id
     * @param priority добавляемый приоритет
     * @return приоритет обернутый в Optional если добавление приоритета прошло успешно, и Optional.empty() если добавить
     * приоритет не удалось
     */
    Optional<Priority> add(Priority priority);

    /**
     * Ищет приоритет по идентификатору
     * @param id идентификатор приоритета
     * @return приоритет обернутый в Optional если приоритет найден, и Optional.empty() если не найден
     */
    Optional<Priority> findById(int id);

    /**
     * Ищет все приоритеты
     * @return список всех найденных приоритетов
     */
    List<Priority> findAll();

    /**
     * Изменяет приоритет
     * @param priority приоритет, который требуется изменить
     * @return true если приоритет обновлен, false если не обновлен
     */
    boolean update(Priority priority);

    /**
     * Удаляет приоритет
     * @param priority приоритет, который необходимо удалить
     * @return приоритет обернутый в Optional если приоритет удален, и Optional.empty() если не удален
     */
    Optional<Priority> delete(Priority priority);

}
