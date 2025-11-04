package pl.stock_market.modules.order.domain.exception;

public class OrderQuantityException extends RuntimeException {
    public OrderQuantityException(String message) {
        super(message);
    }

}
