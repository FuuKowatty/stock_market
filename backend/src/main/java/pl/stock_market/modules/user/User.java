package pl.stock_market.modules.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String login;
    String password;
    String firstName;
    String lastName;
    String email;

    public User() {}

    public User(String login, String password, String firstName, String lastName, String email) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
