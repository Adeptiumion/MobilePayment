package mobilePayment.mobilePayment.cfg;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфиг маппера.
 */
@Configuration
public class MapperCfg {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
