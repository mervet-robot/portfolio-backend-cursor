package yool.ma.portfolioservice.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import yool.ma.portfolioservice.ennum.Role;
import yool.ma.portfolioservice.ennum.Centre;

@Data
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private String sexe;
    private String address;
    private Centre centre;

    private Role role;

    private String firstName;
    private String lastName;

    //    @NotBlank
//    @Size(min = 10, max = 15) // Assuming a standard phone number length
    private String phoneNumber;
}
