package pl.coderslab.recurrenceSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurrenceSetRepository extends JpaRepository<RecurrenceSet, Long> {
    @Query("select rs from RecurrenceSet rs where rs.workplace.id = :workplaceId")
    List<RecurrenceSet> getAllByWorkplaceId(Long workplaceId);

    @Query("select rs from RecurrenceSet rs join rs.recurrences r where r.id = :recurrenceId")
    RecurrenceSet findByRecurrenceId(Long recurrenceId);
}
