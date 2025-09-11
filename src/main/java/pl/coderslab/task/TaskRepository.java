package pl.coderslab.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.workplace.Workplace;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByWorkplaceId(Long workplaceId);

    @Query("select t from Task t join t.workplaceGroups g where t.workplace.id = :workplaceId and g.id = :groupId group by t.active")
    List<Task> findAllByWorkplaceIdAndWorkplaceGroupId(Long workplaceId, Long groupId);

    @Query("select t from Task t where t.workplace.id = :workplaceId and t.category.id = :categoryId")
    List<Task> findAllByWorkplaceIdAndCategoryId(Long workplaceId, Long categoryId);

    @Query("select t from Task t join t.recurrences r where r.id = :recurrenceId")
    List<Task> findAllByRecurrenceId(Long recurrenceId);
}
