package pl.coderslab.profile;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private Long id;
    private String username;
    private String workplaceGroup;
}
