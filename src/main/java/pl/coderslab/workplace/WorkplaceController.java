package pl.coderslab.workplace;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.auth.CurrentUser;

import java.util.List;

@Controller
@RequestMapping("/workplaces")
public class WorkplaceController {
    private final WorkplaceService workplaceService;

    public WorkplaceController(WorkplaceService workplaceService) {
        this.workplaceService = workplaceService;
    }

    @GetMapping("")
    public String getAllWorkplaces(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        List<Workplace> workplaces = workplaceService.getAllWorkplaces(currentUser);
        model.addAttribute("workplace", new Workplace());
        model.addAttribute("workplaces", workplaces);
        return "workplace/list";
    }

    @PostMapping("/create")
    public String createWorkplace(@ModelAttribute Workplace workplace, @AuthenticationPrincipal CurrentUser currentUser) {
        workplaceService.createWorkplace(currentUser, workplace);
        return "redirect:/workplaces";
    }

    @PreAuthorize("!@workplaceAccess.canAccessWorkplace(authentication, #workplace_id)")
    @PostMapping("/join")
    public String joinWorkplace(@RequestParam("workplace_id") Long workplace_id, @AuthenticationPrincipal CurrentUser currentUser) {
        workplaceService.joinWorkplace(currentUser, workplace_id);
        return "redirect:/workplaces";
    }

    @PreAuthorize("@workplaceAccess.canAccessWorkplace(authentication, #workplace_id) && !@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PostMapping("/leave/{workplace_id}")
    public String leaveWorkplace(@PathVariable("workplace_id") Long workplace_id, @AuthenticationPrincipal CurrentUser currentUser) {
        workplaceService.leaveWorkplace(currentUser, workplace_id);
        return "redirect:/workplaces";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace.getId())")
    @PostMapping("/edit")
    public String updateWorkplace(@ModelAttribute Workplace workplace) {
        workplaceService.editWorkplace(workplace);
        return "redirect:/workplaces";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PostMapping("/delete/{workplace_id}")
    public String deleteWorkplace(@PathVariable("workplace_id") Long workplace_id, @AuthenticationPrincipal CurrentUser currentUser) {
        workplaceService.deleteWorkplace(workplace_id, currentUser);
        return "redirect:/workplaces";
    }
}
