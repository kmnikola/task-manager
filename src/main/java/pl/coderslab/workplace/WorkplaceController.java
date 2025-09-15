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

    @PreAuthorize("@workplaceAccess.canAccessWorkplace(authentication, #workplace_id)")
    @GetMapping("/{workplace_id}")
    public Workplace getWorkplaceById(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable("workplace_id") Long workplace_id) {
        return workplaceService.getWorkplaceById(workplace_id);
    }

    @PostMapping("")
    public void createWorkplace(@RequestBody Workplace workplace, @AuthenticationPrincipal CurrentUser currentUser) {
        workplaceService.createWorkplace(currentUser, workplace);
    }

    @PreAuthorize("!@workplaceAccess.canAccessWorkplace(authentication, #workplace_id)")
    @PutMapping("/join/{workplace_id}")
    public void joinWorkplace(@PathVariable("workplace_id") Long workplace_id, @AuthenticationPrincipal CurrentUser currentUser) {
        workplaceService.joinWorkplace(currentUser, workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canAccessWorkplace(authentication, #workplace_id) && " + "!@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PutMapping("/leave/{workplace_id}")
    public void leaveWorkplace(@PathVariable("workplace_id") Long workplace_id, @AuthenticationPrincipal CurrentUser currentUser) {
        workplaceService.leaveWorkplace(currentUser, workplace_id);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace.getId())")
    @PutMapping("")
    public void updateWorkplace(@RequestBody Workplace workplace) {
        workplaceService.editWorkplace(workplace);
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @DeleteMapping("/{workplace_id}")
    public void deleteWorkplace(@PathVariable("workplace_id") Long workplace_id) {
        workplaceService.deleteWorkplace(workplace_id);
    }
}