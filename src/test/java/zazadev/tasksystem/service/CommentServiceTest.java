package zazadev.tasksystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import zazadev.tasksystem.dto.CommentDto;
import zazadev.tasksystem.enums.Role;
import zazadev.tasksystem.enums.TaskPriority;
import zazadev.tasksystem.enums.TaskStatus;
import zazadev.tasksystem.model.Comment;
import zazadev.tasksystem.model.Task;
import zazadev.tasksystem.model.User;
import zazadev.tasksystem.repositories.CommentRepository;
import zazadev.tasksystem.repositories.TaskRepository;
import zazadev.tasksystem.repositories.UserRepository;
import zazadev.tasksystem.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateComment() {
        ModelMapper modelMapper = new ModelMapper();
        CommentService commentService = new CommentService(commentRepository, taskRepository, userRepository, modelMapper);

        User author = new User(1L, "author@example.com", "password", Role.USER);
        Task task = new Task(1L, "Task Title", "Task Description", TaskStatus.PENDING, TaskPriority.HIGH, author, author);
        Comment comment = new Comment(1L, "This is a comment", task, author);

        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto commentDto = new CommentDto(null, "This is a comment", 1L, 1L);
        CommentDto createdComment = commentService.createComment(commentDto);

        assertNotNull(createdComment);
        assertEquals("This is a comment", createdComment.getText());
    }

    @Test
    public void testGetCommentsByTask() {
        ModelMapper modelMapper = new ModelMapper();
        CommentService commentService = new CommentService(commentRepository, taskRepository, userRepository, modelMapper);

        User author = new User(1L, "author@example.com", "password", Role.USER);
        Task task = new Task(1L, "Task Title", "Task Description", TaskStatus.PENDING, TaskPriority.HIGH, author, author);
        Comment comment1 = new Comment(1L, "Comment1", task, author);
        Comment comment2 = new Comment(2L, "Comment2", task, author);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.findByTask(task)).thenReturn(List.of(comment1, comment2));

        List<CommentDto> comments = commentService.getCommentsByTask(1L);

        assertNotNull(comments);
        assertEquals(2, comments.size());
    }
}

