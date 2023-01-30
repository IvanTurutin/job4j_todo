package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.util.ControllerUtility;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер задач
 */
@ThreadSafe
@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;


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
        model.addAttribute("user", ControllerUtility.checkUser(session));
        Optional<Task> task = taskService.findById(id, (User) session.getAttribute("user"));
        if (task.isEmpty()) {
            model.addAttribute("message", "Такая задача не найдена.");
            return "message/fail";
        }
        model.addAttribute("task", task.get());
        return "task/show";
    }

    /**
     * Приозводит подготовку к формированию вида, отображающего выполненные задачи или невыполненные задачи
     * @param model модель вида
     * @param session сессия подключения
     * @param isDone статус задачи
     * @return название шаблона для формирования соответствующего списка задач
     */
    @GetMapping("/done/{isDone}")
    public String doneTasks(HttpSession session, Model model, @PathVariable("isDone") boolean isDone) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        List<Task> tasks = taskService.findByDone(isDone);
        model.addAttribute("tasks", tasks);
        return "index";
    }

    /**
     * Обрабатывает запрос на отображение вида добавления новой задачи
     * @param model модель вида
     * @param session сессия подключения
     * @return название шаблона для формирования вида добавления новой задачи
     */
    @GetMapping("/new")
    public String newTask(HttpSession session, Model model) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
/*
        model.addAttribute("task", new Task());
*/
        return "task/addNew";
    }

    /**
     * Обрабатывает запрос на добавление новой задачи
     * @param session сессия подключения
     * @param model модель вида
     * @param task объект задачи, сформированный из введненных данных
     * @return в случае невозможности добавить задачу - перенаправляет на страницу, сообщающую о неудаче при добавлении задачи,
     * в случае успешного добавления задачи - перенаправляет на страницу, сообщающую о успешном добавлении задачи
     */
    @PostMapping("/add")
    public String add(HttpSession session, Model model, @ModelAttribute Task task, @RequestParam List<Integer> categoryList) {
        User user = ControllerUtility.checkUser(session);
        model.addAttribute("user", user);
        task.setUser(user);
        if (!taskService.add(task, categoryList)) {
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
     * @param id идентификатор задачи
     * @return название шаблона вида для редактирования задачи
     */
    @GetMapping("/formUpdate/{id}")
    public String formUpdateTask(HttpSession session, Model model, @PathVariable("id") int id) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("task", taskService.findById(id));
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "task/update";
    }

    /**
     * Обрабатывает запрос на обновление задачи
     * @param session сессия подключения
     * @param model модель вида
     * @param task объект задачи, сформированный из введненных данных
     * @return если задача не обновлена перенаправляет на страницу с выводом ошибки, если задача обновлена - перенаправляет
     * на страницу, сообщающую об успешном обновлении задачи
     */
    @PostMapping("/update")
    public String updateTask(HttpSession session, Model model, @ModelAttribute Task task, @RequestParam List<Integer> categoryList) {
        User user = ControllerUtility.checkUser(session);
        model.addAttribute("user", user);
        task.setUser(user);

        if (!taskService.update(task, categoryList)) {
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
     * @param id идентификатор задачи
     * @return если задача не обновлена перенаправляет на страницу с выводом ошибки, если задача обновлена - перенаправляет
     * на страницу, сообщающую об успешном обновлении задачи
     */
    @PostMapping("/isDone/{id}")
    public String taskIsDone(HttpSession session, Model model, @PathVariable("id") int id) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        if (!taskService.updateDone(id)) {
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
     * @param id идентификатор задачи
     * @return если задача не удалена - перенаправляет на страницу с выводом ошибки, если задача обновлена - перенаправляет
     * на страницу, сообщающую об успешном удалении задачи
     */
    @PostMapping("/delete/{id}")
    public String deleteTask(HttpSession session, Model model, @PathVariable("id") int id) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        if (!taskService.delete(id)) {
            model.addAttribute("message", "Не удалось удалить задачу.");
            return "message/fail";
        }
        model.addAttribute("message", "Задача успешно удалена.");
        return "message/success";
    }
}
