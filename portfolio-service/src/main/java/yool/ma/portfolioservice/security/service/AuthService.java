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
        user.setRole(Role.USER); // New users are always USER

        Profile profile = new Profile();
        profile.setFirstName(registerRequest.getFirstName());
        profile.setLastName(registerRequest.getLastName());
        profile.setEmail(registerRequest.getEmail());
        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully as USER!"));
    }

    public ResponseEntity<?> registerApprenantLaureat(RegisterRequest registerRequest) {
        Optional<User> existingUserOptional = userRepository.findByUsernameAndEmail(
                registerRequest.getUsername(),
                registerRequest.getEmail()
        );

        if (!existingUserOptional.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User not found with the provided username and email. Please register as a basic user first or check credentials."));
        }

        User user = existingUserOptional.get();

        // Ensure the request's email also matches, as findByUsernameAndEmail checks both.
        // This is more of a sanity check here given the repository method.
        if (!user.getEmail().equals(registerRequest.getEmail())) {
            // This case should ideally not be hit if findByUsernameAndEmail worked as expected
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Credentials mismatch."));
        }

        // Update password if a new one is provided.
        // Consider if password matching the old one should be enforced or if this implies a password reset/update.
        // For now, we'll update it like in the original logic.
        if (registerRequest.getPassword() != null && !registerRequest.getPassword().isEmpty()) {
            if (!encoder.matches(registerRequest.getPassword(), user.getPassword())) {
                 user.setPassword(encoder.encode(registerRequest.getPassword()));
            }
        }


        // Progressive role assignment logic
        if (user.getRole() == Role.USER) {
            user.setRole(Role.APPRENANT);
            // Update profile details
            user.getProfile().setFirstName(registerRequest.getFirstName());
            user.getProfile().setLastName(registerRequest.getLastName());
            // Email is tied to the user, profile email should ideally match
            // user.getProfile().setEmail(registerRequest.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("USER successfully upgraded/registered as APPRENANT!"));
        } else if (user.getRole() == Role.APPRENANT) {
            user.setRole(Role.LAUREAT);
            // Update profile details
            user.getProfile().setFirstName(registerRequest.getFirstName());
            user.getProfile().setLastName(registerRequest.getLastName());
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("APPRENANT successfully upgraded/registered as LAUREAT!"));
        } else if (user.getRole() == Role.LAUREAT) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is already registered as LAUREAT. No further upgrades possible."));
        } else {
            // Handle cases like ADMIN or other roles if they exist and this flow is accessed
             return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User has an unsupported role for this operation (" + user.getRole().name() + ")."));
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
