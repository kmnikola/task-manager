package pl.coderslab.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.task.Task;
import pl.coderslab.workplace.Workplace;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Task> tasks;
    @ManyToMany
    @JoinTable(
            name = "category_workplace_group",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "workplace_group_id")
    )
    private List<WorkplaceGroup> workplaceGroups;
    @JsonIgnore
    @ManyToOne
    private Workplace workplace;
}
