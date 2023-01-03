package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;
import ru.job4j.todo.util.ControllerUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Принимает запрос на отображение вида добавления пользователя
     * @param model модель вида
     * @param session сессия подключения
     * @return назнвание шаблона вида добавления пользователя
     */
    @GetMapping("/formAdd")
    public String addUser(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        return "user/add";
    }

    /**
     * Производит добавление пользователя в систему
     * @param user сформированный объект на основе введенных пользователем данных
     * @param session сессия подключения
     * @return перенаправляет на страницу /successRegistration при успешной регистрации,
     * и на страницу /failRegistration при неудачной регистрации
     */
    @PostMapping("/registration")
    public String registration(HttpSession session, Model model, @ModelAttribute User user) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message", "Пользователь с таким логином уже существует.");
            return "message/fail";
        }
        model.addAttribute("message", "Пользователь успешно зарегистрирован.");
        return "message/success";
    }

    /**
     * Принимает запрос на отображение вида авторизации пользователя
     * @param model модель вида
     * @param fail Параметр отвечающий за проверку верности введенного логина и пароля, (false - по умолчанию,
     *             true - отображается сообщение о неверно введенном логине или пароле)
     * @param session сессия подключения
     * @return название шаблона для авторизации пользователя
     */
    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("fail", fail != null);
        return "user/login";
    }

    /**
     * Принимает запрос и проводит аутентификацию пользователя
     * @param user сфоримрованный объект User на основе введенных данных пользователем
     * @param req запрос сформированный браузером
     * @return В случае успешной аутентификации предоставляет права пользования сайтом и перенаправляет на вид
     * главной страницы, в случае провала аутентификации переводит параметр fail в значение true и перенаправляет на
     * вид авторизации с отображением сообщения о неверно введенными данными пользователя
     */
    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findByLoginAndPassword(
                user.getLogin(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/users/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/index";
    }

    /**
     * Принимает запрос на выход пользователя из системы. Очищает сессию от всех атрибутов
     * @param session сессия подключения
     * @return перенаправляет на главную страницу
     */
    @GetMapping ("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/loginPage";
    }
}
