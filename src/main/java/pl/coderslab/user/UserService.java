package pl.coderslab.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.role.Role;
import pl.coderslab.role.RoleRepository;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public void saveUser(User user) {
        userRepository.findByUsername(user.getUsername()).orElseGet(() -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(true);
            Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
            user.setRoles(new HashSet<>(Arrays.asList(userRole)));
            return userRepository.save(user);
        });
    }

    public void update(User user) {
        User userInDB = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
        if (user.getEmail() != null) {
            userInDB.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            userInDB.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userInDB);
    }
}
