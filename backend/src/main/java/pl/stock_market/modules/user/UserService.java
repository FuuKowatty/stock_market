package pl.stock_market.modules.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.stock_market.modules.user.api.UserDto;
import pl.stock_market.modules.user.api.UserNotFoundException;
import pl.stock_market.modules.user.security.TokenService;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    public void create(UserDto userDto) {
        User user = new User(userDto.login(), bCryptPasswordEncoder.encode(userDto.password()), userDto.firstName(), userDto.lastName(), userDto.email());
        userRepository.save(user);
    }

    public String login(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.email()).orElseThrow(UserNotFoundException::new);
        boolean matches = bCryptPasswordEncoder.matches(userDto.password(), user.getPassword());
        if (matches) {
            return tokenService.login(userDto.email(), userDto.password());
        } else {
            return null;
        }
    }
}
