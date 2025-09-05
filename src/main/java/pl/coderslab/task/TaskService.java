package pl.coderslab.task;

import org.springframework.stereotype.Service;
import pl.coderslab.category.Category;
import pl.coderslab.category.CategoryService;
import pl.coderslab.workplace.WorkplaceService;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final WorkplaceService workplaceService;
    private final WorkplaceGroupService workplaceGroupService;
    private final CategoryService categoryService;

    public TaskService(TaskRepository taskRepository, WorkplaceService workplaceService, WorkplaceGroupService workplaceGroupService, CategoryService categoryService) {
        this.workplaceService = workplaceService;
        this.taskRepository = taskRepository;
        this.workplaceGroupService = workplaceGroupService;
        this.categoryService = categoryService;
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    public List<Task> getAllTasksByWorkplaceId(Long workplaceId) {
        return taskRepository.findAllByWorkplaceId(workplaceId);
    }

    public List<Task> getAllTasksByWorkplaceIdAndWorkplaceGroupId(Long workplaceId, Long workplaceGroupId) {
        return taskRepository.findAllByWorkplaceIdAndWorkplaceGroupId(workplaceId, workplaceGroupId);
    }

    public void addTaskToWorkplace(Task task, Long workplaceId) {
        WorkplaceGroup ownerGroup = workplaceGroupService.getWorkplaceGroupByWorkplaceIdAndName(workplaceId, "owner");
        //remove later
        WorkplaceGroup userGroup = workplaceGroupService.getWorkplaceGroupByWorkplaceIdAndName(workplaceId, "user");
        task.setWorkplace(workplaceService.getWorkplaceById(workplaceId));
        task.getWorkplaceGroups().add(ownerGroup);
        task.getWorkplaceGroups().add(userGroup);
        taskRepository.save(task);
    }

    public void deleteById(Long taskId, Long workplace_id) {
        if (workplaceService.getWorkplaceById(workplace_id).getTasks().contains(getTaskById(taskId))) {
            taskRepository.deleteById(taskId);
        }
    }

    public void updateTask(Task task, Long workplace_id) {
        if (workplaceService.getWorkplaceById(workplace_id).getTasks().contains(task)) {
            Task taskInDb = taskRepository.findById(task.getId()).orElseThrow();
            if (task.getTitle() != null) {
                taskInDb.setTitle(task.getTitle());
            }
            if (task.getDescription() != null) {
                taskInDb.setDescription(task.getDescription());
            }
            if (task.getCategory() != null) {
                taskInDb.setCategory(task.getCategory());
            }
            taskRepository.save(taskInDb);
        }
    }

    public void addGroupToTask(Long groupId, Long taskId, Long workplace_id) {
        if (workplaceService.getWorkplaceById(workplace_id).getTasks().contains(getTaskById(taskId))) {
            Task task = getTaskById(taskId);
            WorkplaceGroup group = workplaceGroupService.getById(groupId);
            if (task.getWorkplace().getWorkplaceGroups().contains(group)) {
                task.getWorkplaceGroups().add(group);
                taskRepository.save(task);
            }
        }
    }

    public void removeGroupFromTask(Long groupId, Long taskId, Long workplace_id) {
        if (workplaceService.getWorkplaceById(workplace_id).getTasks().contains(getTaskById(taskId))) {
            Task task = getTaskById(taskId);
            WorkplaceGroup group = workplaceGroupService.getById(groupId);
            if (task.getWorkplace().getWorkplaceGroups().contains(group)) {
                task.getWorkplaceGroups().remove(group);
            }
        }
    }

    public void setCategoryToTask(Long categoryId, Long taskId, Long workplace_id) {
        if (workplaceService.getWorkplaceById(workplace_id).getTasks().contains(getTaskById(taskId))) {
            Task task = getTaskById(taskId);
            Category category = categoryService.getById(categoryId);
            if (task.getWorkplace().getCategories().contains(category)) {
                task.setCategory(category);
                for (WorkplaceGroup group : category.getWorkplaceGroups()) {
                    task.getWorkplaceGroups().add(group);
                }
                taskRepository.save(task);
            }
        }
    }

    public void removeCategoryFromTask(Long categoryId, Long taskId, Long workplace_id) {
        if (workplaceService.getWorkplaceById(workplace_id).getTasks().contains(getTaskById(taskId))) {
            Task task = getTaskById(taskId);
            Category category = categoryService.getById(categoryId);
            if (task.getWorkplace().getCategories().contains(category)) {
                task.setCategory(null);
                for (WorkplaceGroup group : category.getWorkplaceGroups()) {
                    task.getWorkplaceGroups().remove(group);
                }
                taskRepository.save(task);
            }
        }
    }
}
