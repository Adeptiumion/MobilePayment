package mobilePayment.mobilePayment.service;

import jakarta.annotation.Nullable;
import mobilePayment.mobilePayment.models.User;
import mobilePayment.mobilePayment.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Выношу реализацию {@link UserDetailsService} в отдельный класс для избежания циклической зависимости с фильтром.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Реализация поведения UserDetailsService.
     *
     * @param username логин.
     * @return {@link UserDetails}.
     * @throws UsernameNotFoundException
     * @see UserDetails
     * @see User
     */
    @Override
    @Nullable
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByPhoneNumber(username);
    }
}
