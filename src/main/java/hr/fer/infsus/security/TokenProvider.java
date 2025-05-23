package hr.fer.infsus.security;

import hr.fer.infsus.exception.InvalidTokenException;
import hr.fer.infsus.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private static final String SECRET_KEY = "musicShopSecretKeyINekajJakoTeskoZaPogodoitiJerJeJakoKompliciranoISIgurnoIDUgo";
    public static final long JWT_TOKEN_VALIDITY_DURATION = 24L * 60 * 60 * 1000;

    private final UserDetailsService userDetailsService;

    public String generateToken(final String username,final Role role) {
        final Claims claims = Jwts.claims().subject(username).add("auth", new SimpleGrantedAuthority(role.getAuthority())).build();

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + JWT_TOKEN_VALIDITY_DURATION);
        final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(final String token) throws InvalidTokenException {
        try {
            final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Expired or invalid JWT token!");
        }
    }

    public Authentication getAuthentication(final String token) {
        final String username = getUsername(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(final String token) {
        final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
}