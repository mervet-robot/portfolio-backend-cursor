
package yool.ma.portfolioservice.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yool.ma.portfolioservice.dto.MessageResponse;
import yool.ma.portfolioservice.dto.auth.JwtResponse;
import yool.ma.portfolioservice.dto.auth.LoginRequest;
import yool.ma.portfolioservice.dto.auth.RegisterRequest;
import yool.ma.portfolioservice.ennum.Role;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.repository.UserRepository;
import yool.ma.portfolioservice.security.jwt.JwtUtils;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        return new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getProfile().getFirstName(),
                user.getProfile().getLastName()

        );
    }

    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        // First check if user exists with same username OR email (not AND)
        Optional<User> existingUser = userRepository.findByUsernameAndEmail(
                registerRequest.getUsername(),
                registerRequest.getEmail()
        );

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Verify if the provided credentials match the existing user
            if (!user.getEmail().equals(registerRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username or email already in use"));
            }

            // Progressive role assignment logic
            if (user.getRole() == Role.USER) {
                // Upgrade USER to APPRENANT
                user.setPassword(encoder.encode(registerRequest.getPassword()));
                user.setRole(Role.APPRENANT);
                userRepository.save(user);
                return ResponseEntity.ok(new MessageResponse("USER upgraded to APPRENANT successfully!"));
            }
            else if (user.getRole() == Role.APPRENANT) {
                // Upgrade APPRENANT to LAUREAT
                user.setPassword(encoder.encode(registerRequest.getPassword()));
                user.setRole(Role.LAUREAT);
                userRepository.save(user);
                return ResponseEntity.ok(new MessageResponse("APPRENANT upgraded to LAUREAT successfully!"));
            }
            else if (user.getRole() == Role.LAUREAT) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Already registered as LAUREAT"));
            }
        }

        // Standard checks for new registration
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }

        // New user registration - USER by default
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER); // Changed from APPRENANT to USER

        Profile profile = new Profile();
        profile.setFirstName(registerRequest.getFirstName());
        profile.setLastName(registerRequest.getLastName());
        profile.setEmail(registerRequest.getEmail());
        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully as USER!"));
    }


    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
