package pl.coderslab.workplaceGroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.coderslab.profile.Profile;
import pl.coderslab.workplace.Workplace;

import java.util.List;

@Entity
@Table(name = "workplace_groups")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkplaceGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "workplaceGroup")
    private List<Profile> profiles;
    @JsonIgnore
    @ManyToOne
    private Workplace workplace;
}