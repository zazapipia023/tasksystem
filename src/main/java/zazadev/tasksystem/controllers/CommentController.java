package zazadev.tasksystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zazadev.tasksystem.dto.CommentDto;
import zazadev.tasksystem.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Контроллер комментариев", description = "Управляет созданием, поиском и удалением комментариев")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @taskSecurityService.isRelatedToTask(#commentDto.taskId, authentication.principal.id))")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Создание комментария",
            description = "Позволяет создавать комментарии админам и пользователям, кому задача назначена"
    )
    public ResponseEntity<CommentDto> createComment(
            @RequestBody @Valid @Parameter(description = "Данные комментария") CommentDto commentDto
    ) {
        CommentDto createdComment = commentService.createComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/task/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @taskSecurityService.isRelatedToTask(#taskId, authentication.principal.id))")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение комментариев",
            description = "Позволяет получить комментарии задачи админам и пользователям, кому задача назначена"
    )
    public ResponseEntity<List<CommentDto>> getCommentsByTask(
            @PathVariable @Parameter(description = "Индентификатор задачи") Long taskId
    ) {
        List<CommentDto> comments = commentService.getCommentsByTask(taskId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление комментариев",
            description = "Позволяет админам удалять комментарии"
    )
    public ResponseEntity<Void> deleteComment(
            @PathVariable @Parameter(description = "Идентификатор задачи") Long id
    ) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}
