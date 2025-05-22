package yool.ma.portfolioservice.repository;

import yool.ma.portfolioservice.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import yool.ma.portfolioservice.ennum.ProficiencyLevel;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findByProfileId(Long profileId);
    List<Language> findByProficiency(ProficiencyLevel proficiency);
    List<Language> findByNativeLanguage(boolean nativeLanguage);
}