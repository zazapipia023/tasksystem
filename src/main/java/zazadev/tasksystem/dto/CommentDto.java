package zazadev.tasksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность комментария")
public class CommentDto {

    @Schema(description = "Уникальный идентификатор комментария", example = "3552")
    private Long id;

    @Schema(description = "Текст комментария", example = "Комментарий к задаче 4")
    @NotBlank
    private String text;

    @Schema(description = "Идентификатор задачи, к которому прикреплен комментарий", example = "334")
    private Long taskId;

    @Schema(description = "Идентификатор автора комментария", example = "4553")
    private Long authorId;

}
