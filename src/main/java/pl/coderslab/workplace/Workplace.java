package pl.coderslab.workplace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.coderslab.category.Category;
import pl.coderslab.profile.Profile;
import pl.coderslab.recurrence.Recurrence;
import pl.coderslab.user.User;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.task.Task;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workplaces")
@Getter
@Setter
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @ManyToOne
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "workplace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profile> profiles = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "workplace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "workplace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "workplace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkplaceGroup> workplaceGroups = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "workplace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recurrence> recurrences = new ArrayList<>();
}