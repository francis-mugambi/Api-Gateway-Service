package ke.co.emtechhouse.api_gateway_service.Config;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        log.info("Swagger documentation ....");
        return new OpenAPI()
                .info(new Info()
                        .title("SSO API from E&M Technology House LTD")
                            .description("SWIFT CONVERTER SOLUTION.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("E&M")
                                .url("www.emtechhouse.co.ke/")
                                .email("developer@emtechhouse.co.ke"))
                        .license(new License()
                                .name("License of API")
                                .url("https.emtechhouse.co.ke")));
    }
}
