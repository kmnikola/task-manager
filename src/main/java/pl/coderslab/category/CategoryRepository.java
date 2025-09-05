package pl.coderslab.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.workplace.id = :workplaceId")
    List<Category> findAllByWorkplaceId(Long workplaceId);

    @Query("select c from Category c join c.workplaceGroups g where c.workplace.id = :workplaceId and g.id = :groupId")
    List<Category> findAllByWorkplaceIdAndWorkplaceGroupId(Long workplaceId, Long groupId);
}
