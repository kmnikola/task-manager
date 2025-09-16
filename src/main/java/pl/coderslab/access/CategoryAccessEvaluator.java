package pl.coderslab.access;

import org.springframework.stereotype.Component;
import pl.coderslab.category.Category;
import pl.coderslab.category.CategoryService;

@Component("categoryAccess")
public class CategoryAccessEvaluator {
    private final CategoryService categoryService;
    public CategoryAccessEvaluator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public boolean categoryBelongsToWorkplace(Long categoryId, Long workplaceId) {
        return categoryService.getById(categoryId).getWorkplace().getId().equals(workplaceId);
    }

    public boolean categoryBelongsToWorkplace(Category category, Long workplaceId) {
        return category.getWorkplace().getId().equals(workplaceId);
    }
}
