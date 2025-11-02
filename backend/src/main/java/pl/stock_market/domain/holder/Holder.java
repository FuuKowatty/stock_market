package pl.stock_market.domain.holder;

import jakarta.persistence.*;
import lombok.Getter;
import pl.stock_market.domain.holder.quantity.HolderQuantity;
import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;

@Getter
@Entity
public class Holder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Portfolio portfolio;
    @Embedded
    private final HolderQuantity quantity = new HolderQuantity();

    public Holder() {}

    public void addQuantity(BigDecimal quantityToAdd) {
        quantity.addQuantity(quantityToAdd);
    }

}
