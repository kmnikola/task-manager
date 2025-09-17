package pl.coderslab.access;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.profile.Profile;
import pl.coderslab.profile.ProfileService;
import pl.coderslab.user.User;
import pl.coderslab.user.UserRepository;
import pl.coderslab.user.UserService;
import pl.coderslab.workplace.WorkplaceService;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

@Component("workplaceAccess")
public class WorkplaceAccessEvaluator {
    private final WorkplaceService workplaceService;
    private final WorkplaceGroupService workplaceGroupService;
    private final ProfileService profileService;
    private final UserRepository userRepository;

    public WorkplaceAccessEvaluator(WorkplaceService workplaceService, WorkplaceGroupService workplaceGroupService, ProfileService profileService, UserRepository userRepository) {
        this.workplaceService = workplaceService;
        this.workplaceGroupService = workplaceGroupService;
        this.profileService = profileService;
        this.userRepository = userRepository;
    }

    public boolean canAccessWorkplace(Authentication auth, Long workplaceId) {
        CurrentUser currentUser = (CurrentUser) auth.getPrincipal();
        return workplaceService.getAllWorkplaces(currentUser).contains(workplaceService.getWorkplaceById(workplaceId));
    }

    public boolean canAccessWorkplace(CurrentUser currentUser, Long workplaceId) {
        return workplaceService.getAllWorkplaces(currentUser).contains(workplaceService.getWorkplaceById(workplaceId));
    }

    public boolean belongsToGroup(Authentication auth, Long workplaceId, Long workplaceGroupId) {
        CurrentUser currentUser = (CurrentUser) auth.getPrincipal();
        if (canAccessWorkplace(auth, workplaceId)) {
            Profile profile = profileService.getProfileByWorkplaceIdAndUserId(workplaceId, currentUser.getUser().getId());
            return profile.getWorkplaceGroup() == workplaceGroupService.getById(workplaceGroupId) || profile.getWorkplaceGroup().getName().equals("owner");
        }
        return false;
    }

    public boolean canEditWorkplace(Authentication auth, Long workplaceId) {
        CurrentUser currentUser = (CurrentUser) auth.getPrincipal();
        User user = userRepository.getUserById(currentUser.getUser().getId());
        return user.getWorkplaces().contains(workplaceService.getWorkplaceById(workplaceId));
    }

    public boolean canEditWorkplace(CurrentUser currentUser, Long workplaceId) {
        User user = userRepository.getUserById(currentUser.getUser().getId());
        return user.getWorkplaces().contains(workplaceService.getWorkplaceById(workplaceId));

    }
}