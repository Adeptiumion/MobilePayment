package mobilePayment.mobilePayment.cfg;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфиг Swagger-а.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Mobile Payment API",
                version = "1.0",
                description = "API оплат услуг мобильной связи"
        )
)
@Configuration
public class SwaggerCfg {

    // Убираю из схемы свагера все что связано с объектами для пагинации.
    @Bean
    public OpenApiCustomizer swaggerCustomizer() {
        return openApi -> {
            openApi
                    .getComponents()
                    .getSchemas()
                    .keySet()
                    .removeIf(s -> s.contains("Page") || s.contains("Sort"));
        };
    }
}
