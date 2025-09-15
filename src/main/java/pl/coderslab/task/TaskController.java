package pl.coderslab.task;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/{workplace_id}/tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("@workplaceAccess.canAccessWorkplace(authentication, #workplace_id)")
    @GetMapping("")
    public List<Task> getTasks(@PathVariable("workplace_id") Long workplace_id) {
        return taskService.getAllTasksByWorkplaceId(workplace_id);
    }

    @PreAuthorize("@workplaceAccess.belongsToGroup(authentication, #workplace_id, #group_id)")
    @GetMapping("/{group_id}")
    public List<Task> getTasksByGroup(@PathVariable("workplace_id") Long workplace_id, @PathVariable("group_id") Long group_id) {
        return taskService.getAllTasksByWorkplaceIdAndWorkplaceGroupId(workplace_id, group_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PostMapping("")
    public void addTask(@PathVariable("workplace_id") Long workplace_id, @RequestBody Task task) {
        taskService.addTaskToWorkplace(task, workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @DeleteMapping("/{task_id}")
    public void deleteTask(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id) {
        taskService.deleteById(workplace_id, task_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task, #workplace_id)")
    @PutMapping("")
    public void editTask(@PathVariable("workplace_id") Long workplace_id, @RequestBody Task task) {
        taskService.updateTask(task);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @PutMapping("/{task_id}/toggle-active")
    public void toggleTaskActive(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id) {
        taskService.toggleActive(task_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id) && " + "@groupAccess.groupBelongsToWorkplace(#group_id, #workplace_id)")
    @PutMapping("/{task_id}/add_group/{group_id}")
    public void addGroupToTask(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id, @PathVariable("group_id") Long group_id) {
        taskService.addGroupToTask(group_id, task_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id) && " + "@groupAccess.groupBelongsToWorkplace(#group_id, #workplace_id)")
    @DeleteMapping("/{task_id}/remove_group/{group_id}")
    public void removeGroupFromTask(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id, @PathVariable("group_id") Long group_id) {
        taskService.removeGroupFromTask(group_id, task_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id) && " + "@categoryAccess.categoryBelongsToWorkplace(#category_id, #workplace_id)")
    @PutMapping("/{task_id}/set_category/{category_id}")
    public void addCategoryToTask(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id, @PathVariable("category_id") Long category_id) {
        taskService.setCategoryToTask(category_id, task_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @DeleteMapping("/{task_id}/remove_category")
    public void removeCategoryFromTask(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id) {
        taskService.removeCategoryFromTask(task_id);
    }
}
