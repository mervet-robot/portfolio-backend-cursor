package yool.ma.portfolioservice.security.service;

import yool.ma.portfolioservice.dto.LanguageRequest;
import yool.ma.portfolioservice.dto.LanguageResponse;
import yool.ma.portfolioservice.model.Language;
import yool.ma.portfolioservice.model.Profile;
import yool.ma.portfolioservice.repository.LanguageRepository;
import yool.ma.portfolioservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final ProfileRepository profileRepository;

    public LanguageResponse createLanguage(Long profileId, LanguageRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + profileId));

        Language language = new Language();
        mapRequestToLanguage(request, language);
        language.setProfile(profile);

        Language savedLanguage = languageRepository.save(language);
        return mapToResponse(savedLanguage);
    }

    public List<LanguageResponse> getAllLanguagesByProfileId(Long profileId) {
        return languageRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public LanguageResponse getLanguageById(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found with id: " + id));
        return mapToResponse(language);
    }

    @Transactional
    public LanguageResponse updateLanguage(Long id, LanguageRequest request) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found with id: " + id));

        mapRequestToLanguage(request, language);
        Language updatedLanguage = languageRepository.save(language);
        return mapToResponse(updatedLanguage);
    }

    public void deleteLanguage(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new RuntimeException("Language not found with id: " + id);
        }
        languageRepository.deleteById(id);
    }

    private LanguageResponse mapToResponse(Language language) {
        LanguageResponse response = new LanguageResponse();
        response.setId(language.getId());
        response.setName(language.getName());
        response.setProficiency(language.getProficiency());
        response.setCertification(language.getCertification());
        response.setCertificateUrl(language.getCertificateUrl());
        response.setNativeLanguage(language.isNativeLanguage());
        return response;
    }

    private void mapRequestToLanguage(LanguageRequest request, Language language) {
        language.setName(request.getName());
        language.setProficiency(request.getProficiency());
        language.setCertification(request.getCertification());
        language.setCertificateUrl(request.getCertificateUrl());
        language.setNativeLanguage(request.isNativeLanguage());
    }
}
