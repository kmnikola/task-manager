package pl.coderslab.recurrence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.coderslab.task.Task;
import pl.coderslab.workplace.Workplace;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "recurrences")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Recurrence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Set<DayOfWeek> days;
    private LocalTime time;
    @ManyToOne
    @JsonIgnore
    private Workplace workplace;

    public boolean matches(LocalDateTime dateTime) {
        LocalTime nowTime = dateTime.toLocalTime().withSecond(0).withNano(0);
        LocalTime recurrenceTime = time.withSecond(0).withNano(0);
        return days.contains(dateTime.getDayOfWeek()) && recurrenceTime.equals(nowTime);
    }
}
