package pl.coderslab.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.auth.CurrentUser;

@Controller
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        userService.saveUser(user);
        model.addAttribute("message", "User registered");
        return "auth/login";
    }

    @GetMapping("/account")
    public String getAccount(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        model.addAttribute("user", currentUser.getUser());
        return "account/account";
    }

    @GetMapping("/update-account")
    public String showUpdateForm(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        model.addAttribute("user", currentUser.getUser());
        return "account/update-account";
    }

    @PostMapping("/update-account")
    public String updateAccount(@AuthenticationPrincipal CurrentUser currentUser, @ModelAttribute User userData, Model model) {
        userService.update(currentUser.getUser(), userData);
        model.addAttribute("message", "User updated");
        return "account/account";
    }

    @PostMapping("/remove-account")
    public String removeAccount(@AuthenticationPrincipal CurrentUser currentUser) {
        userService.delete(currentUser.getUser());
        return "redirect:/login?removed";
    }
}
