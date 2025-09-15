package pl.coderslab.profile;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.events.WorkplaceCreatedEvent;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.user.User;
import pl.coderslab.workplace.Workplace;

import java.util.List;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public List<Profile> getAllProfiles(User user) {
        return profileRepository.findAllByUserId(user);
    }

    public Profile getProfileByWorkplaceIdAndUserId(Long workplaceId, Long userId) {
        return profileRepository.findByWorkplaceIdAndUserId(workplaceId, userId).orElseThrow();
    }
}