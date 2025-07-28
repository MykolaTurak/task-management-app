package mate.academy.demo.util;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import mate.academy.demo.dto.attachment.AttachmentDto;
import mate.academy.demo.dto.authentication.AuthenticationDto;
import mate.academy.demo.dto.authentication.AuthenticationRequestDto;
import mate.academy.demo.dto.comment.CommentDto;
import mate.academy.demo.dto.comment.CreateCommentRequestDto;
import mate.academy.demo.dto.label.CreateLabelRequestDto;
import mate.academy.demo.dto.label.LabelDto;
import mate.academy.demo.dto.project.CreateProjectRequestDto;
import mate.academy.demo.dto.project.ProjectDto;
import mate.academy.demo.dto.role.UpdateRoleRequestDto;
import mate.academy.demo.dto.task.CreateTaskRequestDto;
import mate.academy.demo.dto.task.TaskDto;
import mate.academy.demo.dto.user.CreateUserRequestDto;
import mate.academy.demo.dto.user.UserDto;
import mate.academy.demo.dto.user.UserWithRolesDto;
import mate.academy.demo.model.Attachment;
import mate.academy.demo.model.Comment;
import mate.academy.demo.model.Label;
import mate.academy.demo.model.Priority;
import mate.academy.demo.model.Project;
import mate.academy.demo.model.ProjectStatus;
import mate.academy.demo.model.Role;
import mate.academy.demo.model.RoleName;
import mate.academy.demo.model.Task;
import mate.academy.demo.model.TaskStatus;
import mate.academy.demo.model.User;

public class TestUtil {
    private TestUtil() {

    }

