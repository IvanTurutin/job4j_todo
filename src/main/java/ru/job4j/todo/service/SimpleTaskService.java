package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleTaskService implements TaskService {

    private final TaskRepository repository;

    public SimpleTaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Task> add(Task task) {
        return repository.add(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean update(Task task) {
        return repository.update(task);
    }

    @Override
    public Optional<Task> delete(Task task) {
        return Optional.empty();
    }

    @Override
    public boolean updateDone(Task task) {
        return repository.updateDone(task);
    }

    @Override
    public List<Task> findByDone(boolean isDone) {
        return repository.findByDone(isDone);
    }
}
