package pl.coderslab.auth;

import org.springframework.stereotype.Component;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceService;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

@Component("groupAccess")
public class WorkplaceGroupAccessEvaluator {
    private final WorkplaceGroupService workplaceGroupService;
    private final WorkplaceService workplaceService;

    public WorkplaceGroupAccessEvaluator(WorkplaceGroupService workplaceGroupService, WorkplaceService workplaceService) {
        this.workplaceGroupService = workplaceGroupService;
        this.workplaceService = workplaceService;
    }

    public boolean groupBelongsToWorkplace(Long groupId, Long workplaceId) {
        return workplaceService.getWorkplaceById(workplaceId).getWorkplaceGroups().contains(workplaceGroupService.getById(groupId));
    }

    public boolean groupBelongsToWorkplace(WorkplaceGroup group, Long workplaceId) {
        return workplaceService.getWorkplaceById(workplaceId).getWorkplaceGroups().contains(group);
    }
}
