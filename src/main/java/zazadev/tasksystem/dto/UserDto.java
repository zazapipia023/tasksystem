package zazadev.tasksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность пользователя")
public class UserDto {

    @Schema(description = "Уникальный идентификатор пользователя", example = "3456")
    private Long id;

    @Schema(description = "Почта пользователя", example = "example@gmail.com")
    private String email;

    @Schema(description = "Роль пользователя", allowableValues = {"ADMIN", "USER"})
    private String role;

}
