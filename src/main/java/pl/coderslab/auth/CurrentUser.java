package pl.coderslab.auth;

import org.springframework.security.core.GrantedAuthority;
import pl.coderslab.user.User;

import java.util.Collection;

public class CurrentUser extends org.springframework.security.core.userdetails.User {
    private final User user;
    public CurrentUser(String username, String password,
                       Collection<? extends GrantedAuthority> authorities,
                       User user) {
        super(username, password, authorities);
        this.user = user;
    }
    public User getUser() {return user;}
}
