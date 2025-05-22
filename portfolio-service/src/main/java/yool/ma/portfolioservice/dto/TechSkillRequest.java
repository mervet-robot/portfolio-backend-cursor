package yool.ma.portfolioservice.dto;

import lombok.Data;
import yool.ma.portfolioservice.ennum.SkillLevel;

@Data
public class TechSkillRequest {
    private String name;
    private SkillLevel level;
    private String category;
    private int yearsOfExperience;
    private boolean verified;
}
