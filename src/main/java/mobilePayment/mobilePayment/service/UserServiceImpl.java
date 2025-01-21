package mobilePayment.mobilePayment.service;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import mobilePayment.mobilePayment.component.Authenticator;
import mobilePayment.mobilePayment.component.JWTGenerator;
import mobilePayment.mobilePayment.dto.BalanceDTO;
import mobilePayment.mobilePayment.dto.UserDTO;
import mobilePayment.mobilePayment.dto.UserForChangeDTO;
import mobilePayment.mobilePayment.models.Answer;
import mobilePayment.mobilePayment.models.PaymentHistory;
import mobilePayment.mobilePayment.models.Role;
import mobilePayment.mobilePayment.models.User;
import mobilePayment.mobilePayment.repository.PaymentHistoryRepository;
import mobilePayment.mobilePayment.repository.RoleRepository;
import mobilePayment.mobilePayment.repository.UserRepository;
import mobilePayment.mobilePayment.validate.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Слой сервиса пользователя.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Role adminRole;
    private final Role userRole;
    private final Authenticator authenticator;
    private final JWTGenerator generator;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    private static final int DEFAULT_BALANCE = 1000;

    @Autowired
    public UserServiceImpl(PaymentHistoryRepository paymentHistoryRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, @Qualifier("admin") Role adminRole, @Qualifier("user") Role userRole, Authenticator authenticator, JWTGenerator generator, ModelMapper modelMapper, UserValidator userValidator) {
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRole = adminRole;
        this.userRole = userRole;
        this.authenticator = authenticator;
        this.generator = generator;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    /**
     * Метод возвращающий пользователя по номеру телефона.
     *
     * @param phoneNumber номер телефона.
     * @return пользователь.
     */
    @Override
    @Nullable
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.getUserByPhoneNumber(phoneNumber);
    }


    /**
     * Метод регистрации пользователя.
     *
     * @param userDTO       кандидат на регистрацию.
     * @param bindingResult результат валидации.
     * @return {@link Answer}
     */
    @Override
    @Transactional
    public Answer registry(UserDTO userDTO, BindingResult bindingResult) {

        User user = modelMapper.map(userDTO, User.class);

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return new Answer(HttpStatus.BAD_REQUEST, getValidatorErrorMessage(bindingResult));

        // Инициализирую роли.
        user.setRoles(new HashSet<>());

        // Коли еще нет аккаунта с таким номером:
        if (userRepository.getUserByPhoneNumber(user.getPhoneNumber()) == null) {
            // ТЗ: "После регистрации на баланс пользователя зачислять 1000 рублей."
            user.setBalance(DEFAULT_BALANCE);
        }

        // Шифрую пароль.
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        // Даю стандартную роль.
        user.getRoles().add(roleRepository.getByName(userRole.getName()));

        userRepository.save(user);

        return new Answer(HttpStatus.OK, generator.generate(userDTO.getPhoneNumber(), userDTO.getPhoneNumber()));
    }

    /**
     * Метод перевода денег на другой аккаунт.
     *
     * @param header                 заголовок с токеном, из которого получим номер телефона отправителя.
     * @param phoneNumberOfRecipient номер телефона получателя.
     * @param amount                 сумма перевода.
     * @return {@link Answer}
     */
    @Override
    @Transactional
    public Answer payment(String header, String phoneNumberOfRecipient, double amount) {

        String phoneNumberOfSender = generator.getPhoneNumberByHeader(header);

        User recipientUser = userRepository.getUserByPhoneNumber(phoneNumberOfRecipient);
        User senderUser = userRepository.getUserByPhoneNumber(phoneNumberOfSender);


        if (amount <= senderUser.getBalance()) {

            senderUser.setBalance(senderUser.getBalance() - amount);
            recipientUser.setBalance(recipientUser.getBalance() + amount);

            if (senderUser.getPaymentHistory() == null) {
                senderUser.setPaymentHistory(new ArrayList<>());
            }


            PaymentHistory paymentHistory = new PaymentHistory(phoneNumberOfRecipient, new Date(), amount, senderUser);
            paymentHistoryRepository.save(paymentHistory);

            paymentHistory.setAmount(amount);
            senderUser.getPaymentHistory().add(paymentHistory);

            userRepository.save(recipientUser);
            userRepository.save(senderUser);

            return new Answer(HttpStatus.OK, "Перевод прошел!");

        }

        return new Answer(HttpStatus.BAD_REQUEST, "Не хватило денег!");
    }


    /**
     * Метод редактирования пользовательских данных.
     *
     * @param header        заголовок с токеном, из которого получим номер телефона пользователя. (которого будем менять)
     * @param newUser       данные для перезаписи.
     * @param bindingResult результат валидации.
     * @return {@link Answer}
     */
    @Override
    @Transactional
    public Answer editProfile(String header, UserForChangeDTO newUser, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return new Answer(HttpStatus.BAD_REQUEST, getValidatorErrorMessage(bindingResult));

        User currentUser = userRepository.getUserByPhoneNumber(generator.getPhoneNumberByHeader(header));

        // В этом методе уже предусмотрена отмена изменений на null поля.
        currentUser.change(modelMapper.map(newUser, User.class));
        userRepository.save(currentUser);

        return new Answer(HttpStatus.OK, "Успешное изменение!");
    }


    /**
     * Метод отправки истории денежных операций.
     *
     * @param header   заголовок с токеном, из которого получим номер телефона пользователя. (историю которого мы будем отправлять)
     * @param pageable настройки пагинации.
     * @return список {@link PaymentHistory}.
     */
    @Override
    public Page<PaymentHistory> getPaymentHistoryByHeader(String header, Pageable pageable) {
        return paymentHistoryRepository.findAllByOwner(userRepository.getUserByPhoneNumber(generator.getPhoneNumberByHeader(header)), pageable);
    }

    /**
     * Метод аутентификации пользователя. (При входе, когда токен протух.)
     *
     * @param userDTO       данных для входа.
     * @param bindingResult результат валидации.
     * @return {@link Answer}
     */
    @Override
    public Answer authenticate(UserDTO userDTO, BindingResult bindingResult) {
        UserValidator.setForLogin(false);
        userValidator.validate(modelMapper.map(userDTO, User.class), bindingResult);
        UserValidator.setForLogin(true);

        if (bindingResult.hasErrors())
            return new Answer(HttpStatus.BAD_REQUEST, getValidatorErrorMessage(bindingResult));

        return authenticator.authenticate(userDTO.getPhoneNumber(), userDTO.getPassword()) ? new Answer(HttpStatus.OK, generator.generate(userDTO.getPhoneNumber(), userDTO.getPassword())) : new Answer(HttpStatus.UNAUTHORIZED, "Неверные данные пользователя!");
    }

    @Override
    public BalanceDTO getBalance(String header) {
        User user = getUserByPhoneNumber(generator.getPhoneNumberByHeader(header));
        return modelMapper.map(user, BalanceDTO.class);
    }


    /**
     * Служебный метод для форматирования ошибки валидации в строку.
     *
     * @param bindingResult {@link BindingResult}
     * @return сообщение ошибки.
     */
    private static String getValidatorErrorMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors().getFirst().getDefaultMessage();
    }

}
