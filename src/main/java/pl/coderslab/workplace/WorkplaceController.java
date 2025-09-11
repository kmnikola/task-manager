package pl.coderslab.workplace;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.auth.CurrentUser;
import pl.coderslab.user.User;

import java.util.List;

@RestController
@RequestMapping("/workplaces")
public class WorkplaceController {
    private final WorkplaceService workplaceService;
    public WorkplaceController(WorkplaceService workplaceService) {
        this.workplaceService = workplaceService;
    }

    @GetMapping("")
    public List<Workplace> getAllWorkplaces(@AuthenticationPrincipal CurrentUser currentUser) {
        return workplaceService.getAllWorkplaces(currentUser);
    }

    @GetMapping("/{id}")
    public Workplace getWorkplaceById(@PathVariable("id") Long id, @AuthenticationPrincipal CurrentUser currentUser) {
        return workplaceService.getWorkplaceById(currentUser, id);
    }

    @PostMapping("")
    public void createWorkplace(@RequestBody Workplace workplace, @AuthenticationPrincipal CurrentUser currentUser) {
        workplaceService.createWorkplace(currentUser, workplace);
    }

    @PutMapping("/{id}")
    public void joinWorkplace(@PathVariable("id") Long workplaceId, @AuthenticationPrincipal CurrentUser currentUser) {
        workplaceService.joinWorkplace(currentUser, workplaceId);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace.getId())")
    @PutMapping("")
    public void updateWorkplace(@RequestBody Workplace workplace) {
        workplaceService.updateWorkplace(workplace);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @DeleteMapping("/{workplace_id}")
    public void deleteWorkplace(@PathVariable("workplace_id") Long workplace_id) {
        workplaceService.deleteWorkplace(workplace_id);
    }
}