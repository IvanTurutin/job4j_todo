package ru.job4j.todo.service;

import ru.job4j.todo.model.TimeZone;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой временных зон
 * @see ru.job4j.todo.model.TimeZone
 */
public interface TimeZoneService {

    /**
     * Обрабатывает запрос при добавлении временной зоны в репозиторий
     * @param timeZone добавляемая зона
     * @return true, если зона добавлена в БД, false если не добавлена
     */
    boolean add(TimeZone timeZone);

    /**
     * Обрабатывает запрос при поиске временной зоны по идентификатору
     * @param id идентификатор зоны
     * @return зона обернутая в Optional если зона найдена, и Optional.empty() если не найдена
     */
    Optional<TimeZone> findById(int id);

    /**
     * Обрабатывает запрос при поиске всех временных зон
     * @return список всех найденных зон
     */
    List<TimeZone> findAll();

    /**
     * Обрабатывает запрос при изменении временной зоны
     * @param timeZone зона, которую требуется изменить
     * @return true если зона обновлена, false если не обновлена
     */
    boolean update(TimeZone timeZone);

    /**
     * Обрабатывает запрос при удалении временной зоны
     * @param id идентификатор зоны, которую необходимо удалить
     * @return true, если удаление успешно, false если не удалось удалить
     */
    boolean delete(int id);

}
