package yool.ma.portfolioservice.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yool.ma.portfolioservice.dto.PortfolioSummaryDto;

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
        StringBuilder promptBuilder = new StringBuilder("Based on the following data, generate a concise and professional bio between 20 and 50 words. Return only the generated bio text:\n");

        if (summaryDto.getDiploma() != null && !summaryDto.getDiploma().isEmpty()) {
            promptBuilder.append("- Profession/Current Role: ").append(summaryDto.getDiploma()).append("\n");
        }
        if (summaryDto.getExperienceTitle() != null && !summaryDto.getExperienceTitle().isEmpty()) {
            promptBuilder.append("- Most Recent Job Title: ").append(summaryDto.getExperienceTitle());
            if (summaryDto.getExperienceCompany() != null && !summaryDto.getExperienceCompany().isEmpty()) {
                promptBuilder.append(" at ").append(summaryDto.getExperienceCompany());
            }
            promptBuilder.append("\n");
        }
        if (summaryDto.getSkills() != null && !summaryDto.getSkills().isEmpty()) {
            promptBuilder.append("- Key Skills: ").append(String.join(", ", summaryDto.getSkills())).append("\n");
        }
        if (summaryDto.getDegree() != null && !summaryDto.getDegree().isEmpty()) {
            promptBuilder.append("- Education: ").append(summaryDto.getDegree());
            if (summaryDto.getFieldOfStudy() != null && !summaryDto.getFieldOfStudy().isEmpty()) {
                promptBuilder.append(" in ").append(summaryDto.getFieldOfStudy());
            }
            promptBuilder.append("\n");
        }
        
        promptBuilder.append("\nGenerated Bio:");


        String prompt = promptBuilder.toString();

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
                 // Remove any "Generated Bio:" prefix if the LLM includes it despite instructions
                if (responseText.toLowerCase().startsWith("generated bio:")) {
                    responseText = responseText.substring("generated bio:".length()).trim();
                }
                return responseText;
            } else {
                System.err.println("LLM response body or response field is null.");
                return "COULD NOT GENERATE BIO";
            }
        } catch (Exception e) {
            System.err.println("Error calling LLM service: " + e.getMessage());
            e.printStackTrace();
            return "COULD NOT GENERATE BIO";
        }
    }
} 