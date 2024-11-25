package zazadev.tasksystem.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zazadev.tasksystem.enums.TaskStatus;
import zazadev.tasksystem.model.Task;
import zazadev.tasksystem.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAuthorOrAssignee(User author, User assignee, Pageable pageable);

    Page<Task> findByAuthorOrAssigneeAndStatus(User author, User assignee, TaskStatus status, Pageable pageable);

}
