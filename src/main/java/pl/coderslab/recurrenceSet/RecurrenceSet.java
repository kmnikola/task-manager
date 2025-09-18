package pl.coderslab.recurrenceSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.coderslab.recurrence.Recurrence;
import pl.coderslab.workplace.Workplace;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recurrence_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurrenceSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "recurrenceSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recurrence> recurrences = new ArrayList<>();
    @ManyToOne
    @JsonIgnore
    private Workplace workplace;
}
