package pl.coderslab.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("select p from Profile p where p.user = :user")
    List<Profile> findAllByUserId(User user);

    @Query("select p from Profile p where p.workplace.id = :workplaceId and p.user.id = :userId")
    Optional<Profile> findByWorkplaceIdAndUserId(Long workplaceId, Long userId);
}
