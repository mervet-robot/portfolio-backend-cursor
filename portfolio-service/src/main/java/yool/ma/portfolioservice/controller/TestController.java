package yool.ma.portfolioservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yool.ma.portfolioservice.ennum.Role;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    public UserRepository userRepository;


    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/all")
    public List<User> allAccess() {
        return userRepository.findAll();
    }



    @GetMapping("/apprenant")
    public List<User> getAllApprenant() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.APPRENANT)
                .collect(Collectors.toList());
    }




    @GetMapping("/responsable")
    public String responsableAccess() {
        return "Responsable Content.";
    }



    @GetMapping("/directeur")
//    @PreAuthorize("hasRole('DIRECTEUR')")
    public String directeurAccess() {
        return "Directeur Content.";
    }
}
