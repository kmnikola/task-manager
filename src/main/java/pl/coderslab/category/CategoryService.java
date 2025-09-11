package pl.coderslab.category;

import org.springframework.stereotype.Service;
import pl.coderslab.task.TaskRepository;
import pl.coderslab.task.TaskService;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceService;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

import java.util.List;

@Service
public class CategoryService {
    private final WorkplaceService workplaceService;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository, WorkplaceService workplaceService) {
        this.categoryRepository = categoryRepository;
        this.workplaceService = workplaceService;
    }


    public List<Category> getAllCategoriesByWorkplaceId(Long workplaceId) {
        return categoryRepository.findAllByWorkplaceId(workplaceId);
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public void addCategory(Category category, Long workplaceId) {
        category.setWorkplace(workplaceService.getWorkplaceById(workplaceId));
        categoryRepository.save(category);
    }

    public void deleteById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public void updateCategory(Category category) {
        Category categoryInDb = getById(category.getId());
        if (category.getName() != null) {
            categoryInDb.setName(category.getName());
        }
        categoryRepository.save(categoryInDb);
    }
}
