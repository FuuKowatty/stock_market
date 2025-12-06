package pl.stock_market.modules.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    void addToHistory();
    Optional<User> findByEmail(String email);
}
