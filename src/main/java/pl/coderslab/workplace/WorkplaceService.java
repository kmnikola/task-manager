package pl.coderslab.workplace;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.profile.Profile;
import pl.coderslab.profile.ProfileService;
import pl.coderslab.user.UserRepository;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.user.User;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final ProfileService profileService;
    private final WorkplaceGroupService workplaceGroupService;
    private final UserRepository userRepository;

    public WorkplaceService(WorkplaceRepository workplaceRepository, ProfileService profileService, WorkplaceGroupService workplaceGroupService, UserRepository userRepository) {
        this.workplaceRepository = workplaceRepository;
        this.profileService = profileService;
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
        List<Workplace> workplacesOfUser = workplaceRepository.getWorkplacesByUserId(user.getId());
        for (Workplace workplace : workplacesOfUser) {
            workplaces.add(workplace);
        }
        return workplaces;
    }

    public Workplace getWorkplaceById(Long workplaceId) {
        return workplaceRepository.findById(workplaceId).orElseThrow(() -> new EntityNotFoundException("Workplace with id " + workplaceId + " not found"));
    }

    public void createWorkplace(CurrentUser currentUser, Workplace workplace) {
        User user = userRepository.getUserById(currentUser.getUser().getId());
        workplace.setUser(user);
        workplaceRepository.save(workplace);
        user.getWorkplaces().add(workplace);
        userRepository.save(user);

        workplaceGroupService.addWorkplaceGroup(WorkplaceGroup.builder()
                    .name("user")
                    .build(), workplace.getId());
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

    public void deleteWorkplace(Long workplaceId, CurrentUser currentUser) {
        Workplace workplace = getWorkplaceById(workplaceId);
        User user = userRepository.getUserById(currentUser.getUser().getId());
        user.getWorkplaces().remove(workplace);
        userRepository.save(user);
        workplaceRepository.deleteById(workplaceId);
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