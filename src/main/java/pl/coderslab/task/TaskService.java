package pl.coderslab.task;

import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.category.CategoryService;
import pl.coderslab.profile.Profile;
import pl.coderslab.profile.ProfileService;
import pl.coderslab.recurrence.Recurrence;
import pl.coderslab.user.User;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceRepository;
import pl.coderslab.workplace.WorkplaceService;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final WorkplaceService workplaceService;
    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceGroupService workplaceGroupService;
    private final CategoryService categoryService;
    private final ProfileService profileService;

    public TaskService(TaskRepository taskRepository, WorkplaceService workplaceService, WorkplaceRepository workplaceRepository, WorkplaceGroupService workplaceGroupService, CategoryService categoryService, ProfileService profileService) {
        this.workplaceService = workplaceService;
        this.taskRepository = taskRepository;
        this.workplaceRepository = workplaceRepository;
        this.workplaceGroupService = workplaceGroupService;
        this.categoryService = categoryService;
        this.profileService = profileService;
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    public List<Task> getAllTasksByWorkplaceIdAndUser(Long workplaceId, CurrentUser currentUser) {
        Profile profile = profileService.getProfileByWorkplaceIdAndUserId(workplaceId, currentUser.getUser().getId());
        return taskRepository.findAllByWorkplaceIdAndWorkplaceGroupId(workplaceId, profile.getWorkplaceGroup().getId());
    }

    public List<Task> getAllTasksByWorkplaceId(Long workplaceId) {
        return taskRepository.findAllByWorkplaceId(workplaceId);
    }

    public List<Task> getAllTasksByCategoryId(Long workplaceId, Long categoryId) {
        return taskRepository.findAllByWorkplaceIdAndCategoryId(workplaceId, categoryId);
    }

    public void addTaskToWorkplace(Task task, Long workplaceId) {
        WorkplaceGroup ownerGroup = workplaceGroupService.getWorkplaceGroupByWorkplaceIdAndName(workplaceId, "owner");
        WorkplaceGroup userGroup = workplaceGroupService.getWorkplaceGroupByWorkplaceIdAndName(workplaceId, "user");
        task.setWorkplace(workplaceService.getWorkplaceById(workplaceId));
        task.getWorkplaceGroups().add(ownerGroup);
        task.getWorkplaceGroups().add(userGroup);
        taskRepository.save(task);
    }

    public void deleteById(Long workplaceId, Long taskId) {
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        workplace.getTasks().remove(getTaskById(taskId));
        workplaceRepository.save(workplace);
    }

    public void updateTask(Task task) {
        Task taskInDb = getTaskById(task.getId());
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

    public void toggleActive(Long taskId) {
        Task task = getTaskById(taskId);
        task.setActive(!task.isActive());
        taskRepository.save(task);
    }

    public void activateTask(Task task) {
        if (!task.isActive()) {
            task.setActive(true);
            taskRepository.save(task);
            System.out.println("Task " + task.getTitle() + " activated");
        } else {
            System.out.println("Task " + task.getTitle() + " already activated");
        }
    }

    public void addGroupToTask(Long groupId, Long taskId) {
        Task task = getTaskById(taskId);
        WorkplaceGroup group = workplaceGroupService.getById(groupId);
        if (!task.getWorkplaceGroups().contains(group)) {
            task.getWorkplaceGroups().add(group);
        }
        taskRepository.save(task);
    }

    public void addRecurrenceToTask(Long taskId, Recurrence recurrence) {
        Task task = getTaskById(taskId);
        task.getRecurrences().add(recurrence);
        taskRepository.save(task);
    }

    public void removeGroupFromTask(Long groupId, Long taskId) {
        getTaskById(taskId).getWorkplaceGroups().remove(workplaceGroupService.getById(groupId));
    }

    public void setCategoryToTask(Long categoryId, Long taskId) {
        Task task = getTaskById(taskId);
        task.setCategory(categoryService.getById(categoryId));
        taskRepository.save(task);
    }

    public void removeCategoryFromTask(Long taskId) {
        Task task = getTaskById(taskId);
        task.setCategory(null);
        taskRepository.save(task);
    }

    public List<Task> getAllTasksByRecurrence(Long recurrence_id) {
        return taskRepository.findAllByRecurrenceId(recurrence_id);
    }
}
