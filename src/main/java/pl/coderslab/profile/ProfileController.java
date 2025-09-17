package pl.coderslab.profile;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

import java.util.List;

@Controller
@RequestMapping("/workplaces/{workplace_id}/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final WorkplaceGroupService workplaceGroupService;

    public ProfileController(ProfileService profileService, WorkplaceGroupService workplaceGroupService) {
        this.profileService = profileService;
        this.workplaceGroupService = workplaceGroupService;
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @GetMapping("")
    public String listProfiles(@PathVariable("workplace_id") Long workplace_id, Model model) {
        List<Profile> profiles = profileService.getAllProfilesByWorkplace(workplace_id);
        List<WorkplaceGroup> groups = workplaceGroupService.getWorkplaceGroupsInWorkplace(workplace_id);

        model.addAttribute("profiles", profiles);
        model.addAttribute("groups", groups);
        model.addAttribute("workplace_id", workplace_id);

        return "profiles/list";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " +
            "@profileAccess.profileBelongsToWorkplace(#profile_id, #workplace_id) && " +
            "@groupAccess.groupBelongsToWorkplace(#group_id, #workplace_id)")
    @PostMapping("/{profile_id}/set-group")
    public String setWorkplaceGroupToProfile(@PathVariable("workplace_id") Long workplace_id,
                                             @PathVariable("profile_id") Long profile_id,
                                             @RequestParam("group_id") Long group_id) {
        profileService.updateProfileWorkplaceGroup(profile_id, group_id);
        return "redirect:/workplaces/" + workplace_id + "/profiles";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " +
            "@profileAccess.profileBelongsToWorkplace(#profile_id, #workplace_id)")
    @PostMapping("/{profile_id}/delete")
    public String removeProfile(@PathVariable("workplace_id") Long workplace_id,
                                @PathVariable("profile_id") Long profile_id) {
        profileService.removeProfileFromWorkplace(profile_id);
        return "redirect:/workplaces/" + workplace_id + "/profiles";
    }
}