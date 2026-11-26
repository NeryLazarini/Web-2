package ar.edu.unvime.api_blank.dto;

public class LoginOutputDTO {
    private String token;

    public LoginOutputDTO(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}