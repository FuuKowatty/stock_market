package pl.stock_market.domain.order;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;

@Embeddable
@Getter
class OrderAssets {
    public static final String QUANTITY_CANNOT_BE_LESS_THAN_0 = "quantity cannot be less than 0";
    @Getter
    private BigDecimal quantity;
    private boolean activated;

    public OrderAssets() {
        this.quantity = BigDecimal.ZERO;
        this.activated = false;
    }

    public OrderAssets(BigDecimal quantity, boolean activated) {
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderQuantityException(QUANTITY_CANNOT_BE_LESS_THAN_0);
        }
        this.quantity = quantity;
        this.activated = activated;
    }

    public void addQuantity(BigDecimal quantityToAdd) {
        BigDecimal newQuantity = this.quantity.add(quantityToAdd);
        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderQuantityException(QUANTITY_CANNOT_BE_LESS_THAN_0);
        }
        if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {
            activated = false;
        }
        quantity = newQuantity;
    }
}
