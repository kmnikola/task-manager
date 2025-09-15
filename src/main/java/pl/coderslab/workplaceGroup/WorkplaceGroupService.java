package pl.coderslab.workplaceGroup;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.events.WorkplaceCreatedEvent;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceRepository;
import pl.coderslab.workplace.WorkplaceService;

import java.util.Arrays;
import java.util.List;

@Service
public class WorkplaceGroupService {
    private final WorkplaceGroupRepository workplaceGroupRepository;
    private final WorkplaceService workplaceService;

    public WorkplaceGroupService(WorkplaceGroupRepository workplaceGroupRepository, WorkplaceService workplaceService) {
        this.workplaceGroupRepository = workplaceGroupRepository;
        this.workplaceService = workplaceService;
    }

    public List<WorkplaceGroup> getWorkplaceGroupsInWorkplace(Long workplaceId) {
        return workplaceGroupRepository.findAllByWorkplaceId(workplaceId);
    }

    public WorkplaceGroup getWorkplaceGroupByWorkplaceIdAndName(Long workplaceId, String workplaceGroupName) {
        return workplaceGroupRepository.findWorkplaceGroupByWorkplaceIdAndName(workplaceId, workplaceGroupName).orElseThrow();
    }

    public WorkplaceGroup getById(Long workplaceGroupId) {
        return workplaceGroupRepository.findById(workplaceGroupId).orElseThrow();
    }

    public void addWorkplaceGroup(WorkplaceGroup workplaceGroup, Long workplaceId) {
        workplaceGroup.setWorkplace(workplaceService.getWorkplaceById(workplaceId));
        workplaceGroupRepository.save(workplaceGroup);
    }

    public void update(WorkplaceGroup workplaceGroup) {
        WorkplaceGroup groupInDB = getById(workplaceGroup.getId());
        if (workplaceGroup.getName() != null) {
            groupInDB.setName(workplaceGroup.getName());
        }
        workplaceGroupRepository.save(workplaceGroup);
    }

    public void deleteById(Long workplaceId, Long groupId) {
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        workplace.getWorkplaceGroups().remove(getById(groupId));
        workplaceService.updateWorkplace(workplace);
    }

    @EventListener
    @Transactional
    public void handleWorkplaceCreatedEvent(WorkplaceCreatedEvent event) {
        for (WorkplaceGroup group : event.groups()) {
            addWorkplaceGroup(group, event.workplaceId());
        }
    }
}
