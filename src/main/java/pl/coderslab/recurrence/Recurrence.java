package pl.coderslab.recurrence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.coderslab.recurrenceSet.RecurrenceSet;
import pl.coderslab.workplace.Workplace;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private DayOfWeek day;
    private LocalTime time;
    @ManyToOne
    @JsonIgnore
    private Workplace workplace;
    @ManyToOne
    @JsonIgnore
    private RecurrenceSet recurrenceSet;

    public boolean matches(LocalDateTime dateTime) {
        LocalTime nowTime = dateTime.toLocalTime().withSecond(0).withNano(0);
        LocalTime recurrenceTime = time.withSecond(0).withNano(0);
        return day.equals(dateTime.getDayOfWeek()) && recurrenceTime.equals(nowTime);
    }
}
