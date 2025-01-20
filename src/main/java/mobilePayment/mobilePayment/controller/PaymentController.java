package mobilePayment.mobilePayment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mobilePayment.mobilePayment.component.JWTFilter;
import mobilePayment.mobilePayment.dto.BalanceDTO;
import mobilePayment.mobilePayment.dto.UserDTO;
import mobilePayment.mobilePayment.dto.UserForChangeDTO;
import mobilePayment.mobilePayment.models.Answer;
import mobilePayment.mobilePayment.models.PaymentHistory;
import mobilePayment.mobilePayment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Главный контроллер.
 */
@Slf4j
@Tag(name = "Mobile payment endpoints")
@RestController
@RequestMapping("/api")
@PropertySource("classpath:jwt.properties")
public class PaymentController {


    /**
     * Для выдачи данных по запросам.
     */
    private final UserService userService;

    @Autowired
    public PaymentController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Регистрирует пользователя на основе полученных данных из тела запроса, выдавая токен аутентификации.")
    @PostMapping("/registry")
    public ResponseEntity<String> registry(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Данные будующего пользователя.")
            @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) {

        Answer answer = userService.registry(userDTO, bindingResult);
        return new ResponseEntity<>(answer.getAnswer(), answer.getStatus());
    }

    @Operation(summary = "Выдает новый токен на основе данных пользователя.")
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Данные пользователя для входа.")
            @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) {

        Answer answer = userService.authenticate(userDTO, bindingResult);
        return new ResponseEntity<>(answer.getAnswer(), answer.getStatus());
    }

    /**
     * Тут я не ловлю исключение при валидации токена.
     * Если бы оно и было, то оно бы было поймано в {@link JWTFilter}.
     */
    @Operation(summary = "Возвращает баланс и номер телефона текущего пользователя. (На основе данных из токена.)")
    @GetMapping("/balance")
    public ResponseEntity<BalanceDTO> balance(
            @Parameter(
                    description = "Токен аутентификации с добавлением в начале Bearer + пробел.",
                    required = true,
                    in = ParameterIn.HEADER,
                    example = "Bearer <токен в виде строки>"
            )
            @RequestHeader("Authorization") String header
    ) {
        return new ResponseEntity<>(userService.getBalance(header), HttpStatus.OK);
    }

    @Operation(
            summary = "Переводит деньги на аккаунт по номеру телефона.",
            description = "Если денег не хватит вернет false, при успехе true."
    )
    @GetMapping("/payment/{phone}/{amount}")
    public ResponseEntity<String> payment(
            @Parameter(
                    description = "Токен аутентификации с добавлением в начале Bearer + пробел.",
                    required = true,
                    in = ParameterIn.HEADER,
                    example = "Bearer <токен в виде строки>"
            )
            @RequestHeader("Authorization") String header,
            @Parameter(required = true, description = "Номер получателя.", example = "89130274250")
            @PathVariable("phone") String recipientPhoneNumber,
            @Parameter(required = true, description = "Сумма перевода. 100 рублей 55 копеек. (Пример ниже.)", example = "100.55")
            @PathVariable("amount") double amount
    ) {

        Answer answer = userService.payment(header, recipientPhoneNumber, amount);
        return new ResponseEntity<>(answer.getAnswer(), answer.getStatus());

    }

    @Operation(summary = "Редактирует пользователя на основе данных из тела запроса.")
    @PostMapping("/change")
    public ResponseEntity<String> change(
            @Parameter(
                    description = "Токен аутентификации с добавлением в начале Bearer + пробел.",
                    required = true,
                    in = ParameterIn.HEADER,
                    example = "Bearer <токен в виде строки>"
            )
            @RequestHeader("Authorization") String header,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Новые данные пользователя для редактирования.")
            @RequestBody UserForChangeDTO userForChangeDTO,
            BindingResult bindingResult
    ) {


        Answer answer = userService.editProfile(header, userForChangeDTO, bindingResult);
        return new ResponseEntity<>(answer.getAnswer(), answer.getStatus());

    }

    @Operation(summary = "Постранично выводит историю операции пользователя по данным из токена.")
    @GetMapping("/history/{page}/{size}")
    public ResponseEntity<Page<PaymentHistory>> history(
            @Parameter(
                    description = "Токен аутентификации с добавлением в начале Bearer + пробел.",
                    required = true, in = ParameterIn.HEADER,
                    example = "Bearer <токен в виде строки>"
            )
            @RequestHeader("Authorization") String header,
            @Parameter(required = true, description = "Номер страницы.")
            @PathVariable("page") int page,
            @Parameter(required = true, description = "Кол-во записей в рамках страницы.")
            @PathVariable("size") int size
    ) {
        return new ResponseEntity<>(
                userService.getPaymentHistoryByHeader(header, PageRequest.of(page, size)),
                HttpStatus.OK
        );
    }


}
