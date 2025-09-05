package pl.coderslab.workplace;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkplaceDTO {
    private Long id;
    private String name;
    private String[] categories;
    private String[] tasks;
    private String[] groups;
    private String[] profiles;
}
