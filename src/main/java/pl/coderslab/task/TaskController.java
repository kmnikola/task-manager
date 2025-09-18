package pl.coderslab.task;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.access.WorkplaceAccessEvaluator;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.category.Category;
import pl.coderslab.category.CategoryService;
import pl.coderslab.recurrenceSet.RecurrenceSet;
import pl.coderslab.recurrenceSet.RecurrenceSetService;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

import java.util.List;

@Controller
@RequestMapping("/workplaces/{workplace_id}/tasks")
public class TaskController {
    private final TaskService taskService;
    private final WorkplaceAccessEvaluator workplaceAccess;
    private final WorkplaceGroupService workplaceGroupService;
    private final CategoryService categoryService;
    private final RecurrenceSetService recurrenceSetService;

    public TaskController(TaskService taskService, WorkplaceAccessEvaluator workplaceAccess, WorkplaceGroupService workplaceGroupService, CategoryService categoryService, RecurrenceSetService recurrenceSetService) {
        this.taskService = taskService;
        this.workplaceAccess = workplaceAccess;
        this.workplaceGroupService = workplaceGroupService;
        this.categoryService = categoryService;
        this.recurrenceSetService = recurrenceSetService;
    }

    @PreAuthorize("@workplaceAccess.canAccessWorkplace(authentication, #workplace_id)")
    @GetMapping("")
    public String getTasks(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable("workplace_id") Long workplace_id, Model model) {
        boolean canEdit = workplaceAccess.canEditWorkplace(currentUser, workplace_id);

        model.addAttribute("canEdit", canEdit);
        model.addAttribute("workplace_id", workplace_id);

        if (canEdit) {
            List<Task> tasks = taskService.getAllTasksByWorkplaceId(workplace_id);
            model.addAttribute("task", new Task());
            model.addAttribute("tasks", tasks);
            model.addAttribute("groups", workplaceGroupService.getWorkplaceGroupsInWorkplace(workplace_id));
            model.addAttribute("categories", categoryService.getAllCategoriesByWorkplaceId(workplace_id));
            model.addAttribute("recurrenceSets", recurrenceSetService.getAllRecurrenceSets(workplace_id));
        } else {
            List<Task> tasks = taskService.getAllTasksByWorkplaceIdAndUser(workplace_id, currentUser);
            model.addAttribute("tasksByCategory", taskService.getTasksByCategories(workplace_id, tasks));
        }
        return "tasks/list";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PostMapping("/create")
    public String addTask(@PathVariable("workplace_id") Long workplace_id, @ModelAttribute Task task, @RequestParam(value = "category_id", required = false) Long categoryId, @RequestParam(value = "recurrence_set_id", required = false) Long recurrenceSetId, @RequestParam(value = "group_ids", required = false) List<Long> groupIds) {
        if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            task.setCategory(category);
        }
        if (recurrenceSetId != null) {
            RecurrenceSet recurrenceSet = recurrenceSetService.getRecurrenceSetById(recurrenceSetId);
            task.setRecurrenceSet(recurrenceSet);
        }
        if (groupIds != null) {
            for (Long groupId : groupIds) {
                WorkplaceGroup group = workplaceGroupService.getById(groupId);
                task.getWorkplaceGroups().add(group);
            }
        }

        taskService.addTaskToWorkplace(task, workplace_id);
        return "redirect:/workplaces/{workplace_id}/tasks";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @PostMapping("/delete")
    public String deleteTask(@PathVariable("workplace_id") Long workplace_id, @RequestParam("task_id") Long task_id) {
        taskService.deleteById(workplace_id, task_id);
        return "redirect:/workplaces/{workplace_id}/tasks";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @GetMapping("/{task_id}/edit")
    public String showEditForm(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id, Model model) {
        model.addAttribute("task", taskService.getTaskById(task_id));
        return "tasks/edit";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task.getId(), #workplace_id)")
    @PostMapping("/edit")
    public String editTask(@PathVariable("workplace_id") Long workplace_id, @ModelAttribute Task task) {
        taskService.updateTask(task);
        return "redirect:/workplaces/{workplace_id}/tasks";
    }

    @PreAuthorize("@workplaceAccess.canAccessWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @PostMapping("/toggle-active")
    public String toggleTaskActive(@PathVariable("workplace_id") Long workplace_id, @RequestParam("task_id") Long task_id) {
        taskService.toggleActive(task_id);
        return "redirect:/workplaces/{workplace_id}/tasks";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id) && " + "@groupAccess.groupBelongsToWorkplace(#group_id, #workplace_id)")
    @PostMapping("/add_group")
    public String addGroupToTask(@PathVariable("workplace_id") Long workplace_id, @RequestParam("task_id") Long task_id, @RequestParam("group_id") Long group_id) {
        taskService.addGroupToTask(group_id, task_id);
        return "redirect:/workplaces/{workplace_id}/tasks";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id) && " + "@groupAccess.groupBelongsToWorkplace(#group_id, #workplace_id)")
    @PostMapping("/remove_group")
    public String removeGroupFromTask(@PathVariable("workplace_id") Long workplace_id, @RequestParam("task_id") Long task_id, @RequestParam("group_id") Long group_id) {
        taskService.removeGroupFromTask(group_id, task_id);
        return "redirect:/workplaces/{workplace_id}/tasks";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id) && " + "@categoryAccess.categoryBelongsToWorkplace(#category_id, #workplace_id)")
    @PostMapping("/set_category")
    public String addCategoryToTask(@PathVariable("workplace_id") Long workplace_id, @RequestParam("task_id") Long task_id, @RequestParam("category_id") Long category_id) {
        taskService.setCategoryToTask(category_id, task_id);
        return "redirect:/workplaces/{workplace_id}/tasks";

    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @PostMapping("/remove_category")
    public String removeCategoryFromTask(@PathVariable("workplace_id") Long workplace_id, @RequestParam("task_id") Long task_id) {
        taskService.removeCategoryFromTask(task_id);
        return "redirect:/workplaces/{workplace_id}/tasks";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @PostMapping("/set_recurrence_set")
    public String addRecurrenceSetToTask(@PathVariable("workplace_id") Long workplace_id, @RequestParam("task_id") Long task_id, @RequestParam("recurrence_set_id") Long recurrence_set_id) {
        RecurrenceSet recurrenceSet = recurrenceSetService.getRecurrenceSetById(recurrence_set_id);
        taskService.setRecurrenceSetToTask(task_id, recurrenceSet);
        return "redirect:/workplaces/{workplace_id}/tasks";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @PostMapping("/remove_recurrence_set")
    public String removeRecurrenceSetFromTask(@PathVariable("workplace_id") Long workplace_id, @RequestParam("task_id") Long task_id) {
        taskService.removeRecurrenceSetFromTask(task_id);
        return "redirect:/workplaces/{workplace_id}/tasks";
    }
}
