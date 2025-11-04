package pl.stock_market.modules.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Entity
class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String login;

    public User() {}
}
