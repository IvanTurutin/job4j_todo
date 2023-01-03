package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.util.ControllerUtility;

import javax.servlet.http.HttpSession;

/**
 * Контроллер стартовой страницы
 */
@ThreadSafe
@Controller
public class IndexController {
    private final TaskService taskService;

    public IndexController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Принимает запрос на отображение стартовой страницы
     * @param model модель вида
     * @param session сессия подключения
     * @return названия шаблона, которое требуется ипользовать для формирния вида и показа пользователю
     */
    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("tasks", taskService.findAll());
        return "index";
    }

}
