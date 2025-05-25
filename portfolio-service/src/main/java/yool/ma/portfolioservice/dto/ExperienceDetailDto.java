package yool.ma.portfolioservice.dto;

public class ExperienceDetailDto {
    private String title;
    private String company;

    // Constructors
    public ExperienceDetailDto() {
    }

    public ExperienceDetailDto(String title, String company) {
        this.title = title;
        this.company = company;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
} 