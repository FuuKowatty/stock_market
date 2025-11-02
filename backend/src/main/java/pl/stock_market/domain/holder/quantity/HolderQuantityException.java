package pl.stock_market.domain.holder.quantity;

public class HolderQuantityException extends RuntimeException {
    public HolderQuantityException(String message) {
        super(message);
    }

    public HolderQuantityException() {
    }
}