    public static User getFirstUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("test@example.com");
        user.setPassword("test_password");
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setDeleted(false);
        user.setTemporaryToken(null);
        user.setTelegramChatId(null);
        user.setRoles(new HashSet<>());
        user.getRoles().add(getUserRole());
        user.setProjects(new HashSet<>());
        user.getProjects().add(getFirstProject());
        return user;
    }

    public static User getSecondUser() {
        User user = new User();
        user.setId(2L);
        user.setUserName("alice");
        user.setPassword("alice_pass");
        user.setEmail("alice@example.com");
        user.setFirstName("Alice");
        user.setLastName("Wonder");
        user.setDeleted(false);
        user.setTemporaryToken(null);
        user.setTelegramChatId(1234567890L);
        return user;
    }

    public static User getThirdUser() {
        User user = new User();
        user.setId(3L);
        user.setUserName("bob");
        user.setPassword("bob_pass");
        user.setEmail("bob@example.com");
        user.setFirstName("Bob");
        user.setLastName("Builder");
        user.setDeleted(false);
        user.setTemporaryToken("temp_token_abc");
        user.setTelegramChatId(null);
        return user;
    }

    public static Project getFirstProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Project used for testing INSERTs");
        project.setStartDate(LocalDateTime.of(2025, 7, 1, 10, 0));
        project.setEndDate(LocalDateTime.of(2025, 8, 1, 18, 0));
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setDeleted(false);
        return project;
    }

    public static Task getFirstTask() {
        Task task = new Task();
        task.setId(1L);
        task.setName("Add file upload interface");
        task.setDescription("Create frontend form and connect it to backend");
        task.setPriority(Priority.HIGH);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDueDate(LocalDateTime.of(2025, 8, 1, 18, 0));
        task.setProject(getFirstProject());
        task.setAssignee(getFirstUser());
        task.setDeleted(false);
        return task;
    }

    public static Task getFourthTask() {
        Task task = new Task();
        task.setId(4L);
        task.setName("Fix bugs in registration");
        task.setDescription("Resolve reported issues");
        task.setPriority(Priority.MEDIUM);
        task.setStatus(TaskStatus.COMPLETED);
        task.setDueDate(LocalDateTime.of(2025, 7, 10, 15, 0));
        task.setProject(getFirstProject());
        task.setAssignee(getSecondUser());
        task.setDeleted(false);
        return task;
    }

    public static Attachment getFirstAttachment() {
        Attachment attachment = new Attachment();
        attachment.setId(1L);
        attachment.setTask(getFirstTask());
        attachment.setDropBoxFileId("dbx123abc");
        attachment.setFilename("design_mockup_1.png");
        attachment.setUpload(LocalDateTime.of(2025, 7, 20, 10, 0));
        attachment.setDeleted(false);
        return attachment;
    }

    public static Attachment getSecondAttachment() {
        Attachment attachment = new Attachment();
        attachment.setId(2L);
        attachment.setTask(getFirstTask());
        attachment.setDropBoxFileId("dbx456def");
        attachment.setFilename("specifications.pdf");
        attachment.setUpload(LocalDateTime.of(2025, 7, 20, 10, 5));
        attachment.setDeleted(false);
        return attachment;
    }

    public static Comment getFirstComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setTask(getFirstTask());
        comment.setUser(getFirstUser());
        comment.setText("Fixed issue #45 and #46.");
        comment.setTimestamp(LocalDateTime.of(2025, 7, 21, 14, 0));
        comment.setDeleted(false);
        return comment;
    }

    public static Label getFirstLabel() {
        Label label = new Label();
        label.setId(1L);
        label.setName("Frontend");
        label.setColor("#ff5733");
        label.setDeleted(false);
        label.setProjects(Set.of(getFirstProject()));
        return label;
    }

    public static Label getSecondLabel() {
        Label label = new Label();
        label.setId(2L);
        label.setName("Backend");
        label.setColor("#33c4ff");
        label.setDeleted(false);
        return label;
    }

    public static Role getAdminRole() {
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.ADMIN);
        return role;
    }

    public static Role getUserRole() {
        Role role = new Role();
        role.setId(2L);
        role.setName(RoleName.USER);
        return role;
    }

    public static AttachmentDto getFirstAttachmentDto() {
        AttachmentDto dto = new AttachmentDto();
        dto.setTaskId(1L);
        dto.setFilename("design_mockup_1.png");
        dto.setFile("Mock file content 1".getBytes());
        return dto;
    }

    public static AuthenticationRequestDto getValidAuthRequestDto() {
        AuthenticationRequestDto dto = new AuthenticationRequestDto();
        dto.setEmail("test@example.com");
        dto.setPassword("test_password");
        return dto;
    }

    public static AuthenticationDto getValidAuthDto() {
        return new AuthenticationDto("jwt-token-123456");
    }

    public static CommentDto getCommentDto() {
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setText("Fixed issue #45 and #46.");
        dto.setTimestamp(LocalDateTime.of(2025, 7, 21, 14, 0));
        dto.setTaskId(1L);
        dto.setUserId(1L);

        return dto;
    }

    public static CreateCommentRequestDto getValidCommentRequestDto() {
        CreateCommentRequestDto requestDto = new CreateCommentRequestDto();
        requestDto.setText("Fixed issue #45 and #46.");
        requestDto.setTaskId(1L);

        return requestDto;
    }

    public static LabelDto getFirstLabelDto() {
        LabelDto dto = new LabelDto();
        dto.setId(1L);
        dto.setName("Frontend");
        dto.setColor("#ff5733");
        dto.setProjectsId(Set.of(1L));

        return dto;
    }

    public static LabelDto getSecondLabelDto() {
        LabelDto dto = new LabelDto();
        dto.setId(2L);
        dto.setName("Backend");
        dto.setColor("#33c4ff");
        dto.setProjectsId(Set.of(1L));

        return dto;
    }

    public static CreateLabelRequestDto getLabelRequestDto() {
        CreateLabelRequestDto requestDto = new CreateLabelRequestDto();
        requestDto.setName("Frontend");
        requestDto.setColor("#ff5733");
        requestDto.setProjectsId(Set.of(1L));

        return requestDto;
    }

    public static ProjectDto getProjectDto() {
        ProjectDto dto = new ProjectDto();
        dto.setId(1L);
        dto.setName("Test Project");
        dto.setDescription("Project used for testing INSERTs");
        dto.setStartDate(LocalDateTime.of(2025, 7, 1, 10, 0));
        dto.setEndDate(LocalDateTime.of(2025, 8, 1, 18, 0));
        dto.setStatus(ProjectStatus.IN_PROGRESS);

        return dto;
    }

    public static CreateProjectRequestDto getProjectRequestDto() {
        CreateProjectRequestDto requestDto = new CreateProjectRequestDto();
        requestDto.setName("Test Project");
        requestDto.setDescription("Project used for testing INSERTs");
        requestDto.setStartDate(LocalDateTime.of(2025, 7, 1, 10, 0));
        requestDto.setEndDate(LocalDateTime.of(2025, 8, 1, 18, 0));
        requestDto.setStatus(ProjectStatus.IN_PROGRESS);

        return requestDto;
    }

    public static TaskDto getTaskDto() {
        TaskDto dto = new TaskDto();
        dto.setId(1L);
        dto.setName("Add file upload interface");
        dto.setDescription("Create frontend form and connect it to backend");
        dto.setPriority(Priority.HIGH);
        dto.setStatus(TaskStatus.IN_PROGRESS);
        dto.setDueDate(LocalDateTime.of(2025, 8, 1, 18, 0));
        dto.setProjectId(getFirstProject().getId());
        dto.setAssigneeId(getFirstUser().getId());
        return dto;
    }

    public static CreateTaskRequestDto getTaskRequestDto() {
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto();
        requestDto.setName("Add file upload interface");
        requestDto.setDescription("Create frontend form and connect it to backend");
        requestDto.setPriority(Priority.HIGH);
        requestDto.setStatus(TaskStatus.IN_PROGRESS);
        requestDto.setDueDate(LocalDateTime.of(2025, 8, 1, 18, 0));
        requestDto.setProjectId(getFirstProject().getId());
        requestDto.setAssigneeId(getFirstUser().getId());
        return requestDto;
    }

    public static UserDto getUserDto() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("test@example.com");
        dto.setEmail("test@example.com");
        dto.setFirstName("Test");
        dto.setLastName("User");
        return dto;
    }

    public static CreateUserRequestDto getUserRequestDto() {
        CreateUserRequestDto requestDto = new CreateUserRequestDto();
        requestDto.setUserName("test@example.com");
        requestDto.setEmail("test@example.com");
        requestDto.setFirstName("Test");
        requestDto.setLastName("User");
        requestDto.setPassword("test_password");
        requestDto.setRepeatPassword("test_password");
        return requestDto;
    }

    public static UserWithRolesDto getUserWithRolesDto() {
        UserWithRolesDto dto = new UserWithRolesDto();
        dto.setId(1L);
        dto.setUserName("test@example.com");
        dto.setEmail("test@example.com");
        dto.setFirstName("Test");
        dto.setLastName("User");
        dto.setRoleNames(Set.of(RoleName.ADMIN.name(), RoleName.USER.name()));

        return dto;
    }

    public static UpdateRoleRequestDto getUpdateAdminRoleRequestDto() {
        UpdateRoleRequestDto requestDto = new UpdateRoleRequestDto();
        requestDto.setRoleName(RoleName.ADMIN);

        return requestDto;
    }

    public static UpdateRoleRequestDto getUpdateUserRoleRequestDto() {
        UpdateRoleRequestDto requestDto = new UpdateRoleRequestDto();
        requestDto.setRoleName(RoleName.USER);

        return requestDto;
    }
}
