package zazadev.tasksystem.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import zazadev.tasksystem.dto.CommentDto;
import zazadev.tasksystem.exceptions.ResourceNotFoundException;
import zazadev.tasksystem.model.Comment;
import zazadev.tasksystem.model.Task;
import zazadev.tasksystem.repositories.CommentRepository;
import zazadev.tasksystem.repositories.TaskRepository;
import zazadev.tasksystem.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CommentDto createComment(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setTask(taskRepository.findById(commentDto.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        comment.setAuthor(userRepository.findById(commentDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found")));

        Comment savedComment = commentRepository.save(comment);
        return modelMapper.map(savedComment, CommentDto.class);
    }

    public List<CommentDto> getCommentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        List<Comment> comments = commentRepository.findByTask(task);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList());
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment with ID " + id + " not found");
        }
        commentRepository.deleteById(id);
    }
}
