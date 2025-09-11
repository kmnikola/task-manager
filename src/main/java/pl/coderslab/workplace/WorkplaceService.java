package pl.coderslab.workplace;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.events.WorkplaceCreatedEvent;
import pl.coderslab.profile.Profile;
import pl.coderslab.profile.ProfileService;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;
import pl.coderslab.user.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceGroupService workplaceGroupService;
    private final ApplicationEventPublisher eventPublisher;
    private final ProfileService profileService;

    public WorkplaceService(WorkplaceRepository workplaceRepository, WorkplaceGroupService workplaceGroupService, ProfileService profileService, ApplicationEventPublisher eventPublisher) {
        this.workplaceRepository = workplaceRepository;
        this.workplaceGroupService = workplaceGroupService;
        this.profileService = profileService;
        this.eventPublisher = eventPublisher;
    }

    public List<Workplace> getAllWorkplaces(CurrentUser currentUser) {
        User user = currentUser.getUser();
        List<Profile> profiles = profileService.getAllProfiles(user);
        List<Workplace> workplaces = new ArrayList<>();
        for (Profile profile : profiles) {
            workplaces.add(workplaceRepository.findByProfileId(profile.getId()).orElse(null));
        }
        return workplaces;
    }

    public Workplace getWorkplaceById(CurrentUser currentUser, Long id) {
        User user = currentUser.getUser();
        return workplaceRepository.findByIdAndUserId(id, user.getId()).orElseThrow();
    }

    public Workplace getWorkplaceById(Long workplaceId) {
        return workplaceRepository.findById(workplaceId).orElseThrow();
    }

    public void createWorkplace(CurrentUser currentUser, Workplace workplace) {
        workplaceRepository.save(workplace);
        User user = currentUser.getUser();
        //Create initial roles
        List<WorkplaceGroup> groups = Arrays.asList(
                WorkplaceGroup.builder()
                        .name("owner")
                        .build(),
                WorkplaceGroup.builder()
                        .name("user")
                        .build()
        );

        eventPublisher.publishEvent(
                new WorkplaceCreatedEvent(user, groups, workplace.getId())
        );
        //Create a new profile for the user
        Profile profile = profileService.createInitialWorkplaceProfile(user, workplace, groups.get(0));
        profileService.save(profile);
    }

    public void updateWorkplace(Workplace workplace) {
        Workplace workplaceInDB = workplaceRepository.findById(workplace.getId()).orElseThrow();
        if (workplace.getName() != null) {
            workplaceInDB.setName(workplace.getName());
        }
        workplaceRepository.save(workplaceInDB);
    }

    public void deleteWorkplace(Long id) {
        workplaceRepository.deleteById(id);
    }

    public void joinWorkplace(CurrentUser currentUser, Long workplaceId) {
        User user = currentUser.getUser();
        WorkplaceGroup userGroup = workplaceGroupService.getWorkplaceGroupByWorkplaceIdAndName(workplaceId, "user");
        Workplace workplace = getWorkplaceById(workplaceId);
        profileService.save(Profile.builder()
                .user(user)
                .workplace(workplace)
                .workplaceGroup(userGroup)
                .build());
    }
}