package pl.coderslab.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.coderslab.category.Category;

import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplace.Workplace;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    private Category category;
    @ManyToMany
    @JoinTable(
            name = "task_workplace_group",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "workplace_group_id")
    )
    private List<WorkplaceGroup> workplaceGroups = new ArrayList<>();
    @JsonIgnore
    @ManyToOne
    private Workplace workplace;
}
