package pl.coderslab;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.role.Role;
import pl.coderslab.role.RoleRepository;
import pl.coderslab.user.User;
import pl.coderslab.user.UserRepository;

import java.util.Set;

@SpringBootApplication
public class TaskmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskmanagerApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner seed(RoleRepository roles, UserRepository users, PasswordEncoder pe) {
        return args -> {
            Role admin = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(new Role("ROLE_ADMIN")));
            Role user  = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(new Role("ROLE_USER")));
        };
    }

}
