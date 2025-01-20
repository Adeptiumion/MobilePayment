package mobilePayment.mobilePayment.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Модель роли пользователя.
 */
@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    /**
     * Уникальный идентификатор роли.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Название роли.
     */
    private String name;
    /**
     * Пользователи, кому принадлежит роль.
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> owners;

    @Override
    public String getAuthority() {
        return name;
    }
}
