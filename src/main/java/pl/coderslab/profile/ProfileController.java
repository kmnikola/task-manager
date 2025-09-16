package pl.coderslab.profile;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.workplaceGroup.WorkplaceGroup;

import java.util.List;

@RestController
@RequestMapping("/workplaces/{workplace_id}/profiles")
public class ProfileController {
    private final ProfileService profileService;
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @GetMapping("")
    public List<ProfileDTO> getProfiles(@PathVariable("workplace_id") Long workplace_id) {
        return profileService.getProfileDTOSByWorkplace(workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@profileAccess.profileBelongsToWorkplace(#profile_id, #workplace_id) && " + "@groupAccess.groupBelongsToWorkplace(#group_id, #workplace_id)")
    @PutMapping("/{profile_id}/set-group/{group_id}")
    public String setWorkplaceGroupToProfile(@PathVariable("workplace_id") Long workplace_id, @PathVariable("profile_id") Long profile_id, @PathVariable("group_id") Long group_id) {
        profileService.updateProfileWorkplaceGroup(profile_id, group_id);
        return "success";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@profileAccess.profileBelongsToWorkplace(#profile_id, #workplace_id)")
    @DeleteMapping("/{profile_id}")
    public void removeProfile(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable("workplace_id") Long workplace_id, @PathVariable("profile_id") Long profile_id) {
        profileService.removeProfileFromWorkplace(profile_id, currentUser.getUser().getId());
    }
}
