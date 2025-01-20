package mobilePayment.mobilePayment.models;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Модель пользователя.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Пароль пользователя.
     */
    private String password;
    /**
     * Почта пользователя.
     */
    @Email
    private String email;
    /**
     * ФИО.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name")),
            @AttributeOverride(name = "surname", column = @Column(name = "surname"))
    })
    private FullName fullName;
    /**
     * Дата рождения.
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    /**
     * Пол.
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;
    /**
     * Баланс пользователя.
     */
    private double balance;
    /**
     * Номер телефона.
     */
    @Column(name = "phone_number")
    private String phoneNumber;
    /**
     * Роли пользователя.
     */
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "usersAndRoles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles;
    /**
     * Записи платежей пользователя.
     */
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<PaymentHistory> paymentHistory;

    /**
     * Метод изменения данных пользователя.
     * Меняет только те поля, которые ему передали.
     *
     * @param user пользователь.
     */
    public void change(User user) {
        this.password = user.getPassword() == null ? this.getPassword() : user.getPassword();
        this.email = user.getEmail() == null ? this.getEmail() : user.getEmail();
        this.fullName = user.getFullName() == null ? this.getFullName() : user.getFullName();
        this.dateOfBirth = user.getDateOfBirth() == null ? this.getDateOfBirth() : user.getDateOfBirth();
        this.gender = user.getGender() == null ? this.getGender() : user.getGender();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.phoneNumber);
    }
}
