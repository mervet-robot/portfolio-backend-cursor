package yool.ma.portfolioservice.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yool.ma.portfolioservice.dto.MessageResponse;
import yool.ma.portfolioservice.dto.auth.DirecteurRequest;
import yool.ma.portfolioservice.dto.auth.ResponsableRequest;
import yool.ma.portfolioservice.ennum.Role;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DirecteurService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> directeurRegister(DirecteurRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }

        // Create new Directeur
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.DIRECTEUR);

        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setEmail(request.getEmail());
        profile.setPhoneNumber(request.getPhoneNumber());

        profile.setSexe(request.getSexe());
        profile.setAddress(request.getAddress());
        profile.setCentre(request.getCentre());

        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Directeur registered successfully!"));
    }

    public ResponseEntity<?> createResponsable(ResponsableRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }

        // Create new Responsable
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.RESPONSABLE);

        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setEmail(request.getEmail());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setDepartment(request.getDepartment());
        profile.setSexe(request.getSexe());
        profile.setAddress(request.getAddress());
        profile.setCentre(request.getCentre());

        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Responsable registered successfully!"));
    }

    public List<User> getAllResponsables() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.RESPONSABLE)
                .collect(Collectors.toList());
    }

    public User getResponsableById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsable not found with id: " + id));

        if (user.getRole() != Role.RESPONSABLE) {
            throw new RuntimeException("User with id " + id + " is not a Responsable");
        }

        return user;
    }

    public ResponseEntity<?> updateResponsable(Long id, ResponsableRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsable not found with id: " + id));

        if (user.getRole() != Role.RESPONSABLE) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User with id " + id + " is not a Responsable"));
        }

        // Check if username is taken by someone else
        if (!user.getUsername().equals(request.getUsername()) &&
                userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken"));
        }

        // Check if email is taken by someone else
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }

        // Update user information
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // Only update password if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(request.getPassword()));
        }

        // Update profile information
        Profile profile = user.getProfile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setEmail(request.getEmail());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setDepartment(request.getDepartment());

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Responsable updated successfully!"));
    }

    public ResponseEntity<?> deleteResponsable(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Responsable not found with id: " + id));

        if (user.getRole() != Role.RESPONSABLE) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User with id " + id + " is not a Responsable"));
        }

        userRepository.delete(user);
        return ResponseEntity.ok(new MessageResponse("Responsable deleted successfully!"));
    }
} 