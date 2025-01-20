package mobilePayment.mobilePayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobilePayment.mobilePayment.models.FullName;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "DTO для редактирования пользователя.")
public class UserForChangeDTO {
    @Schema(description = "Пароль.")
    private String password;
    @Email(message = "Почта должна быть правильно написана!")
    @Schema(description = "Мыло.")
    private String email;
    @Schema(description = "ФИО.")
    private FullName fullName;
    @Schema(description = "День рождения.")
    @Past(message = "Ты из будущего?)") // гарантия прошедшего времени.
    private LocalDate dateOfBirth;
    @Schema(description = "Пол.")
    @Pattern(regexp = "MAN|WOMAN", message = "Пол должен быть MAN или WOMAN")
    private String gender;
}
