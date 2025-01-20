package mobilePayment.mobilePayment.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Сущность ответа для клиента.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    /**
     * HTTP статус ответа.
     */
    private HttpStatus status;
    /**
     * Сообщение о результате.
     */
    private String answer;
}
