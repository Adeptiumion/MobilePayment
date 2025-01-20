package mobilePayment.mobilePayment.cfg;

import mobilePayment.mobilePayment.models.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфиг базовых ролей.
 */
@Configuration
public class InitRolesCfg {

    @Bean("user")
    public Role user(){
        Role role = new Role();
        role.setName("USER");
        return role;
    }

    @Bean("admin")
    public Role admin(){
        Role role = new Role();
        role.setName("ADMIN");
        return role;
    }

}
