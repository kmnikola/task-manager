package pl.coderslab.recurrence;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.task.Task;
import pl.coderslab.task.TaskService;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecurrenceService {
    private final RecurrenceRepository recurrenceRepository;
    private final TaskService taskService;
    private final WorkplaceService workplaceService;
    public RecurrenceService(RecurrenceRepository recurrenceRepository, TaskService taskService, WorkplaceService workplaceService) {
        this.recurrenceRepository = recurrenceRepository;
        this.taskService = taskService;
        this.workplaceService = workplaceService;
    }

    public List<Recurrence> getAllRecurrencesByTask(Long taskId) {
        return taskService.getTaskById(taskId).getRecurrences();
    }

    public void createRecurrence(Recurrence recurrence, Long workplaceId) {
        recurrenceRepository.save(recurrence);
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        workplace.getRecurrences().add(recurrence);
        workplaceService.updateWorkplace(workplace);
    }

    public void addRecurrenceToTask(Recurrence recurrence, Long taskId) {
        taskService.addRecurrenceToTask(taskId, recurrence);
    }

    public void editRecurrence(Recurrence recurrence) {
        Recurrence recurrenceInDB = recurrenceRepository.findById(recurrence.getId()).orElseThrow();
        if (recurrence.getDays() != null) {
            recurrenceInDB.setDays(recurrence.getDays());
        }
        if (recurrence.getTime() != null) {
            recurrenceInDB.setTime(recurrence.getTime());
        }
        recurrenceRepository.save(recurrenceInDB);
    }

    public void removeRecurrence(Long recurrenceId) {
        recurrenceRepository.deleteById(recurrenceId);
    }

    @Scheduled(fixedRate = 60000) // every 60 seconds
    @Transactional
    public void activateTasksIfDue() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        List<Recurrence> recurrences = recurrenceRepository.findAll();

        for (Recurrence recurrence : recurrences) {
            if (recurrence.matches(now)) {
                List<Task> tasks = taskService.getAllTasksByRecurrence(recurrence.getId());
                for (Task task : tasks) {
                    if (!task.isActive()) {
                        taskService.activateTask(task);
                    }
                }
            }
        }
    }
}
