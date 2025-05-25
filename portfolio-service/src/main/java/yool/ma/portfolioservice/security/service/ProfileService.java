package yool.ma.portfolioservice.security.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yool.ma.portfolioservice.dto.ProfileUpdateRequest;
import yool.ma.portfolioservice.dto.SocialLinkRequestDTO;
import yool.ma.portfolioservice.dto.SocialLinkResponseDTO;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.model.SocialLink;
import yool.ma.portfolioservice.model.User;
import yool.ma.portfolioservice.repository.ProfileRepository;
import yool.ma.portfolioservice.repository.SocialLinkRepository;
import yool.ma.portfolioservice.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SocialLinkRepository socialLinkRepository;

    private Profile getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Profile profile = user.getProfile();
        if (profile == null) {
            throw new EntityNotFoundException("Profile not found for user id: " + userId);
        }
        return profile;
    }

    private SocialLinkResponseDTO toSocialLinkResponseDTO(SocialLink socialLink) {
        SocialLinkResponseDTO dto = new SocialLinkResponseDTO();
        dto.setId(socialLink.getId());
        dto.setPlatform(socialLink.getPlatform());
        dto.setUrl(socialLink.getUrl());
        return dto;
    }

    @Transactional(readOnly = true)
    public Profile getProfile(Long userId) {
        return getUserProfile(userId);
    }

    @Transactional
    public Profile updateProfile(Long userId, ProfileUpdateRequest profileUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }

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
        if (profileUpdateRequest.getBio() != null) {
            profile.setBio(profileUpdateRequest.getBio());
        }
        if (profileUpdateRequest.getAddress() != null) {
            profile.setAddress(profileUpdateRequest.getAddress());
        }
        if (profileUpdateRequest.getCentre() != null) {
            profile.setCentre(profileUpdateRequest.getCentre());
        }

        return profileRepository.save(profile);
    }

    @Transactional
    public Profile updateProfilePicture(Long userId, String picturePath) {
        Profile profile = getUserProfile(userId);
        profile.setProfilePicture(picturePath);
        return profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public List<SocialLinkResponseDTO> getSocialLinks(Long userId) {
        Profile profile = getUserProfile(userId);
        return profile.getSocialLinks().stream()
                .map(this::toSocialLinkResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SocialLinkResponseDTO addSocialLink(Long userId, SocialLinkRequestDTO requestDTO) {
        Profile profile = getUserProfile(userId);
        SocialLink newSocialLink = new SocialLink();
        newSocialLink.setPlatform(requestDTO.getPlatform());
        newSocialLink.setUrl(requestDTO.getUrl());
        newSocialLink.setProfile(profile);
        
        profile.getSocialLinks().add(newSocialLink);
        SocialLink savedLink = socialLinkRepository.save(newSocialLink);
        profileRepository.save(profile);
        return toSocialLinkResponseDTO(savedLink);
    }

    @Transactional
    public SocialLinkResponseDTO updateSocialLink(Long userId, Long socialLinkId, SocialLinkRequestDTO requestDTO) {
        Profile profile = getUserProfile(userId);
        SocialLink socialLink = socialLinkRepository.findById(socialLinkId)
                .orElseThrow(() -> new EntityNotFoundException("SocialLink not found with id: " + socialLinkId));

        if (!socialLink.getProfile().getId().equals(profile.getId())) {
            throw new SecurityException("SocialLink does not belong to the specified user profile.");
        }

        socialLink.setPlatform(requestDTO.getPlatform());
        socialLink.setUrl(requestDTO.getUrl());
        SocialLink updatedLink = socialLinkRepository.save(socialLink);
        return toSocialLinkResponseDTO(updatedLink);
    }

    @Transactional
    public void deleteSocialLink(Long userId, Long socialLinkId) {
        Profile profile = getUserProfile(userId);
        SocialLink socialLink = socialLinkRepository.findById(socialLinkId)
            .orElseThrow(() -> new EntityNotFoundException("SocialLink not found with id: " + socialLinkId));

        if (!socialLink.getProfile().getId().equals(profile.getId())) {
            throw new SecurityException("SocialLink does not belong to the specified user profile.");
        }
        
        socialLinkRepository.delete(socialLink);
    }
}