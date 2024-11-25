package zazadev.tasksystem.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zazadev.tasksystem.dto.CommentDto;
import zazadev.tasksystem.dto.TaskDto;
import zazadev.tasksystem.enums.TaskPriority;
import zazadev.tasksystem.enums.TaskStatus;
import zazadev.tasksystem.exceptions.ResourceNotFoundException;
import zazadev.tasksystem.model.Comment;
import zazadev.tasksystem.model.Task;
import zazadev.tasksystem.model.User;
import zazadev.tasksystem.repositories.CommentRepository;
import zazadev.tasksystem.repositories.TaskRepository;
import zazadev.tasksystem.repositories.UserRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    public TaskDto createTask(TaskDto taskDto) {
        Task task = modelMapper.map(taskDto, Task.class);
        task.setAuthor(userRepository.findById(taskDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found")));
        task.setAssignee(userRepository.findById(taskDto.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found")));

        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDto.class);
    }

    public Page<TaskDto> getTasks(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return taskRepository.findByAuthorOrAssignee(user, user, pageable)
                .map(this::mapToTaskDto);
    }

    public Page<TaskDto> getTasks(Long userId, String status, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TaskStatus taskStatus = TaskStatus.valueOf(status);
        return taskRepository.findByAuthorOrAssigneeAndStatus(user, user, taskStatus, pageable)
                .map(this::mapToTaskDto);
    }

    public Page<TaskDto> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(this::mapToTaskDto);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (taskDto.getTitle() != null) task.setTitle(taskDto.getTitle());
        if (taskDto.getDescription() != null) task.setDescription(taskDto.getDescription());
        if (taskDto.getStatus() != null) task.setStatus(TaskStatus.valueOf(taskDto.getStatus()));
        if (taskDto.getPriority() != null) task.setPriority(TaskPriority.valueOf(taskDto.getPriority()));

        Task updatedTask = taskRepository.save(task);
        return modelMapper.map(updatedTask, TaskDto.class);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with ID " + id + " not found");
        }
        taskRepository.deleteById(id);
    }

    private TaskDto mapToTaskDto(Task task) {
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        taskDto.setComments(commentRepository.findByTask(task)
                .stream()
                .map(this::mapToCommentDto)
                .collect(Collectors.toList()));
        return taskDto;
    }

    private CommentDto mapToCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
        return commentDto;
    }


}
