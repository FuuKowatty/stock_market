package pl.stock_market.modules.wallet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import pl.stock_market.modules.wallet.exception.NotEnoughMoneyException;

import java.math.BigDecimal;

@Entity
@Getter
class Wallet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long userId;
    BigDecimal saldo;

    public void updateMoney(BigDecimal money) {
        BigDecimal newSaldo = saldo.add(money);
        if (newSaldo.compareTo(BigDecimal.ZERO) < 1) {
            throw new NotEnoughMoneyException();
        }
        saldo = newSaldo;
    }

}
