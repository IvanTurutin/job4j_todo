package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер задач
 */
@ThreadSafe
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Производит подготовку для формирования вида, отображающего детальные сведения о задаче.
     * @param model модель вида
     * @param session сессия подключения
     * @param id идентификатор задачи
     * @return если задача не найдена переводит на вид с выводом ошибки, если задача найдена, переводит на вид с
     * детальным отображением сведений о задаче
     */
    @GetMapping("/formShow/{id}")
    public String formShowSession(Model model, HttpSession session, @PathVariable("id") int id) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            model.addAttribute("message", "Такая задача не найдена.");
            return "message/fail";
        }
        session.setAttribute("task", task.get());
        return "task/show";
    }

    /**
     * Приозводит подготовку к формированию вида, отображающего выполненные задачи или невыполненные задачи
     * @param model модель вида
     * @param isDone статус задачи
     * @return название шаблона для формирования соответствующего списка задач
     */
    @GetMapping("/done/{isDone}")
    public String doneTasks(Model model, @PathVariable("isDone") boolean isDone) {
        List<Task> tasks = taskService.findByDone(isDone);
        model.addAttribute("tasks", tasks);
        return "index";
    }

    /**
     * Обрабатывает запрос на отображение вида добавления новой задачи
     * @param model модель вида
     * @return название шаблона для формирования вида добавления новой задачи
     */
    @GetMapping("/new")
    public String newTask(Model model) {
        /*model.addAttribute("user", ControllerUtility.checkUser(session));*/
        return "task/addNew";
    }

    /**
     * Обрабатывает запрос на добавление новой задачи
     * @param model модель вида
     * @param task объект задачи, сформированный из введненных данных
     * @return в случае невозможности добавить задачу - перенаправляет на страницу, сообщающую о неудаче при добавлении задачи,
     * в случае успешного добавления задачи - перенаправляет на страницу, сообщающую о успешном добавлении задачи
     */
    @PostMapping("/add")
    public String registration(Model model, @ModelAttribute Task task) {
        Optional<Task> addedTask = taskService.add(task);
        if (addedTask.isEmpty()) {
            model.addAttribute("message", "Не удалось добавить новую задачу.");
            return "message/fail";
        }
        model.addAttribute("message", "Задача успешно добавлена.");
        return "message/success";
    }

    /**
     * Обрабатывает запрос на формирование вида для редактирования задачи
     * @param session сессия подключения
     * @param model модель вида
     * @return название шаблона вида для редактирования задачи
     */
    @GetMapping("/formUpdate")
    public String formUpdateTask(HttpSession session, Model model) {
/*
        model.addAttribute("user", ControllerUtility.checkUser(session));
*/
        model.addAttribute("task", session.getAttribute("task"));
        return "task/update";
    }

    /**
     * Обрабатывает запрос на обновление задачи
     * @param model модель вида
     * @param task объект задачи, сформированный из введненных данных
     * @return если задача не обновлена перенаправляет на страницу с выводом ошибки, если задача обновлена - перенаправляет
     * на страницу, сообщающую об успешном обновлении задачи
     */
    @PostMapping("/update")
    public String updateTask(Model model, @ModelAttribute Task task) {
        boolean updatedTask = taskService.update(task);
        if (!updatedTask) {
            model.addAttribute("message", "Не удалось обновить задачу.");
            return "message/fail";
        }
        model.addAttribute("message", "Задача успешно обновлена.");
        return "message/success";
    }

    /**
     * Обработка запроса на изменение статуса
     * @param session сессия подключения
     * @param model модель вида
     * @return если задача не обновлена перенаправляет на страницу с выводом ошибки, если задача обновлена - перенаправляет
     * на страницу, сообщающую об успешном обновлении задачи
     */
    @PostMapping("/isDone")
    public String taskIsDone(HttpSession session, Model model) {
        Task sessionTask = (Task) session.getAttribute("task");
        sessionTask.setDone(true);
        boolean isDone = taskService.updateDone(sessionTask);
        if (!isDone) {
            model.addAttribute("message", "Не удалось обновить задачу.");
            return "message/fail";
        }
        model.addAttribute("message", "Задача успешно обновлена.");
        return "message/success";
    }

    /**
     * Обрабатывает запрос на удаление задачи
     * @param session сессия подключения
     * @param model модель вида
     * @return если задача не удалена - перенаправляет на страницу с выводом ошибки, если задача обновлена - перенаправляет
     * на страницу, сообщающую об успешном удалении задачи
     */
    @PostMapping("/delete")
    public String deleteTask(HttpSession session, Model model) {
        Task sessionTask = (Task) session.getAttribute("task");
        Optional<Task> deleted = taskService.delete(sessionTask);
        if (deleted.isEmpty()) {
            model.addAttribute("message", "Не удалось удалить задачу.");
            return "message/fail";
        }
        model.addAttribute("message", "Задача успешно удалена.");
        return "message/success";
    }


}
