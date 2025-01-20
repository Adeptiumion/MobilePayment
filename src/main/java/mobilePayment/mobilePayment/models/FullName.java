package mobilePayment.mobilePayment.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
@Schema(description = "Модель ФИО.")
public class FullName {
    @Schema(description = "Имя.")
    private String firstName;
    @Schema(description = "Фамилия.")
    private String lastName;
    @Schema(description = "Отчество.")
    private String surname;
}
