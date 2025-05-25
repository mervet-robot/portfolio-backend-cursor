package yool.ma.portfolioservice.dto;

public class ProjectDetailDto {
    private String title;
    private String description;

    // Constructors
    public ProjectDetailDto() {
    }

    public ProjectDetailDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 