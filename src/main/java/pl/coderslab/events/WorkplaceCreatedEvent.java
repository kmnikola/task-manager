package pl.coderslab.events;

import pl.coderslab.user.User;
import pl.coderslab.workplaceGroup.WorkplaceGroup;

import java.util.List;

public record WorkplaceCreatedEvent(User user, List<WorkplaceGroup> groups, Long workplaceId) {}