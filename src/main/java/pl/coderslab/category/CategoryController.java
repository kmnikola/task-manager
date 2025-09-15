package pl.coderslab.category;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/{workplace_id}/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("@workplaceAccess.canAccessWorkplace(authentication, #workplace_id)")
    @GetMapping("")
    public List<Category> getCategories(@PathVariable("workplace_id") Long workplace_id) {
        return categoryService.getAllCategoriesByWorkplaceId(workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PostMapping("")
    public void addCategory(@PathVariable("workplace_id") Long workplace_id, @RequestBody Category category) {
        categoryService.addCategory(category, workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@categoryAccess.categoryBelongsToWorkplace(#category_id, #workplace_id)")
    @DeleteMapping("/{category_id}")
    public void deleteCategory(@PathVariable("workplace_id") Long workplace_id, @PathVariable("category_id") Long category_id) {
        categoryService.deleteById(workplace_id, category_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@categoryAccess.categoryBelongsToWorkplace(#category, #workplace_id)")
    @PutMapping("")
    public void editCategory(@PathVariable("workplace_id") Long workplace_id, @RequestBody Category category) {
        categoryService.updateCategory(category);
    }
}
