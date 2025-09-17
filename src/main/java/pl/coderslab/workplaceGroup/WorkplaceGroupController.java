package pl.coderslab.workplaceGroup;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.workplace.WorkplaceService;

import java.util.List;

@Controller
@RequestMapping("/workplaces/{workplace_id}/groups")
public class WorkplaceGroupController {

    private final WorkplaceGroupService workplaceGroupService;
    private final WorkplaceService workplaceService;

    public WorkplaceGroupController(WorkplaceGroupService workplaceGroupService, WorkplaceService workplaceService) {
        this.workplaceGroupService = workplaceGroupService;
        this.workplaceService = workplaceService;
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @GetMapping("")
    public String listGroups(@PathVariable("workplace_id") Long workplace_id, Model model) {
        List<WorkplaceGroup> groups = workplaceGroupService.getWorkplaceGroupsInWorkplace(workplace_id);
        model.addAttribute("groups", groups);
        model.addAttribute("workplace_id", workplace_id);
        return "groups/list";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @GetMapping("/create")
    public String showCreateForm(@PathVariable("workplace_id") Long workplace_id, Model model) {
        model.addAttribute("workplaceGroup", new WorkplaceGroup());
        model.addAttribute("workplace_id", workplace_id);
        return "groups/form";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id)")
    @PostMapping("/create")
    public String createGroup(@PathVariable("workplace_id") Long workplace_id,
                              @ModelAttribute WorkplaceGroup workplaceGroup) {
        workplaceGroup.setWorkplace(workplaceService.getWorkplaceById(workplace_id));
        workplaceGroupService.addWorkplaceGroup(workplaceGroup, workplace_id);
        return "redirect:/workplaces/" + workplace_id + "/groups";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " +
            "@groupAccess.groupBelongsToWorkplace(#group_id, #workplace_id)")
    @GetMapping("/edit")
    public String showEditForm(@PathVariable("workplace_id") Long workplace_id,
                               @RequestParam("group_id") Long group_id,
                               Model model) {
        WorkplaceGroup group = workplaceGroupService.getById(group_id);
        model.addAttribute("workplaceGroup", group);
        model.addAttribute("workplace_id", workplace_id);
        return "groups/edit";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " +
            "@groupAccess.groupBelongsToWorkplace(#workplaceGroup.id, #workplace_id)")
    @PostMapping("/edit")
    public String editGroup(@PathVariable("workplace_id") Long workplace_id,
                            @ModelAttribute WorkplaceGroup workplaceGroup) {
        workplaceGroupService.update(workplaceGroup, workplace_id);
        return "redirect:/workplaces/" + workplace_id + "/groups";
    }

    @PreAuthorize("@workplaceAccess.canEditWorkplace(authentication, #workplace_id) && " +
            "@groupAccess.groupBelongsToWorkplace(#group_id, #workplace_id)")
    @PostMapping("/delete")
    public String deleteGroup(@PathVariable("workplace_id") Long workplace_id,
                              @RequestParam("group_id") Long group_id) {
        workplaceGroupService.deleteById(workplace_id, group_id);
        return "redirect:/workplaces/" + workplace_id + "/groups";
    }
}