package pl.stock_market.modules.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stock_market.modules.user.User;
import pl.stock_market.modules.user.UserRepository;
import pl.stock_market.modules.user.security.TokenService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @PostMapping("/signin")
    public void create(@RequestBody UserDto userDto) {
        User user = new User(userDto.login(), bCryptPasswordEncoder.encode(userDto.password()), userDto.firstName(), userDto.lastName(), userDto.email());
        userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto userDto) {
        User user = userRepository.findByEmail(userDto.email()).orElseThrow(() -> new UserNotFoundException(""));
        boolean matches = bCryptPasswordEncoder.matches(userDto.password(), user.getPassword());
        if (matches) {
            return tokenService.login(userDto.email(), userDto.password());
        } else {
            return null;
        }
    }

}
