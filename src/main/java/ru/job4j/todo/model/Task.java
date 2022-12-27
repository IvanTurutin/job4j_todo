package ru.job4j.todo.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Модель данных Task (задача)
 */
@Entity
@Table(name = "tasks")
@Data
public class Task {

    /**
     * Идентификатор задачи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private LocalDateTime created = LocalDateTime.now().withNano(3);
    /**
     * Статус выполнения задачи
     */
    private boolean done;
}