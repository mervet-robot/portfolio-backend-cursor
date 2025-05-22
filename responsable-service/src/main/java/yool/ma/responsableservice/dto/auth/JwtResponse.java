
package yool.ma.responsableservice.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;

    private String firstName;
    private String lastName;



    public JwtResponse(String accessToken, Long id, String username, String email, String role, String firstName, String lastName
    ) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
