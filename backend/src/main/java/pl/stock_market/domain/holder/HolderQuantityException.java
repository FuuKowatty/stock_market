package pl.stock_market.domain.holder;

class HolderQuantityException extends RuntimeException {
    HolderQuantityException(String message) {
        super(message);
    }

    HolderQuantityException() {
    }
}
