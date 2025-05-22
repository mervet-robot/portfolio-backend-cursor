package yool.ma.portfolioservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import yool.ma.portfolioservice.dto.auth.ResponsableRequest;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.security.service.DirecteurService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/directeur")
public class DirecteurController {

    @Autowired
    private DirecteurService directeurService;

    @PostMapping("/responsables")
    public ResponseEntity<?> createResponsable(@Valid @RequestBody ResponsableRequest responsableRequest) {
        return directeurService.createResponsable(responsableRequest);
    }

    @GetMapping("/responsables")
    public ResponseEntity<List<User>> getAllResponsables() {
        return ResponseEntity.ok(directeurService.getAllResponsables());
    }

    @GetMapping("/responsables/{id}")
    public ResponseEntity<User> getResponsableById(@PathVariable Long id) {
        return ResponseEntity.ok(directeurService.getResponsableById(id));
    }

    @PutMapping("/responsables/{id}")
    public ResponseEntity<?> updateResponsable(@PathVariable Long id, @Valid @RequestBody ResponsableRequest responsableRequest) {
        return directeurService.updateResponsable(id, responsableRequest);
    }

    @DeleteMapping("/responsables/{id}")
    public ResponseEntity<?> deleteResponsable(@PathVariable Long id) {
        return directeurService.deleteResponsable(id);
    }
} 