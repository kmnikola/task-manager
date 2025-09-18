package pl.coderslab.recurrence;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.recurrenceSet.RecurrenceSet;
import pl.coderslab.recurrenceSet.RecurrenceSetService;
import pl.coderslab.task.Task;
import pl.coderslab.task.TaskService;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecurrenceService {
    private final RecurrenceRepository recurrenceRepository;
    private final TaskService taskService;
    private final WorkplaceService workplaceService;
    private final RecurrenceSetService recurrenceSetService;
    public RecurrenceService(RecurrenceRepository recurrenceRepository, TaskService taskService, WorkplaceService workplaceService, RecurrenceSetService recurrenceSetService) {
        this.recurrenceRepository = recurrenceRepository;
        this.taskService = taskService;
        this.workplaceService = workplaceService;
        this.recurrenceSetService = recurrenceSetService;
    }

    public Recurrence getRecurrenceById(Long id) {
        return recurrenceRepository.findById(id).orElseThrow();
    }

    public List<Recurrence> getAllRecurrences(Long workplaceId) {
        return recurrenceRepository.getAllByWorkplaceId(workplaceId);
    }

    public List<Recurrence> getAllRecurrencesByRecurrenceSetId(Long recurrenceSetId) {
        return recurrenceRepository.getAllByRecurrenceSetId(recurrenceSetId);
    }

    public void createRecurrence(Recurrence recurrence, Long workplaceId, Long recurrenceSetId) {
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        recurrence.setWorkplace(workplace);
        RecurrenceSet recurrenceSet = recurrenceSetService.getRecurrenceSetById(recurrenceSetId);
        recurrence.setRecurrenceSet(recurrenceSet);
        recurrenceRepository.save(recurrence);
        recurrenceSet.getRecurrences().add(recurrence);
        recurrenceSetService.updateRecurrenceSet(recurrenceSet);
        workplace.getRecurrences().add(recurrence);
        workplaceService.updateWorkplace(workplace);
    }

    public void editRecurrence(Recurrence recurrence, Long workplaceId) {
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        recurrence.setWorkplace(workplace);
        recurrenceRepository.save(recurrence);
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
                RecurrenceSet recurrenceSet = recurrenceSetService.getRecurrenceSetByRecurrenceId(recurrence.getId());
                List<Task> tasks = taskService.getAllTasksByRecurrenceSet(recurrenceSet.getId());
                for (Task task : tasks) {
                    if (!task.isActive()) {
                        taskService.activateTask(task);
                    }
                }
            }
        }
    }
}
