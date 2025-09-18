package pl.coderslab.recurrenceSet;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pl.coderslab.recurrence.Recurrence;
import pl.coderslab.recurrence.RecurrenceService;
import pl.coderslab.workplace.WorkplaceService;

@Controller
@RequestMapping("/workplaces/{workplace_id}/recurrences")
@SessionAttributes("recurrenceSet")
public class RecurrenceSetController {
    private final RecurrenceService recurrenceService;
    private final RecurrenceSetService recurrenceSetService;

    public RecurrenceSetController(RecurrenceService recurrenceService, RecurrenceSetService recurrenceSetService) {
        this.recurrenceService = recurrenceService;
        this.recurrenceSetService = recurrenceSetService;
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @GetMapping("")
    public String getAllRecurrences(@PathVariable("workplace_id") Long workplace_id, Model model) {
        model.addAttribute("recurrenceSets", recurrenceSetService.getAllRecurrenceSets(workplace_id));
        return "recurrence/list";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PostMapping("/create")
    public String createRecurrenceSet(@PathVariable("workplace_id") Long workplace_id, @RequestParam String name) {
        RecurrenceSet recurrenceSet = new RecurrenceSet();
        recurrenceSet.setName(name);
        recurrenceSetService.createRecurrenceSet(recurrenceSet, workplace_id);
        return "redirect:/workplaces/{workplace_id}/recurrences";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@recurrenceAccess.recurrenceSetBelongsToWorkplace(#recurrenceSetId, #workplace_id)")
    @GetMapping("/edit")
    public String editRecurrenceSetForm(@PathVariable("workplace_id") Long workplace_id,
                                        @RequestParam Long recurrenceSetId,
                                        Model model) {

        RecurrenceSet recurrenceSet = recurrenceSetService.getRecurrenceSetById(recurrenceSetId);
        model.addAttribute("recurrenceSet", recurrenceSet);
        model.addAttribute("recurrences", recurrenceService.getAllRecurrencesByRecurrenceSetId(recurrenceSetId));
        model.addAttribute("recurrence", new Recurrence());
        return "recurrence/edit";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@recurrenceAccess.recurrenceSetBelongsToWorkplace(#recurrenceSet.id, #workplace_id)")
    @PostMapping("/edit/add-recurrence")
    public String editAddRecurrenceToRecurrenceSet(@PathVariable("workplace_id") Long workplace_id,
                                                   @ModelAttribute RecurrenceSet recurrenceSet,
                                                   @ModelAttribute Recurrence recurrence, Model model) {
        recurrenceService.createRecurrence(recurrence, workplace_id, recurrenceSet.getId());
        model.addAttribute("recurrenceSet", recurrenceSet);
        return "redirect:/workplaces/{workplace_id}/recurrences/edit?recurrenceSetId=" + recurrenceSet.getId();
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@recurrenceAccess.recurrenceBelongsToRecurrenceSetAndWorkplace(#recurrence.id, #recurrence.recurrenceSet.id, #workplace_id)")
    @PostMapping("/edit/recurrence")
    public String editRecurrenceInRecurrenceSet(@PathVariable("workplace_id") Long workplace_id,
                                                @ModelAttribute Recurrence recurrence, Model model) {
        recurrenceService.editRecurrence(recurrence, workplace_id);
        RecurrenceSet recurrenceSet = recurrenceSetService.getRecurrenceSetByRecurrenceId(recurrence.getId());
        model.addAttribute("recurrenceSet", recurrenceSet);
        return "redirect:/workplaces/{workplace_id}/recurrences/edit?recurrenceSetId=" + recurrenceSet.getId();
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@recurrenceAccess.recurrenceSetBelongsToWorkplace(#recurrenceSet.id, #workplace_id)")
    @PostMapping("/edit")
    public String updateRecurrenceSet(@PathVariable("workplace_id") Long workplace_id,
                                      @ModelAttribute RecurrenceSet recurrenceSet,
                                      SessionStatus sessionStatus) {
        recurrenceSetService.updateRecurrenceSet(recurrenceSet);
        sessionStatus.setComplete();
        return "redirect:/workplaces/{workplace_id}/recurrences";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@recurrenceAccess.recurrenceBelongsToWorkplace(#recurrence_id, #workplace_id)")
    @PostMapping("/remove-recurrence")
    public String removeRecurrence(@PathVariable("workplace_id") Long workplace_id, @RequestParam("recurrence_id") Long recurrence_id) {
        recurrenceService.removeRecurrence(recurrence_id);
        return "redirect:/workplaces/{workplace_id}/recurrences";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " + "@recurrenceAccess.recurrenceSetBelongsToWorkplace(#recurrenceSetId, #workplace_id)")
    @PostMapping("/remove")
    public String removeRecurrenceSet(@PathVariable("workplace_id") Long workplace_id, @RequestParam("recurrenceSetId") Long recurrenceSetId) {
        recurrenceSetService.removeRecurrenceSet(recurrenceSetId);
        return "redirect:/workplaces/{workplace_id}/recurrences";
    }
}
