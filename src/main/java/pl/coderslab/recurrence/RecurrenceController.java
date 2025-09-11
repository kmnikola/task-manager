package pl.coderslab.recurrence;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{workplace_id}/tasks/{task_id}/recurrence")
public class RecurrenceController {
    private final RecurrenceService recurrenceService;
    public RecurrenceController(RecurrenceService recurrenceService) {
        this.recurrenceService = recurrenceService;
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @GetMapping("")
    public List<Recurrence> getAllRecurrences(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id) {
        return recurrenceService.getAllRecurrencesByTask(task_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @PostMapping("")
    public void createRecurrence(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id, @RequestBody Recurrence recurrence) {
        recurrenceService.addRecurrenceToTask(recurrence, task_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @PutMapping("")
    public void editRecurrence(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id, @RequestBody Recurrence recurrence) {
        recurrenceService.editRecurrence(recurrence);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@taskAccess.taskBelongsToWorkplace(#task_id, #workplace_id)")
    @DeleteMapping("/{recurrence_id}")
    public void removeRecurrence(@PathVariable("workplace_id") Long workplace_id, @PathVariable("task_id") Long task_id, @PathVariable("recurrence_id") Long recurrence_id) {
        recurrenceService.removeRecurrence(recurrence_id);
    }


}
