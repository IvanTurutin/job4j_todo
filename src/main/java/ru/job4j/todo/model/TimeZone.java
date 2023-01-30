package ru.job4j.todo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Модель данных временной зоны
 */
@Entity
@Table(name = "timezones")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TimeZone {
    /**
     * Идентификатор временной зоны
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Обозначение идентификатра временной зоны
     */
    @Column(unique = true)
    private String zoneId;

}
