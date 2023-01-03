package ru.job4j.todo.util;

import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;

public final class ControllerUtility {
    private ControllerUtility() {

    }

    private static final String GUEST = "Гость";

    public static User checkUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName(GUEST);
        }
        return user;
    }
}
