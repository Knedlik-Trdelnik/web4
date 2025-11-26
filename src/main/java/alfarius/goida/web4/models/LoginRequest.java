package alfarius.goida.web4.models;


import jakarta.enterprise.context.RequestScoped;


public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username) {
        this.username = username;
    }

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
