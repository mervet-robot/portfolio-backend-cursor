package yool.ma.portfolioservice.dto;

import lombok.Data;
import yool.ma.portfolioservice.ennum.Centre;
import java.util.List;

@Data
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;

    private String phoneNumber;
    private String diploma;
    private String profilePicture;
    private String bio;
    private List<String> socialLinks;
    private String address;
    private Centre centre;
}