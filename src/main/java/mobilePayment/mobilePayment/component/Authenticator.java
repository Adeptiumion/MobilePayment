package mobilePayment.mobilePayment.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;


/**
 * Конфиг аутентификации.
 */
@Slf4j
@Component
public class Authenticator {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public Authenticator(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Метод аутентификации.
     *
     * @param login    логин. (В рамках приложения - это номер телефона)
     * @param password пароль.
     * @return прошла ли аутентификация или нет.
     */
    public boolean authenticate(String login, String password) {

        // В UPAT кладу логин и пароль.
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(login, password);

        try {
            authenticationManager.authenticate(token);
            // Атентификация НЕ пройдет при неверных данных.
        } catch (Exception e) {
            return false;
        }

        return true;

    }
}
