package mobilePayment.mobilePayment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Schema(
        description = "DTO для того чтобы:\n" +
                "На основе авторизационных данных возвращать текущее значение баланса счета и логин пользователя."
)
public class BalanceDTO {
    @Schema(description = "Баланс пользователя.")
    private int balance;
    @Schema(description = "Логин пользователя.")
    @JsonProperty("login")
    private String phoneNumber;
}
