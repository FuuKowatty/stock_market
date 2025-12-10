package pl.stock_market.modules.user.api;

import org.springframework.security.authentication.BadCredentialsException;

class PasswordPolicy {
    static void verify(String password) {
        if (password == null || password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(".*\\d.*"))
            throw new BadCredentialsException("Password too weak");
    }
}
