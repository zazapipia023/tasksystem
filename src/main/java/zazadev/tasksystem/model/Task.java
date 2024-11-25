package zazadev.tasksystem.model;

import jakarta.persistence.*;
import lombok.*;
import zazadev.tasksystem.enums.TaskPriority;
import zazadev.tasksystem.enums.TaskStatus;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    private User author;

    @ManyToOne
    private User assignee;

}
