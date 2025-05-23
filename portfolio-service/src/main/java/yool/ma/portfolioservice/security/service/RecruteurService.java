package yool.ma.portfolioservice.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yool.ma.portfolioservice.dto.MessageResponse;
import yool.ma.portfolioservice.dto.auth.RecruteurRequest;
import yool.ma.portfolioservice.ennum.Role;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecruteurService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> recruteurRegister(RecruteurRequest request) {
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
        user.setRole(Role.RECRUTEUR);

        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setEmail(request.getEmail());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setCompany(request.getCompany());
        profile.setSexe(request.getSexe());
        profile.setAddress(request.getAddress());
        profile.setCentre(request.getCentre());
        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("RECRUTEUR registered successfully!"));
    }


    public List<User> getAllRecruteurs() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.RECRUTEUR)
                .collect(Collectors.toList());
    }

} 