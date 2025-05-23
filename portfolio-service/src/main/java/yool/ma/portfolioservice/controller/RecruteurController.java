package yool.ma.portfolioservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import yool.ma.portfolioservice.dto.auth.RecruteurRequest;
import yool.ma.portfolioservice.security.service.RecruteurService;

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
} 