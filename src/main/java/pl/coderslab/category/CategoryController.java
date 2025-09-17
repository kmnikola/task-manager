package pl.coderslab.category;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/workplaces/{workplace_id}/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @GetMapping("")
    public String getCategories(@PathVariable("workplace_id") Long workplace_id, Model model) {
        List<Category> categories = categoryService.getAllCategoriesByWorkplaceId(workplace_id);
        model.addAttribute("categories", categories);
        return "categories/list";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @GetMapping("/create")
    public String showAddCategoryForm(@PathVariable("workplace_id") Long workplace_id, Model model) {
        model.addAttribute("category", new Category());
        return "categories/form";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PostMapping("/create")
    public String addCategory(@PathVariable("workplace_id") Long workplace_id, @ModelAttribute Category category) {
        categoryService.addCategory(category, workplace_id);
        return "redirect:/workplaces/" + workplace_id + "/categories";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " +
            "@categoryAccess.categoryBelongsToWorkplace(#category_id, #workplace_id)")
    @PostMapping("/delete")
    public String deleteCategory(@PathVariable("workplace_id") Long workplace_id, @RequestParam("category_id") Long category_id) {
        categoryService.deleteById(workplace_id, category_id);
        return "redirect:/workplaces/" + workplace_id + "/categories";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " +
            "@categoryAccess.categoryBelongsToWorkplace(#category_id, #workplace_id)")
    @GetMapping("/edit")
    public String showEditCategoryForm(@PathVariable("workplace_id") Long workplace_id,
                                       @RequestParam("category_id") Long category_id,
                                       Model model) {
        Category category = categoryService.getById(category_id);
        model.addAttribute("category", category);
        return "categories/edit";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " +
            "@categoryAccess.categoryBelongsToWorkplace(#category.id, #workplace_id)")
    @PostMapping("/edit")
    public String editCategory(@PathVariable("workplace_id") Long workplace_id,
                               @ModelAttribute Category category) {
        categoryService.updateCategory(category);
        return "redirect:/workplaces/" + workplace_id + "/categories";
    }
}
