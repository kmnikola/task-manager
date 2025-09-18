package pl.coderslab.access;

import org.springframework.stereotype.Component;
import pl.coderslab.profile.ProfileService;

@Component("profileAccess")
public class ProfileAccessEvaluator {
    private final ProfileService profileService;
    public ProfileAccessEvaluator(ProfileService profileService) {
        this.profileService = profileService;
    }

    public boolean profileBelongsToWorkplace(Long profile_id, Long workplace_id) {
        return profileService.getAllProfilesByWorkplace(workplace_id).contains(profileService.getProfileById(profile_id));
    }
}
