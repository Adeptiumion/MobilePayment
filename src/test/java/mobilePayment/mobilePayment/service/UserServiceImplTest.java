package mobilePayment.mobilePayment.service;

import mobilePayment.mobilePayment.component.Authenticator;
import mobilePayment.mobilePayment.component.JWTGenerator;
import mobilePayment.mobilePayment.dto.UserDTO;
import mobilePayment.mobilePayment.models.Answer;
import mobilePayment.mobilePayment.models.Role;
import mobilePayment.mobilePayment.models.User;
import mobilePayment.mobilePayment.repository.PaymentHistoryRepository;
import mobilePayment.mobilePayment.repository.RoleRepository;
import mobilePayment.mobilePayment.repository.UserRepository;
import mobilePayment.mobilePayment.validate.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;


@DisplayName("Тесты UserServiceImpl.")
@ExtendWith({MockitoExtension.class})
public class UserServiceImplTest {

    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Role adminRole;
    @Mock
    private Role userRole;
    @Mock
    private Authenticator authenticator;
    @Mock
    private JWTGenerator generator;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserValidator userValidator;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Тест получения номера по хедеру. (Позитивный кейс)")
    void getUserByPhoneNumberPositive() {
        User expected = Mockito.mock(User.class);

        Mockito.when(this.userRepository
                .getUserByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(expected);
        User actual = userRepository
                .getUserByPhoneNumber(ArgumentMatchers.anyString());

        Mockito.verify(this.userRepository).getUserByPhoneNumber(ArgumentMatchers.anyString());

        Assertions.assertAll(new Executable[]{
                () -> Assertions.assertEquals(expected, actual),
                () -> Assertions.assertNotNull(expected)
        });
    }

    @Test
    @DisplayName("Тест получения номера по хедеру. (Негативный кейс)")
    void getUserByPhoneNumberNegative() {
        User expected = null;

        Mockito.when(this.userRepository
                .getUserByPhoneNumber(ArgumentMatchers.anyString())).thenReturn(null);
        User actual = userRepository
                .getUserByPhoneNumber(ArgumentMatchers.anyString());

        Mockito.verify(this.userRepository).getUserByPhoneNumber(ArgumentMatchers.anyString());

        Assertions.assertAll(new Executable[]{
                () -> Assertions.assertEquals(expected, actual),
                () -> Assertions.assertNull(expected)
        });
    }

    @Test
    @DisplayName("Тест регистрации. (Позитивный кейс)")
    void registryPositive() {

        UserDTO forSave = Mockito.mock(UserDTO.class);
        User expected = Mockito.mock(User.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);

        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(this.modelMapper.map(forSave, User.class)).thenReturn(expected);
        Mockito.when(this.userRepository.save(expected)).thenReturn(expected);
        ;

        User actualForMap = modelMapper.map(forSave, User.class);
        User actualForSave = userRepository.save(expected);
        boolean hasErrors = bindingResult.hasErrors();

        Assertions.assertAll(new Executable[]{
                () -> Assertions.assertEquals(expected, actualForMap),
                () -> Assertions.assertEquals(expected, actualForSave),
                () -> Assertions.assertNotNull(expected),
                () -> Assertions.assertEquals(hasErrors, false),
        });

    }

    @Test
    @DisplayName("Тест регистрации. (Негативный кейс)")
    void registryNegative() {
        UserDTO forSave = Mockito.mock(UserDTO.class);
        User expected = null;
        BindingResult bindingResult = Mockito.mock(BindingResult.class);

        Mockito.when(bindingResult.hasErrors()).thenReturn(true);


        boolean hasErrors = bindingResult.hasErrors();

        Assertions.assertAll(new Executable[]{
                () -> Assertions.assertNull(expected),
                () -> Assertions.assertEquals(hasErrors, true),
        });
    }

}



