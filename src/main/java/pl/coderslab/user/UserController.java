package pl.coderslab.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        userService.saveUser(user);
        return "user registered";
    }

    @GetMapping("/account")
    public String getAccount(@AuthenticationPrincipal User user) {
        return user.getUsername();
    }

    @PutMapping("/update-account")
    public String updateAccount(@AuthenticationPrincipal User user, @RequestBody User userData) {
        userService.update(user, userData);
        return "user updated";
    }

    @DeleteMapping("/remove-account")
    public String removeAccount(@AuthenticationPrincipal User user) {
        userService.delete(user);
        return "user removed";
    }
}
