package pl.stock_market.modules.user.api;

import org.springframework.security.authentication.BadCredentialsException;

import java.util.regex.Pattern;

import static org.apache.logging.log4j.util.Strings.isBlank;

public record UserDto(String login, String password, String firstName, String lastName, String email) {
    private static final Pattern EMAIL_RX = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    static void validateCreate(UserDto dto) {
        if (dto == null || isBlank(dto.login) || isBlank(dto.firstName) || isBlank(dto.lastName))
            throw new BadCredentialsException("Missing required fields");
        if (isBlank(dto.email) || !EMAIL_RX.matcher(dto.email).matches())
            throw new BadCredentialsException("Invalid email format");
        PasswordPolicy.verify(dto.password);
    }
}
