package pl.stock_market.modules.holder;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
class HolderAssets {

    public static final String QUANTITY_CANNOT_BE_LESS_THAN_0 = "quantity cannot be less than 0";
    private BigDecimal quantity;
    private boolean activated;

    public HolderAssets() {
        this.quantity = BigDecimal.ZERO;
        this.activated = false;
    }

    public HolderAssets(BigDecimal quantity, boolean activated) {
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new HolderQuantityException(QUANTITY_CANNOT_BE_LESS_THAN_0);
        }
        this.quantity = quantity;
        this.activated = activated;
    }

    public void addQuantity(BigDecimal quantityToAdd) {
        BigDecimal newQuantity = this.quantity.add(quantity);
        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new HolderQuantityException(QUANTITY_CANNOT_BE_LESS_THAN_0);
        }
        if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {
            activated = false;
        }
        quantity = newQuantity;
    }

}
