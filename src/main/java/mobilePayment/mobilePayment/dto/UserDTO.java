package mobilePayment.mobilePayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для регистрации.")
public class UserDTO {
    @Schema(description = "Номер телефона будующего пользователя.")
    private String phoneNumber;
    @Schema(description = "Пароль будущего пользователя.")
    private String password;
}
