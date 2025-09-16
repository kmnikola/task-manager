package pl.coderslab.access;

import org.springframework.stereotype.Component;
import pl.coderslab.user.User;
import pl.coderslab.user.UserService;

@Component("accountAccess")
public class AccountAccessEvaluator {
    private final UserService userService;
    public AccountAccessEvaluator(UserService userService) {
        this.userService = userService;
    }
}
