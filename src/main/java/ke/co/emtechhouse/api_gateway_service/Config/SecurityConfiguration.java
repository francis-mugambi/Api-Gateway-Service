package ke.co.emtechhouse.api_gateway_service.Config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Initializing securityFilterChain bean");
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
//                                        "/api/v1/auth/**",
                                        "/v3/api-docs",
                                        "/v2/api-docs",
                                        "/swagger-resources/**",
                                        "/swagger-ui/**",
                                        "swagger-ui/index.html*",
                                        "/api/v1/auth/authenticate",
                                        "/webjars/**").permitAll()
                                .anyRequest().authenticated()
                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("").permitAll()
//                        .anyRequest().authenticated()
//                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }
}
