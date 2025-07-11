package mate.academy.demo.service;

import lombok.RequiredArgsConstructor;
import mate.academy.demo.dto.task.CreateTaskRequestDto;
import mate.academy.demo.dto.task.TaskDto;
import mate.academy.demo.exception.EntityNotFoundException;
import mate.academy.demo.mapper.TaskMapper;
import mate.academy.demo.model.Project;
import mate.academy.demo.model.Task;
import mate.academy.demo.model.User;
import mate.academy.demo.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final ProjectService projectService;

    @Override
    public TaskDto save(CreateTaskRequestDto taskRequestDto) {
        //TODO: Add telegram notification
        if (!userService.existById(taskRequestDto.getAssigneeId())) {
            throw new EntityNotFoundException("Can't find user with id: "
                    + taskRequestDto.getAssigneeId());
        }
        User assignee = new User();
        assignee.setId(taskRequestDto.getAssigneeId());

        if (!projectService.existById(taskRequestDto.getProjectId())) {
            throw new EntityNotFoundException("Can't find project with id: "
                    + taskRequestDto.getProjectId());
        }
        Project project = new Project();
        project.setId(taskRequestDto.getProjectId());

        Task task = taskMapper.toModel(taskRequestDto);
        task.setAssignee(assignee);
        task.setProject(project);

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public Page<TaskDto> findAllByProjectId(Long projectId, Pageable pageable) {
        return taskRepository.findAllByProjectId(projectId, pageable)
                .map(taskMapper::toDto);
    }

    @Override
    public TaskDto findById(Long id) {
        return taskMapper.toDto(taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find task with id: " + id
                )));
    }

    @Override
    public TaskDto update(CreateTaskRequestDto createTaskRequestDto, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find task with id: " + id
                ));

        taskMapper.updateModelFromDto(createTaskRequestDto, task);

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
