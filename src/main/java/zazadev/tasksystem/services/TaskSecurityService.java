package zazadev.tasksystem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zazadev.tasksystem.repositories.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskSecurityService {

    private final TaskRepository taskRepository;

    public boolean isAssignee(Long taskId, Long userId) {
        return taskRepository.findById(taskId)
                .map(task -> task.getAssignee() != null && task.getAssignee().getId().equals(userId))
                .orElse(false);
    }

    public boolean isRelatedToTask(Long taskId, Long userId) {
        return taskRepository.findById(taskId)
                .map(task -> task.getAuthor().getId().equals(userId) ||
                        (task.getAssignee() != null && task.getAssignee().getId().equals(userId)))
                .orElse(false);
    }

}
