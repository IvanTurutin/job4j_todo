package ru.job4j.todo.repository;

import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий категорий
 * @see ru.job4j.todo.model.Category
 */
public interface CategoryRepository {
    /**
     * Добавляет категорию в репозиторий и назначает ей id
     * @param category добавляемая категория
     * @return категория обернутая в Optional если добавление категории прошло успешно, и Optional.empty() если добавить
     * категорию не удалось
     */
    Optional<Category> add(Category category);

    /**
     * Ищет категорию по идентификатору
     * @param id идентификатор категории
     * @return категория обернутая в Optional если категория найдена, и Optional.empty() если не найдена
     */
    Optional<Category> findById(int id);

    /**
     * Ищет все категории
     * @return список всех найденных категорий
     */
    List<Category> findAll();

    /**
     * Изменяет категорию
     * @param category категория, которую требуется изменить
     * @return true если категорию обновлена, false если не обновлена
     */
    boolean update(Category category);

    /**
     * Удаляет категорию
     * @param category категория, которую необходимо удалить
     * @return категория обернутая в Optional если категория удалена, и Optional.empty() если не удалена
     */
    Optional<Category> delete(Category category);

    /**
     * Находит все категории, идентификаторы которых передаются в виде списка
     * @param ids список идентификаторов категорий
     * @return список найденных категорий
     */
    List<Category> allPresent(List<Integer> ids);
}
