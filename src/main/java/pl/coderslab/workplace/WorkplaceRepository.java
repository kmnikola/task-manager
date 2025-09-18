package pl.coderslab.workplace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.profile.Profile;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
    @Query("select w from Workplace w join w.profiles p where p.id = :profileId")
    Optional<Workplace> findByProfileId(Long profileId);

    @Query("select w from Workplace w where w.user.id = :userId")
    List<Workplace> getWorkplacesByUserId(Long userId);
}
