package pl.coderslab.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.auth.CurrentUser;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create-owner")
    public String createOwner() {
        User user = new User();
        user.setUsername("sampleOwner");
        user.setPassword("coderslab");
        userService.saveUser(user);
        return "sampleOwner created";
    }

    @GetMapping("/create-user")
    public String createUser() {
        User user = new User();
        user.setUsername("sampleUser");
        user.setPassword("coderslab");
        userService.saveUser(user);
        return "sampleUser created";
    }

    @GetMapping("/create-user1")
    public String createUser1() {
        User user = new User();
        user.setUsername("sampleUser1");
        user.setPassword("coderslab");
        userService.saveUser(user);
        return "sampleUser1 created";
    }
}
