package yool.ma.portfolioservice.dto;

import lombok.Data;
import yool.ma.portfolioservice.ennum.CertificationType;
import yool.ma.portfolioservice.ennum.ProficiencyLevel;

@Data
public class LanguageRequest {
    private String name;
    private ProficiencyLevel proficiency;
    private CertificationType certification;
    private String certificateUrl;
    private boolean nativeLanguage;
}
