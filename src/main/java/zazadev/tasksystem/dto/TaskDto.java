package zazadev.tasksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "Сущность задачи")
public class TaskDto {

    @Schema(description = "Уникальный идентификатор задачи", example = "3432")
    private Long id;

    @Schema(description = "Название задачи", example = "Разработать телеграм-бота")
    @NotBlank
    private String title;

    @Schema(description = "Описание задачи", example = "Разработать телеграм-бота, выдающий прогноз погода каждое утро")
    @NotBlank
    private String description;

    @Schema(description = "Статус задачи", allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"})
    @NotNull
    private String status;

    @Schema(description = "Приоритет задачи", allowableValues = {"HIGH", "MEDIUM", "LOW"})
    @NotNull
    private String priority;

    @Schema(description = "Комментарии к задаче")
    private List<CommentDto> comments;

    @Schema(description = "Идентификатор создателя задачи", example = "334")
    private Long authorId;

    @Schema(description = "Идентификатор исполнителя задачи", example = "321")
    private Long assigneeId;

}
