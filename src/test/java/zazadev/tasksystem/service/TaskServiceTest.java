package zazadev.tasksystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import zazadev.tasksystem.dto.TaskDto;
import zazadev.tasksystem.enums.Role;
import zazadev.tasksystem.enums.TaskPriority;
import zazadev.tasksystem.enums.TaskStatus;
import zazadev.tasksystem.model.Task;
import zazadev.tasksystem.model.User;
import zazadev.tasksystem.repositories.CommentRepository;
import zazadev.tasksystem.repositories.TaskRepository;
import zazadev.tasksystem.repositories.UserRepository;
import zazadev.tasksystem.services.TaskService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Test
    public void testCreateTask() {
        ModelMapper modelMapper = new ModelMapper();
        TaskService taskService = new TaskService(taskRepository, userRepository, commentRepository, modelMapper);

        User author = new User(1L, "author@example.com", "password", Role.ADMIN);
        User assignee = new User(2L, "assignee@example.com", "password", Role.USER);
        Task task = new Task(1L, "Title", "Description", TaskStatus.PENDING, TaskPriority.HIGH, author, assignee);

        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDto taskDto = new TaskDto(null, "Title", "Description", "PENDING", "HIGH", List.of() , 1L, 2L);
        TaskDto createdTask = taskService.createTask(taskDto);

        assertNotNull(createdTask);
        assertEquals("Title", createdTask.getTitle());
        assertEquals("Description", createdTask.getDescription());
        assertEquals("PENDING", createdTask.getStatus());
    }

    @Test
    public void testGetTasksByAuthorOrAssignee() {
        ModelMapper modelMapper = new ModelMapper();
        TaskService taskService = new TaskService(taskRepository, userRepository, commentRepository, modelMapper);

        User author = new User(1L, "author@example.com", "password", Role.ADMIN);
        User assignee = new User(2L, "assignee@example.com", "password", Role.USER);

        Task task1 = new Task(1L, "Task1", "Description1", TaskStatus.PENDING, TaskPriority.HIGH, author, assignee);
        Task task2 = new Task(2L, "Task2", "Description2", TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM, author, assignee);

        Page<Task> tasks = new PageImpl<>(List.of(task1, task2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(taskRepository.findByAuthorOrAssignee(author, author, Pageable.unpaged())).thenReturn(tasks);

        Page<TaskDto> result = taskService.getTasks(1L, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }
}

