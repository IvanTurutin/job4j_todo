package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель данных Task (задача)
 */
@Entity
@Table(name = "tasks")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    /**
     * Идентификатор задачи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    /**
     * Название задачи
     */
    private String name;
    /**
     * Описание задачи
     */
    private String description;
    /**
     * Время создания задачи
     */
    private LocalDateTime created = LocalDateTime.now().withNano(0);
    /**
     * Статус выполнения задачи
     */
    private boolean done = false;

    /**
     * Пользователь, которому принадлежит эта задача
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Приоритет задачи
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    private Priority priority;


    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns = { @JoinColumn(name = "task_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private List<Category> categories = new ArrayList<>();

}