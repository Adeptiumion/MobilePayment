package mobilePayment.mobilePayment.repository;

import mobilePayment.mobilePayment.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Слой репозитория для пользователя.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Метод возвращающий пользователя по номеру телефона.
     *
     * @param phoneNumber номер телефона.
     * @return пользователь.
     */
    @Query("Select u from User u left join fetch u.roles where u.phoneNumber=:phone")
    User getUserByPhoneNumber(@Param("phone") String phoneNumber);
}
