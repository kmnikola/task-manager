package pl.coderslab.task;

import org.springframework.stereotype.Service;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.category.Category;
import pl.coderslab.category.CategoryService;
import pl.coderslab.profile.Profile;
import pl.coderslab.profile.ProfileService;
import pl.coderslab.recurrenceSet.RecurrenceSet;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceRepository;
import pl.coderslab.workplace.WorkplaceService;
import pl.coderslab.workplaceGroup.WorkplaceGroup;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

import java.util.*;
import java.util.stream.Collectors;

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

    public Map<Category, List<Task>> getTasksByCategories(Long workplaceId, List<Task> tasks) {
        Map<Category, List<Task>> map = new LinkedHashMap<>();

        List<Category> categories = categoryService.getAllCategoriesByWorkplaceId(workplaceId)
                .stream()
                .sorted(Comparator.comparing(Category::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();

        for (Category category : categories) {
            map.put(category, new ArrayList<>());
        }

        Category uncategorized = new Category();
        uncategorized.setId(-1L);
        uncategorized.setName("Uncategorized");
        map.put(uncategorized, new ArrayList<>());

        for (Task task : tasks) {
            Category category = task.getCategory();
            if (category != null && map.containsKey(category)) {
                map.get(category).add(task);
            } else {
                map.get(uncategorized).add(task);
            }
        }

        for (Map.Entry<Category, List<Task>> entry : map.entrySet()) {
            List<Task> sortedTasks = entry.getValue().stream()
                    .sorted(Comparator.comparing((Task t) -> !t.isActive()))
                    .collect(Collectors.toList());
            entry.setValue(sortedTasks);
        }

        return map;
    }

    public void addTaskToWorkplace(Task task, Long workplaceId) {
        task.setWorkplace(workplaceService.getWorkplaceById(workplaceId));
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

    public void setRecurrenceSetToTask(Long taskId, RecurrenceSet recurrenceSet) {
        Task task = getTaskById(taskId);
        task.setRecurrenceSet(recurrenceSet);
        taskRepository.save(task);
    }

    public void removeRecurrenceSetFromTask(Long taskId) {
        Task task = getTaskById(taskId);
        task.setRecurrenceSet(null);
        taskRepository.save(task);
    }

    public List<Task> getAllTasksByRecurrenceSet(Long recurrenceSetId) {
        return taskRepository.findAllByRecurrenceSetId(recurrenceSetId);
    }

    public void removeGroupFromTask(Long groupId, Long taskId) {
        Task task = getTaskById(taskId);
        task.getWorkplaceGroups().remove(workplaceGroupService.getById(groupId));
        taskRepository.save(task);
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
}
