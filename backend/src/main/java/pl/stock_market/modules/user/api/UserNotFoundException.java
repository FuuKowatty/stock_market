package pl.stock_market.modules.user.api;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
