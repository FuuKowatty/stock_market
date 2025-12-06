package pl.stock_market.modules.user.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public String login(String email, String password) {
        var now = Instant.now();
        var expiresIn = 300L;
        var claims = JwtClaimsSet.builder()
                .issuer("stock_market_app")
                .subject(email)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
