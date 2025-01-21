package mobilePayment.mobilePayment.repository;

import mobilePayment.mobilePayment.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Слой репозитория для ролей пользователей.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * Метод возвращающий роль по ее имени.
     * @param name имя роли.
     * @return роль.
     */
    Role findByName(String name);

    Role getByName(String name);
}
