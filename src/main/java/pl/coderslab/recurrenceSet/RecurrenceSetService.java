package pl.coderslab.recurrenceSet;

import org.springframework.stereotype.Service;
import pl.coderslab.workplace.Workplace;
import pl.coderslab.workplace.WorkplaceService;

import java.util.List;

@Service
public class RecurrenceSetService {
    private final RecurrenceSetRepository recurrenceSetRepository;
    private final WorkplaceService workplaceService;
    public RecurrenceSetService(RecurrenceSetRepository recurrenceSetRepository, WorkplaceService workplaceService) {
        this.recurrenceSetRepository = recurrenceSetRepository;
        this.workplaceService = workplaceService;
    }

    public List<RecurrenceSet> getAllRecurrenceSets(Long workplaceId) {
        return recurrenceSetRepository.getAllByWorkplaceId(workplaceId);
    }

    public void createRecurrenceSet(RecurrenceSet recurrenceSet, Long workplaceId) {
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        recurrenceSet.setWorkplace(workplace);
        recurrenceSetRepository.save(recurrenceSet);
    }

    public RecurrenceSet getRecurrenceSetByRecurrenceId(Long recurrenceId) {
        return recurrenceSetRepository.findByRecurrenceId(recurrenceId);
    }

    public RecurrenceSet getRecurrenceSetById(Long recurrenceSetId) {
        return recurrenceSetRepository.findById(recurrenceSetId).orElseThrow();
    }

    public void updateRecurrenceSet(RecurrenceSet recurrenceSet) {
        recurrenceSetRepository.save(recurrenceSet);
    }

    public void removeRecurrenceSet(Long recurrenceSetId) {
        recurrenceSetRepository.deleteById(recurrenceSetId);
    }
}
