package yool.ma.portfolioservice.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yool.ma.portfolioservice.dto.PortfolioSummaryDto;
import yool.ma.portfolioservice.dto.ExperienceDetailDto;
import yool.ma.portfolioservice.dto.FormationDetailDto;
import yool.ma.portfolioservice.dto.CertificationDetailDto;
import yool.ma.portfolioservice.dto.ProjectDetailDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LlmService {

    private final RestTemplate restTemplate;
    private final String llmApiUrl = "http://localhost:11434/api/generate"; // Your LLM API endpoint

    public LlmService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateBioFromPortfolioData(PortfolioSummaryDto summaryDto) {
        StringBuilder promptBuilder = new StringBuilder("Based on the following comprehensive portfolio data, generate a concise and professional bio between 20 and 50 words for this person. The bio should highlight key aspects of their profile. Return only the generated bio text, with no introductory phrases like \"Here is a bio:\".\n\nPortfolio Data:\n");

        // Profile Info
        if (summaryDto.getFirstName() != null && !summaryDto.getFirstName().isEmpty()) {
            promptBuilder.append("- Name: ").append(summaryDto.getFirstName());
            if (summaryDto.getLastName() != null && !summaryDto.getLastName().isEmpty()) {
                promptBuilder.append(" ").append(summaryDto.getLastName());
            }
            promptBuilder.append("\n");
        }
        if (summaryDto.getDiploma() != null && !summaryDto.getDiploma().isEmpty()) {
            promptBuilder.append("- Headline/Current Profession: ").append(summaryDto.getDiploma()).append("\n");
        }

        // Experiences
        if (summaryDto.getExperiences() != null && !summaryDto.getExperiences().isEmpty()) {
            promptBuilder.append("\nExperiences:\n");
            for (ExperienceDetailDto exp : summaryDto.getExperiences()) {
                promptBuilder.append("  - Title: ").append(exp.getTitle());
                if (exp.getCompany() != null && !exp.getCompany().isEmpty()) {
                    promptBuilder.append(", Company: ").append(exp.getCompany());
                }
                promptBuilder.append("\n");
            }
        }

        // Latest Formation
        FormationDetailDto latestFormation = summaryDto.getLatestFormation();
        if (latestFormation != null) {
            promptBuilder.append("\nLatest Education:\n");
            promptBuilder.append("  - Degree: ").append(latestFormation.getDegree());
            if (latestFormation.getInstitution() != null && !latestFormation.getInstitution().isEmpty()) {
                promptBuilder.append(", Institution: ").append(latestFormation.getInstitution());
            }
            if (latestFormation.getFieldOfStudy() != null && !latestFormation.getFieldOfStudy().isEmpty()) {
                promptBuilder.append(", Field of Study: ").append(latestFormation.getFieldOfStudy());
            }
            promptBuilder.append("\n");
        }

        // Technical Skills
        if (summaryDto.getTechSkills() != null && !summaryDto.getTechSkills().isEmpty()) {
            promptBuilder.append("\nTechnical Skills: ").append(String.join(", ", summaryDto.getTechSkills())).append("\n");
        }

        // Certifications
        if (summaryDto.getCertifications() != null && !summaryDto.getCertifications().isEmpty()) {
            promptBuilder.append("\nCertifications:\n");
            for (CertificationDetailDto cert : summaryDto.getCertifications()) {
                promptBuilder.append("  - Name: ").append(cert.getName());
                if (cert.getIssuingOrganization() != null && !cert.getIssuingOrganization().isEmpty()) {
                    promptBuilder.append(", Issued by: ").append(cert.getIssuingOrganization());
                }
                promptBuilder.append("\n");
            }
        }

        // Projects
        if (summaryDto.getProjects() != null && !summaryDto.getProjects().isEmpty()) {
            promptBuilder.append("\nProjects:\n");
            for (ProjectDetailDto proj : summaryDto.getProjects()) {
                promptBuilder.append("  - Title: ").append(proj.getTitle());
                if (proj.getDescription() != null && !proj.getDescription().isEmpty()) {
                    promptBuilder.append(" (Description: ").append(proj.getDescription().substring(0, Math.min(proj.getDescription().length(), 50))).append("...)"); // Truncate long descriptions
                }
                promptBuilder.append("\n");
            }
        }
        
        promptBuilder.append("\nBased on all the above, generate the bio.");
        promptBuilder.append("\nGenerated Bio (20-50 words):"); // Reiterating the desired output format for the LLM

        String prompt = promptBuilder.toString();
        System.out.println("DEBUG: LLM Prompt:\n" + prompt); // For debugging the prompt

        Map<String, Object> body = Map.of(
                "model", "tinyllama", // Or your preferred model
                "prompt", prompt,
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(llmApiUrl, entity, Map.class);
            if (response.getBody() != null && response.getBody().get("response") != null) {
                String responseText = ((String) response.getBody().get("response")).trim();
                
                // Clean up potential leading/trailing quotes from LLM response
                if (responseText.startsWith("\"") && responseText.endsWith("\"") && responseText.length() > 1) {
                    responseText = responseText.substring(1, responseText.length() - 1);
                }
                // Remove any "Generated Bio:" or similar prefix if the LLM includes it despite instructions
                String[] prefixesToRemove = {"generated bio:", "bio:", "here is a bio:", "here's a bio:"};
                for (String prefix : prefixesToRemove) {
                    if (responseText.toLowerCase().startsWith(prefix)) {
                        responseText = responseText.substring(prefix.length()).trim();
                        break;
                    }
                }
                return responseText;
            } else {
                System.err.println("LLM response body or response field is null.");
                return "COULD NOT GENERATE BIO";
            }
        } catch (Exception e) {
            System.err.println("Error calling LLM service: " + e.getMessage());
            e.printStackTrace(); // Consider using a proper logger
            return "COULD NOT GENERATE BIO";
        }
    }
} 