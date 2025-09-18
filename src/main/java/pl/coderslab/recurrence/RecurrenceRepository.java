package pl.coderslab.recurrence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurrenceRepository extends JpaRepository<Recurrence, Long> {
    List<Recurrence> getAllByWorkplaceId(Long workplaceId);

    @Query("select r from Recurrence r where r.recurrenceSet.id = :recurrenceSetId order by r.day, r.time")
    List<Recurrence> getAllByRecurrenceSetId(Long recurrenceSetId);
}
