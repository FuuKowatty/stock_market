package pl.stock_market.modules.holder;

class HolderQuantityException extends RuntimeException {
    HolderQuantityException(String message) {
        super(message);
    }

    HolderQuantityException() {
    }
}
