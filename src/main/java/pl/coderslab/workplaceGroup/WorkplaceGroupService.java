package pl.coderslab.workplaceGroup;

import org.springframework.stereotype.Service;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceService;

import java.util.Arrays;
import java.util.List;

@Service
public class WorkplaceGroupService {
    private final WorkplaceGroupRepository workplaceGroupRepository;
    public WorkplaceGroupService(WorkplaceGroupRepository workplaceGroupRepository) {
        this.workplaceGroupRepository = workplaceGroupRepository;
    }

    public List<WorkplaceGroup> getWorkplaceGroups(Long workplaceId) {
        return workplaceGroupRepository.findAllByWorkplaceId(workplaceId);
    }

    public WorkplaceGroup getWorkplaceGroupByWorkplaceIdAndName(Long workplaceId, String workplaceGroupName) {
        return workplaceGroupRepository.findWorkplaceGroupByWorkplaceIdAndName(workplaceId, workplaceGroupName).orElseThrow();
    }

    public WorkplaceGroup getById(Long workplaceGroupId) {
        return workplaceGroupRepository.findById(workplaceGroupId).orElseThrow();
    }

    public void addWorkplaceGroup(WorkplaceGroup workplaceGroup, Long workplaceId) {
        save(workplaceGroup);
    }

    public void save(WorkplaceGroup workplaceGroup) {
        workplaceGroupRepository.save(workplaceGroup);
    }

    public List<WorkplaceGroup> createInitialWorkplaceGroups(Workplace workplace) {
        return Arrays.asList(
                WorkplaceGroup.builder()
                        .name("owner")
                        .workplace(workplace)
                        .build(),
                WorkplaceGroup.builder()
                        .name("user")
                        .workplace(workplace)
                        .build()
        );
    }

    public void update(WorkplaceGroup workplaceGroup) {
        WorkplaceGroup groupInDB = getById(workplaceGroup.getId());
        if (workplaceGroup.getName() != null) {
            groupInDB.setName(workplaceGroup.getName());
        }
        save(groupInDB);
    }

    public void deleteById(Long groupId) {
        workplaceGroupRepository.deleteById(groupId);
    }
}
