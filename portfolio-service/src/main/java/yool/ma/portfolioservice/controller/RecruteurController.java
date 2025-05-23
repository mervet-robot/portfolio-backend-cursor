package yool.ma.portfolioservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import yool.ma.portfolioservice.dto.auth.RecruteurRequest;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.security.service.RecruteurService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recruteur")
public class RecruteurController {

    @Autowired
    private RecruteurService recruteurService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> recruteurRegister(@Valid @RequestBody RecruteurRequest recruteurRequest) {
        return recruteurService.recruteurRegister(recruteurRequest);
    }

    @GetMapping("/recruteurs")
    public ResponseEntity<List<User>> getAllRecruteurs() {
        return ResponseEntity.ok(recruteurService.getAllRecruteurs());
    }

} 