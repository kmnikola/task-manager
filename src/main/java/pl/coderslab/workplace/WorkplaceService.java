package pl.coderslab.workplace;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.profile.Profile;
import pl.coderslab.profile.ProfileService;
import pl.coderslab.user.UserRepository;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplaceGroup.WorkplaceGroupRepository;
import pl.coderslab.user.User;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ProfileService profileService;
    private final WorkplaceGroupService workplaceGroupService;
    private final UserRepository userRepository;

    public WorkplaceService(WorkplaceRepository workplaceRepository, ProfileService profileService, ApplicationEventPublisher eventPublisher, WorkplaceGroupService workplaceGroupService, UserRepository userRepository) {
        this.workplaceRepository = workplaceRepository;
        this.profileService = profileService;
        this.eventPublisher = eventPublisher;
        this.workplaceGroupService = workplaceGroupService;
        this.userRepository = userRepository;
    }

    public List<Workplace> getAllWorkplaces(CurrentUser currentUser) {
        User user = currentUser.getUser();
        List<Profile> profiles = profileService.getAllProfilesByUser(user);
        List<Workplace> workplaces = new ArrayList<>();
        for (Profile profile : profiles) {
            workplaces.add(workplaceRepository.findByProfileId(profile.getId()).orElse(null));
        }
        return workplaces;
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
                        .canEdit(true)
                        .build(),
                WorkplaceGroup.builder()
                        .name("user")
                        .build()
        );
        for (WorkplaceGroup group : groups) {
            workplaceGroupService.addWorkplaceGroup(group, workplace.getId());
        }
        //Create a new profile for the owner
        Profile profile = Profile.builder()
                .user(user)
                .workplace(workplace)
                .workplaceGroup(groups.get(0))
                .build();
        profileService.save(profile);
    }

    public void editWorkplace(Workplace workplace) {
        Workplace workplaceInDB = getWorkplaceById(workplace.getId());
        if (workplace.getName() != null) {
            workplaceInDB.setName(workplace.getName());
        }
        workplaceRepository.save(workplaceInDB);
    }

    public void updateWorkplace(Workplace workplace) {
        workplaceRepository.save(workplace);
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

    public void leaveWorkplace(CurrentUser currentUser, Long workplaceId) {
        User user = currentUser.getUser();
        Profile profile = profileService.getProfileByWorkplaceIdAndUserId(workplaceId, user.getId());
        profileService.deleteProfile(profile.getId());
    }
}