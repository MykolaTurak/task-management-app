package mate.academy.demo.service;

import lombok.RequiredArgsConstructor;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.exception.ForbiddenOperationException;
import mate.academy.demo.model.Task;
import mate.academy.demo.model.User;
import mate.academy.demo.repository.TaskRepository;
import mate.academy.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final TaskRepository taskRepository;

    @Override
    public void isCurrentUserRelatedToProject(Long projectId) {
        Long userId = authenticationService.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user with id: " + userId
                ));

        if (user.getProjects().stream()
                .noneMatch(project -> project.getId().equals(projectId))) {
            throw new ForbiddenOperationException("User with ID "
                    + userId + " is not a member of project with ID "
                    + projectId + " and cannot perform this operation.");
        }
    }

    @Override
    public void isCurrentUserRelatedToTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find task with id: " + taskId
                ));

        this.isCurrentUserRelatedToProject(task.getProject().getId());
    }
}
