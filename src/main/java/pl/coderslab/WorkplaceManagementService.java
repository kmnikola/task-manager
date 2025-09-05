package pl.coderslab;

import org.springframework.stereotype.Service;
import pl.coderslab.category.CategoryRepository;
import pl.coderslab.category.CategoryService;
import pl.coderslab.profile.ProfileRepository;
import pl.coderslab.profile.ProfileService;
import pl.coderslab.task.TaskRepository;
import pl.coderslab.task.TaskService;
import pl.coderslab.user.UserRepository;
import pl.coderslab.user.UserService;
import pl.coderslab.workplace.WorkplaceRepository;
import pl.coderslab.workplace.WorkplaceService;
import pl.coderslab.workplaceGroup.WorkplaceGroupRepository;
import pl.coderslab.workplaceGroup.WorkplaceGroupService;

@Service
public class WorkplaceManagementService {
    private final WorkplaceRepository workplaceRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final WorkplaceGroupRepository workplaceGroupRepository;
    private final ProfileRepository profileRepository;

    public WorkplaceManagementService(WorkplaceRepository workplaceRepository, CategoryRepository categoryRepository, UserRepository userRepository, TaskRepository taskRepository, ProfileRepository profileRepository, WorkplaceGroupRepository workplaceGroupRepository) {
        this.workplaceRepository = workplaceRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.profileRepository = profileRepository;
        this.workplaceGroupRepository = workplaceGroupRepository;
    }
}
