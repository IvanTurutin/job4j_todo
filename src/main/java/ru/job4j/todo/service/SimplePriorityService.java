package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.PriorityRepository;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой приоритетов
 */
@ThreadSafe
@Service
@AllArgsConstructor
public class SimplePriorityService implements  PriorityService {

    private final PriorityRepository repository;

    @Override
    public Optional<Priority> add(Priority priority) {
        return repository.add(priority);
    }

    @Override
    public Optional<Priority> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Priority> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean update(Priority priority) {
        return repository.update(priority);
    }

    @Override
    public boolean delete(int id) {
        Optional<Priority> optionalPriority = repository.findById(id);
        optionalPriority.ifPresent(repository::delete);
        return optionalPriority.isPresent();
    }
}
