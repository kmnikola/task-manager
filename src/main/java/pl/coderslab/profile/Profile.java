package pl.coderslab.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.user.User;
import pl.coderslab.workplace.Workplace;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    private WorkplaceGroup workplaceGroup;
    @JsonIgnore
    @ManyToOne
    private Workplace workplace;
    @JsonIgnore
    @ManyToOne
    private User user;
}
