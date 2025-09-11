package pl.coderslab.auth;

import org.springframework.stereotype.Component;
import pl.coderslab.category.Category;
import pl.coderslab.category.CategoryService;
import pl.coderslab.workplace.WorkplaceService;

@Component("categoryAccess")
public class CategoryAccessEvaluator {
    private final CategoryService categoryService;
    private final WorkplaceService workplaceService;
    public CategoryAccessEvaluator(CategoryService categoryService, WorkplaceService workplaceService) {
        this.categoryService = categoryService;
        this.workplaceService = workplaceService;
    }

    public boolean categoryBelongsToWorkplace(Long categoryId, Long workplaceId) {
        return workplaceService.getWorkplaceById(workplaceId).getCategories().contains(categoryService.getById(categoryId));
    }

    public boolean categoryBelongsToWorkplace(Category category, Long workplaceId) {
        return workplaceService.getWorkplaceById(workplaceId).getCategories().contains(category);
    }
}
