package pl.coderslab.workplaceGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkplaceGroupRepository extends JpaRepository<WorkplaceGroup, Long> {
    @Query("select g from WorkplaceGroup g where g.workplace.id = :workplaceId and g.name = :name")
    Optional<WorkplaceGroup> findWorkplaceGroupByWorkplaceIdAndName(Long workplaceId, String name);

    @Query("select g from WorkplaceGroup g where g.workplace.id = :workplaceId")
    List<WorkplaceGroup> findAllByWorkplaceId(Long workplaceId);
}
