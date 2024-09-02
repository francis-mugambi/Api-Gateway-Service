package ke.co.emtechhouse.api_gateway_service.Auth;

import ke.co.emtechhouse.api_gateway_service.Config.JwtService;
import ke.co.emtechhouse.api_gateway_service.User.Role;
import ke.co.emtechhouse.api_gateway_service.User.User;
import ke.co.emtechhouse.api_gateway_service.User.UserRepository;
import ke.co.emtechhouse.api_gateway_service.Utills.EntityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository repository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    public EntityResponse register(RegisterRequest request) {
        EntityResponse response = new EntityResponse();
        User user1 = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user1 != null){
            log.info("User exists");
            response.setMessage("User Already exists!!");
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            response.setEntity("");
            return response;
        }else {
            var user = User.builder()
                    .firstname(request.getFirstName())
                    .lastName(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            log.info("Register User: "+user);
            repository.save(user);
            var jwtToken = jwtService.generateToken(user);
            log.info("JWT Token: "+jwtToken);
            response.setMessage("User Created Successfully");
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setEntity(jwtToken);
            return response;
//            return  AuthenticationResponse.builder()
//                    .token(jwtToken)
//                    .build();
        }

    }
    public EntityResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticate: "+request);
        EntityResponse response = new EntityResponse();
        User user1 = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user1 == null) {
            log.info("User not found");
            response.setMessage("Account Access Restricted! Check with the  System Admin");
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            response.setEntity("");
            return  response;
        }else {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            log.info("Authentication ended ....");
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            //log.info("JwtToken: "+jwtToken);
            response.setMessage("User Authenticated Successfully");
            response.setStatusCode(HttpStatus.ACCEPTED.value());
            response.setEntity(jwtToken);
            return  response;
//            return  AuthenticationResponse.builder()
//                    .token(jwtToken)
//                    .build();
        }

    }
}
