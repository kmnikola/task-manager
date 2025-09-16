package pl.coderslab.access;

import org.springframework.stereotype.Component;
import pl.coderslab.task.Task;
import pl.coderslab.task.TaskService;
import pl.coderslab.workplace.WorkplaceService;

@Component("taskAccess")
public class TaskAccessEvaluator {
    private final TaskService taskService;
    private final WorkplaceService workplaceService;

    public TaskAccessEvaluator(TaskService taskService, WorkplaceService workplaceService) {
        this.taskService = taskService;
        this.workplaceService = workplaceService;
    }

    public boolean taskBelongsToWorkplace(Long task_id, Long workplace_id) {
        return workplaceService.getWorkplaceById(workplace_id).getTasks().contains(taskService.getTaskById(task_id));
    }

    public boolean taskBelongsToWorkplace(Task task, Long workplace_id) {
        return workplaceService.getWorkplaceById(workplace_id).getTasks().contains(task);
    }
}
