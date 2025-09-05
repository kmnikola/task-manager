package pl.coderslab.profile;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    ProfileService profileService;
    ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
}