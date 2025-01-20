package mobilePayment.mobilePayment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name = "history")
@Data
@NoArgsConstructor
@Schema(description = "Модель записи о переводе денег.")
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор платежа.")
    private int id;
    @Schema(description = "Номер телефона получателя.")
    @Column(name = "phone_number_of_recipient")
    private String phoneNumberOfRecipient;
    @Schema(description = "Дата денежного перевода.")
    private Date date;
    @Schema(description = "Сумма денежного перевода.")
    private double amount;
    /**
     * Отправитель денег.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    public PaymentHistory(String phoneNumberOfRecipient, Date date, double amount, User owner) {
        this.phoneNumberOfRecipient = phoneNumberOfRecipient;
        this.date = date;
        this.amount = amount;
        this.owner = owner;
    }
}
