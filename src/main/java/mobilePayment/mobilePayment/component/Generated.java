package mobilePayment.mobilePayment.component;

import java.util.Map;

/**
 * Интерфейс описывающий базовое поведение генератора токенов.
 */
public interface Generated {
    /**
     * Метод генерирующий токен на основе двух субъектов.
     * @param firstSubject первый субъект.
     * @param secondSubject второй субъект.
     * @return токен в строковом виде.
     */
    String generate(String firstSubject, String secondSubject);

    /**
     * Метод валидации токена, возвращающий карту основных данных "входа".
     * @param token принимаемый токен.
     * @return карта основных данных "входа".
     */
    Map<String, String> validate(String token);
}
