package pl.coderslab.workplaceGroup;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.events.WorkplaceCreatedEvent;
import pl.coderslab.workplace.WorkplaceRepository;
import pl.coderslab.workplace.WorkplaceService;

import java.util.Arrays;
import java.util.List;

@Service
public class WorkplaceGroupService {
    private final WorkplaceGroupRepository workplaceGroupRepository;
    private final WorkplaceRepository workplaceRepository;

    public WorkplaceGroupService(WorkplaceGroupRepository workplaceGroupRepository, WorkplaceRepository workplaceRepository) {
        this.workplaceGroupRepository = workplaceGroupRepository;
        this.workplaceRepository = workplaceRepository;
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
        workplaceGroup.setWorkplace(workplaceRepository.findById(workplaceId).orElseThrow());
        workplaceGroupRepository.save(workplaceGroup);
    }

    public void update(WorkplaceGroup workplaceGroup) {
        WorkplaceGroup groupInDB = getById(workplaceGroup.getId());
        if (workplaceGroup.getName() != null) {
            groupInDB.setName(workplaceGroup.getName());
        }
        workplaceGroupRepository.save(workplaceGroup);
    }

    public void deleteById(Long groupId) {
        workplaceGroupRepository.deleteById(groupId);
    }

    @EventListener
    @Transactional
    public void handleWorkplaceCreatedEvent(WorkplaceCreatedEvent event) {
        for (WorkplaceGroup group : event.groups()) {
            addWorkplaceGroup(group, event.workplaceId());
        }
    }
}
