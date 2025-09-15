package pl.coderslab.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.coderslab.profile.Profile;
import pl.coderslab.profile.ProfileService;
import pl.coderslab.workplace.WorkplaceService;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

@Component("workplaceAccess")
public class WorkplaceAccessEvaluator {
    private final WorkplaceService workplaceService;
    private final WorkplaceGroupService workplaceGroupService;
    private final ProfileService profileService;

    public WorkplaceAccessEvaluator(WorkplaceService workplaceService, WorkplaceGroupService workplaceGroupService, ProfileService profileService) {
        this.workplaceService = workplaceService;
        this.workplaceGroupService = workplaceGroupService;
        this.profileService = profileService;
    }

    public boolean canAccessWorkplace(Authentication auth, Long workplaceId) {
        CurrentUser currentUser = (CurrentUser) auth.getPrincipal();
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
        if (canAccessWorkplace(auth, workplaceId)) {
            Profile profile = profileService.getProfileByWorkplaceIdAndUserId(workplaceId, currentUser.getUser().getId());
            return profile.getWorkplaceGroup() == workplaceGroupService.getWorkplaceGroupByWorkplaceIdAndName(workplaceId, "owner");
        }
        return false;
    }
}