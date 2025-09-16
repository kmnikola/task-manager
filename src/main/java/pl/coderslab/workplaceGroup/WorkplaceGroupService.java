package pl.coderslab.workplaceGroup;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.workplace.Workplace;
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

    public Workplace getWorkplaceById(Long workplaceId) {
        return workplaceRepository.findById(workplaceId).orElseThrow();
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
        if (checkNameExists(workplaceGroup, workplaceId)) {
            workplaceGroup.setWorkplace(getWorkplaceById(workplaceId));
            workplaceGroupRepository.save(workplaceGroup);
        }
    }

    public void update(WorkplaceGroup workplaceGroup, Long workplaceId) {
        WorkplaceGroup groupInDB = getById(workplaceGroup.getId());
        if (checkNameExists(workplaceGroup, workplaceId) && !groupInDB.getName().equals("owner") && !groupInDB.getName().equals("user")) {
            if (workplaceGroup.getName() != null) {
                groupInDB.setName(workplaceGroup.getName());
            }
            workplaceGroupRepository.save(groupInDB);
        }
    }

    private boolean checkNameExists(WorkplaceGroup workplaceGroup, Long workplaceId) {
        List<WorkplaceGroup> groups = getWorkplaceById(workplaceId).getWorkplaceGroups();
        for (WorkplaceGroup group : groups) {
            if (group.getName().equals(workplaceGroup.getName())) {
                return false;
            }
        }
        return true;
    }

    public void deleteById(Long workplaceId, Long groupId) {
        WorkplaceGroup groupInDB = getById(groupId);
        if (!groupInDB.getName().equals("owner") && !groupInDB.getName().equals("user")) {
            Workplace workplace = getWorkplaceById(workplaceId);
            workplace.getWorkplaceGroups().remove(groupInDB);
            workplaceRepository.save(workplace);
        }
    }
}
