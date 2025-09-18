package pl.coderslab.profile;

import org.springframework.stereotype.Service;
import pl.coderslab.user.User;
import pl.coderslab.workplaceGroup.WorkplaceGroupRepository;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final WorkplaceGroupRepository workplaceGroupRepository;
    public ProfileService(ProfileRepository profileRepository, WorkplaceGroupRepository workplaceGroupRepository) {
        this.profileRepository = profileRepository;
        this.workplaceGroupRepository = workplaceGroupRepository;
    }

    public Profile getProfileById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    public List<Profile> getAllProfilesByWorkplace(Long workplaceId) {
        return profileRepository.findAllByWorkplaceId(workplaceId);
    }

    public void save(Profile profile) {
        profileRepository.save(profile);
    }

    public List<Profile> getAllProfilesByUser(User user) {
        return profileRepository.findAllByUserId(user);
    }

    public Profile getProfileByWorkplaceIdAndUserId(Long workplaceId, Long userId) {
        return profileRepository.findByWorkplaceIdAndUserId(workplaceId, userId).orElseThrow();
    }

    public void updateProfileWorkplaceGroup(Long profileId, Long groupId) {
        Profile profile = getProfileById(profileId);
        profile.setWorkplaceGroup(workplaceGroupRepository.findById(groupId).orElseThrow());
        profileRepository.save(profile);
    }

    public void deleteProfile(Long profileId) {
        profileRepository.deleteById(profileId);
    }
}