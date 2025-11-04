package pl.stock_market.modules.holder;

import jakarta.persistence.*;
import lombok.Getter;
import pl.stock_market.infrastructure.application.dto.Portfolio;

import java.math.BigDecimal;

@Getter
@Entity
class Holder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Portfolio portfolio;
    @Embedded
    private final HolderAssets assets = new HolderAssets();

    public Holder() {}

    public void addQuantity(BigDecimal quantityToAdd) {
        assets.addQuantity(quantityToAdd);
    }

}
