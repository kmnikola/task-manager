package pl.coderslab.category;

import org.springframework.stereotype.Service;
import pl.coderslab.task.Task;
import pl.coderslab.task.TaskRepository;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceService;

import java.util.List;

@Service
public class CategoryService {
    private final WorkplaceService workplaceService;
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public CategoryService(CategoryRepository categoryRepository, WorkplaceService workplaceService, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.workplaceService = workplaceService;
        this.taskRepository = taskRepository;
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

    public void deleteById(Long workplaceId, Long categoryId) {
        for (Task task : taskRepository.findAllByWorkplaceIdAndCategoryId(workplaceId, categoryId)) {
            task.setCategory(null);
            taskRepository.save(task);
        }
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        workplace.getCategories().remove(getById(categoryId));
        workplaceService.updateWorkplace(workplace);
    }

    public void updateCategory(Category category) {
        Category categoryInDb = getById(category.getId());
        if (category.getName() != null) {
            categoryInDb.setName(category.getName());
        }
        categoryRepository.save(categoryInDb);
    }
}
