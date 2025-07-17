package mate.academy.demo.service;

public interface VerificationService {
    void isCurrentUserRelatedToProject(Long projectId);

    void isCurrentUserRelatedToTask(Long taskId);
}
