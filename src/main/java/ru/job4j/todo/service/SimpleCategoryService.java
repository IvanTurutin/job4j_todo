package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ThreadSafe
@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {

    private CategoryRepository repository;
    @Override
    public boolean add(Category category) {
        return repository.add(category).isPresent();
    }

    @Override
    public Optional<Category> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean update(Category category) {
        return repository.update(category);
    }

    @Override
    public boolean delete(int id) {
        Optional<Category> optionalCategory = repository.findById(id);
        optionalCategory.ifPresent(repository::delete);
        return optionalCategory.isPresent();
    }

    @Override
    public List<Category> isPresent(List<Integer> categoryIds) {
        List<Category> categories = categoryIds.stream().map(id -> {
            Category category = new Category();
            category.setId(id);
            return category;
        }).collect(Collectors.toList());
        return categories.stream().allMatch(category -> findById(category.getId()).isPresent())
                ? categories
                : new ArrayList<>();
    }

    @Override
    public List<Category> allPresent(List<Integer> ids) {
        return repository.allPresent(ids);
    }
}
