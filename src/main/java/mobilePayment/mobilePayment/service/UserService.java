package mobilePayment.mobilePayment.service;

import mobilePayment.mobilePayment.dto.BalanceDTO;
import mobilePayment.mobilePayment.dto.UserDTO;
import mobilePayment.mobilePayment.dto.UserForChangeDTO;
import mobilePayment.mobilePayment.models.Answer;
import mobilePayment.mobilePayment.models.PaymentHistory;
import mobilePayment.mobilePayment.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

/**
 * Интерфейс базового поведения слоя сервиса пользователя.
 */
public interface UserService {

    /**
     * Метод возвращающий пользователя по номеру телефона.
     * @param phoneNumber номер телефона.
     * @return пользователь.
     */
    User getUserByPhoneNumber(String phoneNumber);

    /**
     * Метод регистрации пользователя.
     * @param user кандидат на регистрацию.
     * @param bindingResult результат валидации.
     * @return {@link Answer}
     */
    Answer registry(UserDTO user, BindingResult bindingResult);

    /**
     * Метод перевода денег на другой аккаунт.
     * @param header заголовок с токеном, из которого получим номер телефона отправителя.
     * @param phoneNumberOfRecipient номер телефона получателя.
     * @param amount сумма перевода.
     * @return {@link Answer}
     */
    Answer payment(String header, String phoneNumberOfRecipient, double amount);

    /**
     * Метод редактирования пользовательских данных.
     * @param header заголовок с токеном, из которого получим номер телефона пользователя. (которого будем менять)
     * @param newUser данные для перезаписи.
     * @param bindingResult результат валидации.
     * @return {@link Answer}
     */
    Answer editProfile(String header, UserForChangeDTO newUser, BindingResult bindingResult);

    /**
     * Метод отправки истории денежных операций.
     * @param header заголовок с токеном, из которого получим номер телефона пользователя. (историю которого мы будем отправлять)
     * @param pageable настройки пагинации.
     * @return список {@link PaymentHistory}.
     */
    Page<PaymentHistory> getPaymentHistoryByHeader(String header, Pageable pageable);

    /**
     * Метод аутентификации пользователя. (При входе, когда токен протух.)
     * @param userDTO данных для входа.
     * @param bindingResult результат валидации.
     * @return {@link Answer}
     */
    Answer authenticate(UserDTO userDTO, BindingResult bindingResult);

    /**
     * Метод возвращающий баланс текущего пользователя.
     * @param header заголовок с токеном, из которого получим номер телефона пользователя. (баланс которого нам нужен)
     * @return {@link BalanceDTO}
     */
    BalanceDTO getBalance(String header);


}
