package ru.job4j.todo.repository;

import ru.job4j.todo.model.TimeZone;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище временных зон
 * @see ru.job4j.todo.model.TimeZone
 */
public interface TimeZoneRepository {
    /**
     * Добавляет зону в репозиторий и назначает ей id
     * @param timeZone добавляемая зона
     * @return зона обернутая в Optional если добавление зоны прошло успешно, и Optional.empty() если добавить
     * зону не удалось
     */
    Optional<TimeZone> add(TimeZone timeZone);

    /**
     * Ищет категорию по идентификатору
     * @param id идентификатор категории
     * @return категория обернутая в Optional если категория найдена, и Optional.empty() если не найдена
     */
    Optional<TimeZone> findById(int id);

    /**
     * Ищет все зоны
     * @return список всех найденных зон
     */
    List<TimeZone> findAll();

    /**
     * Изменяет зону
     * @param timeZone зона, которую требуется изменить
     * @return true если зона обновлена, false если не обновлена
     */
    boolean update(TimeZone timeZone);

    /**
     * Удаляет зону
     * @param timeZone зона, которую необходимо удалить
     * @return зона обернутая в Optional если зона удалена, и Optional.empty() если не удалена
     */
    Optional<TimeZone> delete(TimeZone timeZone);

}
