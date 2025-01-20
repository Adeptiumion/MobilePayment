package mobilePayment.mobilePayment.repository;

import mobilePayment.mobilePayment.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Слой репозитория для пользователя.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Метод возвращающий пользователя по номеру телефона.
     * @param phoneNumber номер телефона.
     * @return пользователь.
     */
    @EntityGraph(attributePaths = {"roles"}) // Ну мне нужна подгрузка ролей именно на этом методе.
    User getUserByPhoneNumber(String phoneNumber);
}
