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
    private final WorkplaceGroupService workplaceGroupService;
    private final TaskRepository taskRepository;
    public CategoryService(CategoryRepository categoryRepository, WorkplaceService workplaceService, WorkplaceGroupService workplaceGroupService, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.workplaceService = workplaceService;
        this.workplaceGroupService = workplaceGroupService;
        this.taskRepository = taskRepository;
    }


    public List<Category> getAllCategoriesByWorkplaceId(Long workplaceId) {
        return categoryRepository.findAllByWorkplaceId(workplaceId);
    }

    public List<Category> getAllCategoriesByWorkplaceIdAndWorkplaceGroupId(Long workplaceId, Long groupId) {
        return categoryRepository.findAllByWorkplaceIdAndWorkplaceGroupId(workplaceId, groupId);
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public void addCategory(Category category, Long workplaceId) {
        category.setWorkplace(workplaceService.getWorkplaceById(workplaceId));
        categoryRepository.save(category);
    }

    public void deleteById(Long categoryId, Long workplace_id) {
        if (workplaceService.getWorkplaceById(workplace_id).getCategories().contains(getById(categoryId))) {
            categoryRepository.deleteById(categoryId);
        }
    }

    public void updateCategory(Category category, Long workplace_id) {
        if (workplaceService.getWorkplaceById(workplace_id).getCategories().contains(category)) {
            Category categoryInDb = getById(category.getId());
            if (category.getName() != null) {
                categoryInDb.setName(category.getName());
            }
            categoryRepository.save(categoryInDb);
        }
    }

    public void addGroupToCategory(Long groupId, Long categoryId, Long workplaceId) {
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        Category category = getById(categoryId);
        WorkplaceGroup group = workplaceGroupService.getById(groupId);
        if (workplace.getCategories().contains(category) && workplace.getWorkplaceGroups().contains(group)) {
            category.getWorkplaceGroups().add(group);
            categoryRepository.save(category);
            category.getTasks().forEach(task -> {
                task.getWorkplaceGroups().add(group);
                taskRepository.save(task);
            });
        }
    }

    public void removeGroupFromCategory(Long groupId, Long categoryId, Long workplaceId) {
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        Category category = getById(categoryId);
        WorkplaceGroup group = workplaceGroupService.getById(groupId);
        if (workplace.getCategories().contains(category) && workplace.getWorkplaceGroups().contains(group)) {
            category.getWorkplaceGroups().remove(group);
            categoryRepository.save(category);
            category.getTasks().forEach(task -> {
                task.getWorkplaceGroups().remove(group);
                taskRepository.save(task);
            });
        }
    }
}
