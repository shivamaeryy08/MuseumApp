package ca.mcgill.ecse321.MuseumManagementSystem.dto;

public class VisitorDto {

    private String username;
    private String password;

    public VisitorDto() {}

    public VisitorDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}