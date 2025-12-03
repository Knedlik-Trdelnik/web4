package alfarius.goida.web4.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "web_users")
public class User implements Serializable {
    @Column(nullable = false,
            unique=true)
    private String login;
    @Column(nullable = false)
    private String password;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    public User() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
