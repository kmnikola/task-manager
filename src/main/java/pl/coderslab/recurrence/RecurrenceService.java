package pl.coderslab.recurrence;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.task.Task;
import pl.coderslab.task.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecurrenceService {
    private final RecurrenceRepository recurrenceRepository;
    private final TaskService taskService;
    public RecurrenceService(RecurrenceRepository recurrenceRepository, TaskService taskService) {
        this.recurrenceRepository = recurrenceRepository;
        this.taskService = taskService;
    }

    public List<Recurrence> getAllRecurrences() {
        return recurrenceRepository.findAll();
    }

    public List<Recurrence> getAllRecurrencesByTask(Long taskId) {
        return taskService.getTaskById(taskId).getRecurrences();
    }

    public void addRecurrenceToTask(Recurrence recurrence, Long taskId) {
        recurrenceRepository.save(recurrence);
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

        List<Recurrence> recurrences = getAllRecurrences();

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
