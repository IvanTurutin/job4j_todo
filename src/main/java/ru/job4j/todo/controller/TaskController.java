package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @GetMapping("/formShowTask/{taskId}")
    public String formShowSession(Model model, HttpSession session, @PathVariable("taskId") int id) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            model.addAttribute("message", "Такая задача не найдена.");
            return "message/fail";
        }
        session.setAttribute("task", task.get());
        return "task/showTask";
    }

    /**
     * Приозводит подготовку к формированию вида, отображающего выполненные задачи или невыполненные задачи
     * @param model модель вида
     * @param session сессия подключения
     * @param isDone статус задачи
     * @return название шаблона для формирования соответствующего списка задач
     */
    @GetMapping("/doneTasks/{isDone}")
    public String doneTasks(Model model, HttpSession session, @PathVariable("isDone") boolean isDone) {
        System.out.println(isDone);
        List<Task> tasks = taskService.findByDone(isDone);
        tasks.forEach(System.out::println);
        model.addAttribute("tasks", tasks);
        return "index";
    }

    /**
     * Обрабатывает запрос на отображение вида добавления новой задачи
     * @param model модель вида
     * @return название шаблона для формирования вида добавления новой задачи
     */
    @GetMapping("/newTask")
    public String newTask(Model model) {
        /*model.addAttribute("user", ControllerUtility.checkUser(session));*/
        return "task/addNewTask";
    }

    /**
     * Обрабатывает запрос на добавление новой задачи
     * @param task объект задачи, сформированный из введненных данных
     * @return в случае невозможности добавить задачу - перенаправляет на страницу, сообщающую о неудаче при добавлении задачи,
     * в случае успешного добавления задачи - перенаправляет на страницу, сообщающую о успешном добавлении задачи
     */
    @PostMapping("/addTask")
    public String registration(@ModelAttribute Task task) {
        Optional<Task> addedTask = taskService.add(task);
        if (addedTask.isEmpty()) {
            return "redirect:/failAddTask";
        }
        return "redirect:/successAddTask";
    }

    /**
     * Обрабатывает запрос на отображение сообщения о неудачном добавлении задачи
     * @param model модель вида
     * @return название шаблона, сообщающего о неудачном добавлении задачи
     */
    @GetMapping("/failAddTask")
    public String failRegistration(Model model) {
/*
        model.addAttribute("user", ControllerUtility.checkUser(session));
*/
        model.addAttribute("message", "Не удалось добавить новую задачу.");
        return "message/fail";
    }

    /**
     * Обрабатывает запрос на отображение сообщения об удачном добавлении задачи
     * @param model модель вида
     * @return название шаблона, сообщающего об удачном добавлении задачи
     */
    @GetMapping("/successAddTask")
    public String successRegistration(Model model) {
/*
        model.addAttribute("user", ControllerUtility.checkUser(session));
*/
        model.addAttribute("message", "Задача успешно добавлена.");
        return "message/success";
    }

    /**
     * Обрабатывает запрос на формирование вида для редактирования задачи
     * @param model модель вида
     * @return название шаблона вида для редактирования задачи
     */
    @GetMapping("/formUpdateTask")
    public String formUpdateTask(Model model) {
/*
        model.addAttribute("user", ControllerUtility.checkUser(session));
*/
        return "task/updateTask";
    }

    /**
     * Обрабатывает запрос на обновление задачи
     * @param session сессия подключения
     * @param task объект задачи, сформированный из введненных данных
     * @return если задача не обновлена перенаправляет на страницу с выводом ошибки, если задача обновлена - перенаправляет
     * на страницу, сообщающую об успешном обновлении задачи
     */
    @PostMapping("/updateTask")
    public String updateTask(HttpSession session, @ModelAttribute Task task) {
        Task sessionTask = (Task) session.getAttribute("task");
        if (task.getName().isEmpty()) {
            task.setName(sessionTask.getName());
        }
        if (task.getDescription().isEmpty()) {
            task.setDescription(sessionTask.getDescription());
        }
        boolean updatedTask = taskService.update(task);
        if (!updatedTask) {
            return "redirect:/failUpdateTask";
        }
        return "redirect:/successUpdateTask";
    }

    /**
     * Обрабатывает запрос при неудачном обновлении задачи
     * @param model модель вида
     * @return название шаблона с сообщением о неудачном обновлении задачи
     */
    @GetMapping("/failUpdateTask")
    public String failUpdateTask(Model model) {
/*
        model.addAttribute("user", ControllerUtility.checkUser(session));
*/
        model.addAttribute("message", "Не удалось обновить задачу.");
        return "message/fail";
    }

    /**
     * Обрабатывает запрос при удачном обновлении задачи
     * @param model модель вида
     * @return название шаблона с сообщением о удачном обновлении задачи
     */
    @GetMapping("/successUpdateTask")
    public String successUpdateTask(Model model) {
/*
        model.addAttribute("user", ControllerUtility.checkUser(session));
*/
        model.addAttribute("message", "Задача успешно обновлена.");
        return "message/success";
    }

    /**
     * Обработка запроса на изменение статуса
     * @param session сессия подключения
     * @return если задача не обновлена перенаправляет на страницу с выводом ошибки, если задача обновлена - перенаправляет
     * на страницу, сообщающую об успешном обновлении задачи
     */
    @PostMapping("/taskIsDone")
    public String taskIsDone(HttpSession session) {
        Task sessionTask = (Task) session.getAttribute("task");
        sessionTask.setDone(true);
        boolean isDone = taskService.updateDone(sessionTask);
        if (!isDone) {
            return "redirect:/failUpdateTask";
        }
        return "redirect:/successUpdateTask";
    }

    /**
     * Обрабатывает запрос на удаление задачи
     * @param session сессия подключения
     * @return если задача не удалена - перенаправляет на страницу с выводом ошибки, если задача обновлена - перенаправляет
     * на страницу, сообщающую об успешном удалении задачи
     */
    @PostMapping("/deleteTask")
    public String deleteTask(HttpSession session) {
        Task sessionTask = (Task) session.getAttribute("task");
        Optional<Task> deleted = taskService.delete(sessionTask);
        if (deleted.isEmpty()) {
            return "redirect:/failDeleteTask";
        }
        return "redirect:/successDeleteTask";
    }

    /**
     * Обрабатывает запрос при неудачном удалении задачи
     * @param model модель вида
     * @return название шаблона с сообщением о неудачном удалении задачи
     */
    @GetMapping("/failDeleteTask")
    public String failDeleteTask(Model model) {
/*
        model.addAttribute("user", ControllerUtility.checkUser(session));
*/
        model.addAttribute("message", "Не удалось удалить задачу.");
        return "message/fail";
    }

    /**
     * Обрабатывает запрос при удачном удалении задачи
     * @param model модель вида
     * @return название шаблона с сообщением о удачном удалении задачи
     */
    @GetMapping("/successDeleteTask")
    public String successDeleteTask(Model model) {
/*
        model.addAttribute("user", ControllerUtility.checkUser(session));
*/
        model.addAttribute("message", "Задача успешно удалена.");
        return "message/success";
    }


}
