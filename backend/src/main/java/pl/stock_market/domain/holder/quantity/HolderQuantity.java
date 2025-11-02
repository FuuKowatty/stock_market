package pl.stock_market.domain.holder.quantity;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class HolderQuantity {

    private BigDecimal quantity;
    private boolean active;

    public HolderQuantity() {
        this.quantity = BigDecimal.ZERO;
        this.active = false;
    }

    public HolderQuantity(BigDecimal quantity, boolean active) {
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new HolderQuantityException("quantity cannot be less than 0");
        }
        this.quantity = quantity;
        this.active = active;
    }

    public void addQuantity(BigDecimal quantityToAdd) {
        BigDecimal newQuantity = this.quantity.add(quantity);
        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new HolderQuantityException("Negative quantity not allowed");
        }
        if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {
            active = false;
        }
        quantity = newQuantity;
    }

}
