package ke.co.emtechhouse.api_gateway_service.Config;

import ke.co.emtechhouse.api_gateway_service.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    @Bean
    public UserDetailsService userDetailsService(){
        logger.info("Initializing userDetailsService bean");
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        logger.info("Initializing authenticationProvider bean");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        logger.info("Initializing AuthenticationManager bean");
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Initializing passwordEncoder bean");
        return new BCryptPasswordEncoder();
    }
}
