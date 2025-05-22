
package yool.ma.responsableservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yool.ma.responsableservice.dto.MessageResponse;
import yool.ma.responsableservice.dto.auth.JwtResponse;
import yool.ma.responsableservice.dto.auth.LoginRequest;
import yool.ma.responsableservice.dto.auth.RegisterRequest;
import yool.ma.responsableservice.ennum.Role;
import yool.ma.responsableservice.model.Profile;
import yool.ma.responsableservice.model.User;
import yool.ma.responsableservice.repository.UserRepository;
import yool.ma.responsableservice.security.jwt.JwtUtils;


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

//    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
//        // First check if user exists with same username OR email (not AND)
//        Optional<User> existingUser = userRepository.findByUsernameAndEmail(
//                registerRequest.getUsername(),
//                registerRequest.getEmail()
//        );
//
//        if (existingUser.isPresent()) {
//            User user = existingUser.get();
//
//            // Verify if the provided credentials match the existing user
//            if (!user.getEmail().equals(registerRequest.getEmail())) {
//                return ResponseEntity
//                        .badRequest()
//                        .body(new MessageResponse("Error: Username or email already in use"));
//            }
//
//
//        }
//
//        // Standard checks for new registration
//        if (userRepository.existsByUsername(registerRequest.getUsername())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken"));
//        }
//
//        if (userRepository.existsByEmail(registerRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use"));
//        }
//
//        // New user registration - USER by default
//        User user = new User();
//        user.setUsername(registerRequest.getUsername());
//        user.setEmail(registerRequest.getEmail());
//        user.setPassword(encoder.encode(registerRequest.getPassword()));
//        user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : Role.RESPONSABLE); // Changed from APPRENANT to USER
//
////        Profile profile = new Profile();
////        profile.setFirstName(registerRequest.getFirstName());
////        profile.setLastName(registerRequest.getLastName());
////        profile.setEmail(registerRequest.getEmail());
////        profile.setUser(user);
////        user.setProfile(profile);
//
//        userRepository.save(user);
//        return ResponseEntity.ok(new MessageResponse("User registered successfully as USER!"));
//    }


public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
    // Check if user exists with same username AND email
    Optional<User> existingUser = userRepository.findByUsernameAndEmail(
            registerRequest.getUsername(),
            registerRequest.getEmail()
    );

    if (existingUser.isPresent()) {
        User user = existingUser.get();

        if (!user.getEmail().equals(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username or email already in use"));
        }

        // Return existing user's role
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: User already exists with role: " + user.getRole().name()));
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

    // Validate the requested role
    Role requestedRole = registerRequest.getRole() != null ? registerRequest.getRole() : Role.RESPONSABLE;

    // New user registration
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(encoder.encode(registerRequest.getPassword()));
    user.setRole(requestedRole);

            Profile profile = new Profile();
            profile.setFirstName(registerRequest.getFirstName());
            profile.setLastName(registerRequest.getLastName());
            profile.setEmail(registerRequest.getEmail());
            profile.setUser(user);
            user.setProfile(profile);

    userRepository.save(user);

    // Return success message with the assigned role
    return ResponseEntity.ok(new MessageResponse(
            "User registered successfully as " + requestedRole.name() + "!"
    ));
}

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
