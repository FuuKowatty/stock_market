package pl.stock_market.domain.order;

class OrderQuantityException extends RuntimeException {
    public OrderQuantityException(String message) {
        super(message);
    }

}
