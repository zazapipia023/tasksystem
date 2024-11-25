package zazadev.tasksystem.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zazadev.tasksystem.dto.TaskDto;
import zazadev.tasksystem.model.Task;
import zazadev.tasksystem.services.TaskService;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Контроллер задач", description = "Управляет созданием, редактированием и удалением задач")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Создание задачи",
            description = "Позволяет создать задачу"
    )
    public ResponseEntity<TaskDto> createTask(
            @RequestBody @Valid @Parameter(description = "Данные задачи") TaskDto taskDto
    ) {
        TaskDto createdTask = taskService.createTask(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение задач",
            description = "Позволяет получить задачи в трех видах:\n" +
                    "1: Получить все задачи, при условии что запрос пришел от админа\n" +
                    "2: Получить задачи конкретного пользователя(назначенные ему или им)\n" +
                    "3: Получить задачи конкретного пользователя определенного статуса(назначенные ему или им)"

    )
    public ResponseEntity<Page<TaskDto>> getTasks(
            @RequestParam(required = false) @Parameter(description = "Идентификатор пользователя") Long userId,
            @RequestParam(required = false) @Parameter(description = "Статус задачи") String status,
            @Parameter(hidden = true) Pageable pageable
    ) {
        Page<TaskDto> tasks;
        if (userId == null) {
            tasks = taskService.getAllTasks(pageable);
        } else if (status == null) {
            tasks = taskService.getTasks(userId, pageable);
        } else {
            tasks = taskService.getTasks(userId, status, pageable);
        }
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @taskSecurityService.isAssignee(#id, authentication.principal.id))")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Обновление задачи",
            description = "Позволяет отредактировать задачу админам или пользователям, кому задача была назначена"
    )
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable @Parameter(description = "Идентификатор задачи") Long id,
            @RequestBody @Parameter(description = "Обновленные данные задачи") TaskDto taskDto
    ) {
        TaskDto updatedTask = taskService.updateTask(id, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление задачи",
            description = "Позволяет удалять задачи(только для админов)"
    )
    public ResponseEntity<Void> deleteTask(
            @PathVariable @Parameter(description = "Идентификатор задачи") Long id
    ) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
