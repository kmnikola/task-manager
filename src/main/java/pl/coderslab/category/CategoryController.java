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

    @PreAuthorize("@workplaceAccess.canAccess(authentication, #workplace_id)")
    @GetMapping("")
    public List<Category> getCategories(@PathVariable("workplace_id") Long workplace_id) {
        return categoryService.getAllCategoriesByWorkplaceId(workplace_id);
    }

    @PreAuthorize("@workplaceAccess.belongsToGroup(authentication, #workplace_id, #group_id)")
    @GetMapping("/{group_id}")
    public List<Category> getCategoriesByRole(@PathVariable("workplace_id") Long workplace_id, @PathVariable("group_id") Long group_id) {
        return categoryService.getAllCategoriesByWorkplaceIdAndWorkplaceGroupId(workplace_id, group_id);
    }

    @PreAuthorize("@workplaceAccess.canEdit(authentication, #workplace_id)")
    @PostMapping("")
    public void addCategory(@PathVariable("workplace_id") Long workplace_id, @RequestBody Category category) {
        categoryService.addCategory(category, workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEdit(authentication, #workplace_id)")
    @DeleteMapping("")
    public void deleteCategory(@PathVariable("workplace_id") Long workplace_id, @RequestBody Long category_id) {
        categoryService.deleteById(category_id, workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEdit(authentication, #workplace_id)")
    @PutMapping("")
    public void editCategory(@PathVariable("workplace_id") Long workplace_id, @RequestBody Category category) {
        categoryService.updateCategory(category, workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEdit(authentication, #workplace_id)")
    @PutMapping("/{category_id}/add_group/{group_id}")
    public void addGroupToCategory(@PathVariable("workplace_id") Long workplace_id, @PathVariable("category_id") Long category_id, @PathVariable("group_id") Long group_id) {
        categoryService.addGroupToCategory(group_id, category_id, workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEdit(authentication, #workplace_id)")
    @PutMapping("/{category_id}/remove_group/{group_id}")
    public void removeGroupFromCategory(@PathVariable("workplace_id") Long workplace_id, @PathVariable("category_id") Long category_id, @PathVariable("group_id") Long group_id) {
        categoryService.removeGroupFromCategory(group_id, category_id, workplace_id);
    }
}
