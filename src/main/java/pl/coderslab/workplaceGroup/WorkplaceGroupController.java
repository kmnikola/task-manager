package pl.coderslab.workplaceGroup;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.workplace.WorkplaceService;

import java.util.List;

@RestController
@RequestMapping("/{workplace_id}/groups")
public class WorkplaceGroupController {
    private final WorkplaceGroupService workplaceGroupService;
    private final WorkplaceService workplaceService;
    public WorkplaceGroupController(WorkplaceGroupService workplaceGroupService, WorkplaceService workplaceService) {
        this.workplaceGroupService = workplaceGroupService;
        this.workplaceService = workplaceService;
    }

    @PreAuthorize("@workplaceAccess.canAccessWorkplace(authentication, #workplace_id)")
    @GetMapping("")
    public List<WorkplaceGroup> getWorkplaceGroups(@PathVariable("workplace_id") Long workplace_id) {
        return workplaceGroupService.getWorkplaceGroupsInWorkplace(workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PostMapping("")
    public void addWorkplaceGroup(@RequestBody WorkplaceGroup workplaceGroup, @PathVariable("workplace_id") Long workplace_id) {
        workplaceGroup.setWorkplace(workplaceService.getWorkplaceById(workplace_id));
        workplaceGroupService.addWorkplaceGroup(workplaceGroup, workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@groupAccess.groupBelongsToWorkplace(workplaceGroup, workplace_id)")
    @PutMapping("")
    public void editWorkplaceGroup(@RequestBody WorkplaceGroup workplaceGroup, @PathVariable("workplace_id") Long workplace_id) {
        workplaceGroupService.update(workplaceGroup);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@groupAccess.groupBelongsToWorkplace(group_id, workplace_id)")
    @DeleteMapping("/{group_id}")
    public void deleteWorkplaceGroup(@PathVariable("workplace_id") Long workplace_id, @PathVariable("group_id") Long group_id) {
        workplaceGroupService.deleteById(group_id);
    }
}
