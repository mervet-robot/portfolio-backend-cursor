package yool.ma.portfolioservice.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yool.ma.portfolioservice.dto.ProfileUpdateRequest;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.repository.ProfileRepository;
import yool.ma.portfolioservice.repository.UserRepository;

import java.util.HashSet;

@Service
public class ProfileService {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;



    public Profile getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return user.getProfile();
    }

    public Profile updateProfile(Long userId, ProfileUpdateRequest profileUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        // Update profile fields
        if (profileUpdateRequest.getFirstName() != null) {
            profile.setFirstName(profileUpdateRequest.getFirstName());
        }
        if (profileUpdateRequest.getLastName() != null) {
            profile.setLastName(profileUpdateRequest.getLastName());
        }
        if (profileUpdateRequest.getPhoneNumber() != null) {
            profile.setPhoneNumber(profileUpdateRequest.getPhoneNumber());
        }
        if (profileUpdateRequest.getDiploma() != null) {
            profile.setDiploma(profileUpdateRequest.getDiploma());
        }
        if (profileUpdateRequest.getProfilePicture() != null) {
            profile.setProfilePicture(profileUpdateRequest.getProfilePicture());
        }
        if (profileUpdateRequest.getBio() != null) {
            profile.setBio(profileUpdateRequest.getBio());
        }
        if (profileUpdateRequest.getSocialLinks() != null) {
            profile.setSocialLinks(new HashSet<>(profileUpdateRequest.getSocialLinks()));
        }
        if (profileUpdateRequest.getAddress() != null) {
            profile.setAddress(profileUpdateRequest.getAddress());
        }
        if (profileUpdateRequest.getCentre() != null) {
            profile.setCentre(profileUpdateRequest.getCentre());
        }

        return profileRepository.save(profile);
    }

    public Profile updateProfilePicture(Long userId, String picturePath) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }

        profile.setProfilePicture(picturePath);
        return profileRepository.save(profile);
    }

}