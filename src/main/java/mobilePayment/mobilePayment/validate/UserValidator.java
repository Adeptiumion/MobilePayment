package mobilePayment.mobilePayment.validate;

import mobilePayment.mobilePayment.models.User;
import mobilePayment.mobilePayment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Валидатор User для регистрации.
 */
@Component
public class UserValidator implements Validator {

    /**
     * Флаг указывающий нужно ли проверять наличие аккаунта с предложенным номером.
     */
    private static boolean forLogin = true;
    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;

        User fromBase = userRepository.getUserByPhoneNumber(
                user.getPhoneNumber()
        );


        if (forLogin) {
            // Проверка на уже имеющийся аккаунт с данным номером.
            if (fromBase != null) {
                errors.rejectValue(
                        "phoneNumber",
                        "",
                        "Пользователь с данным номером уже зарегистрирован!"
                );
                return;
            }
        }

        String phoneNumber = user.getPhoneNumber();

        // Проверка номера на существование.
        if (phoneNumber == null) {
            errors.rejectValue(
                    "phoneNumber",
                    "",
                    "Вы не передали номер!"
            );
            return;
        }

        // Проверка начала номера.
        if (!phoneNumber.startsWith("89")) {
            errors.rejectValue("phoneNumber", "", "Номер телефона должен начинаться с 89!");
            return;
        }

        // Проверка на наличие инородных символов.
        if (phoneNumber.matches(".*\\D.*")) {
            errors.rejectValue("phoneNumber", "", "Номер телефона должен состоять только цифр!");
            return;
        }

        // Проверка на длину номера.
        if (phoneNumber.length() != 11)
            errors.rejectValue("phoneNumber", "", "Номер телефона должен состоять из 11 цифр!");


    }

    /**
     * Метод меняющий значение флага проверки номера на наличие в базе.
     *
     * @param forLogin нужно проверять или нет.
     */
    public static void setForLogin(boolean forLogin) {
        UserValidator.forLogin = forLogin;
    }
}
