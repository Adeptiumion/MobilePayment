package mobilePayment.mobilePayment.cfg;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mobilePayment.mobilePayment.component.Authenticator;
import mobilePayment.mobilePayment.component.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Конфиг безопасности.
 */
@Slf4j
@Setter
@Configuration
@EnableWebSecurity
@PropertySource("classpath:jwt.properties")
public class SecurityCfg {

    /**
     * Ендпоинты разрешенные без токена.
     */
    @Value("${jwt.allowed}")
    private String[] allowedEndpoints;
    /**
     * Фильтр валидирующий токены.
     */
    private final JWTFilter filter;

    @Autowired
    public SecurityCfg(JWTFilter filter) {
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> {

                            auth
                                    .requestMatchers(allowedEndpoints).permitAll()
                                    .anyRequest().authenticated();
                        }
                )
                .sessionManagement(
                        sm -> sm
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Бин для {@link Authenticator}.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

}
