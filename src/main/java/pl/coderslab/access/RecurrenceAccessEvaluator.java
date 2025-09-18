package pl.coderslab.access;

import org.springframework.stereotype.Component;
import pl.coderslab.recurrence.Recurrence;
import pl.coderslab.recurrence.RecurrenceService;
import pl.coderslab.recurrenceSet.RecurrenceSetService;

@Component("recurrenceAccess")
public class RecurrenceAccessEvaluator {
    private final RecurrenceService recurrenceService;
    private final RecurrenceSetService recurrenceSetService;

    public RecurrenceAccessEvaluator(RecurrenceService recurrenceService, RecurrenceSetService recurrenceSetService) {
        this.recurrenceService = recurrenceService;
        this.recurrenceSetService = recurrenceSetService;
    }

    public boolean recurrenceSetBelongsToWorkplace(Long recurrenceSetId, Long workplaceId) {
        return recurrenceSetService.getRecurrenceSetById(recurrenceSetId).getWorkplace().getId().equals(workplaceId);
    }

    public boolean recurrenceBelongsToRecurrenceSetAndWorkplace(Long recurrenceId, Long recurrenceSetId, Long workplaceId) {
        Recurrence recurrence = recurrenceService.getRecurrenceById(recurrenceId);
        return recurrence.getWorkplace().getId().equals(workplaceId) && recurrence.getRecurrenceSet().getId().equals(recurrenceSetId);
    }

    public boolean recurrenceBelongsToWorkplace(Long recurrenceId, Long workplaceId) {
        return recurrenceService.getRecurrenceById(recurrenceId).getWorkplace().getId().equals(workplaceId);
    }
}
