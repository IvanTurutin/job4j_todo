package ru.job4j.todo;

import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.TimeZone;

public class TimeZoneRun {
    public static void main(String[] args) {
        var zones = new ArrayList<TimeZone>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        for (TimeZone zone : zones) {
            System.out.println(zone.getID() + " : " + zone.getDisplayName());
        }
    }
}
