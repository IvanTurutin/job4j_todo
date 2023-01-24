package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой приоритетов
 */
public interface CategoryService {
    /**
     * Обрабатывает запрос при добавлении категории в репозиторий
     * @param category добавляемая категория
     * @return true, если категория добавлена в БД, false если не добавлена
     */
    boolean add(Category category);

    /**
     * Обрабатывает запрос при поиске категории по идентификатору
     * @param id идентификатор категории
     * @return категория обернутая в Optional если категория найдена, и Optional.empty() если не найдена
     */
    Optional<Category> findById(int id);

    /**
     * Обрабатывает запрос при поиске всех категорий
     * @return список всех найденных категорий
     */
    List<Category> findAll();

    /**
     * Обрабатывает запрос при изменении категории
     * @param category категория, которую требуется изменить
     * @return true если категория обновлена, false если не обновлена
     */
    boolean update(Category category);

    /**
     * Обрабатывает запрос при удалении категории
     * @param id идентификатор категории, которую необходимо удалить
     * @return true, если удаление успешно, false если не удалось удалить
     */
    boolean delete(int id);

    /**
     * Определяет наличие всех категорий в списке идентификаторов
     * @param categoryIds список идентификаторов категорий для проверки на наличие
     * @return список категорий если все эменты списка имеются в базе данных, пустой список если хотя бы одна из
     * категорий отсутствует
     */
     List<Category> isPresent(List<Integer> categoryIds);

    /**
     * Производит проверку, все ли категории присутствуют в БД
     * @param ids список идентификатовро категорий
     * @return true если все категории присутствуют в базе данных, false если хотя бы одна из категорий отсутствует
     */
     List<Category> allPresent(List<Integer> ids);

}
