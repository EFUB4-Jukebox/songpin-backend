package sws.songpin.global.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key accessKey;
    private final Key refreshKey;
    private final RedisService redisService;
    private final CustomUserDetailsService userDetailsService;
    private static final Duration ACCESS_TOKEN_EXPIRE_TIME = Duration.ofMinutes(30); //30분
    private static final Duration REFRESH_TOKEN_EXPIRE_TIME = Duration.ofDays(7); //7일

    public JwtUtil(@Value("${jwt.secret.access}") String accessSecret, @Value("${jwt.secret.refresh}") String refreshSecret, CustomUserDetailsService userDetailsService, RedisService redisService){
        byte[] keyBytes = Decoders.BASE64.decode(accessSecret);
        this.accessKey = Keys.hmacShaKeyFor(keyBytes);
        keyBytes = Decoders.BASE64.decode(refreshSecret);
        this.refreshKey = Keys.hmacShaKeyFor(keyBytes);
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
    }

    public String generateAccessToken(Authentication authentication){

        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());

        Date now = new Date();
        Date expireDate = new Date(now.getTime()+ACCESS_TOKEN_EXPIRE_TIME.toMillis());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();

    }

    public String generateRefreshToken(Authentication authentication){

        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());

        Date now = new Date();
        Date expireDate = new Date(now.getTime()+REFRESH_TOKEN_EXPIRE_TIME.toMillis());

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(refreshKey,SignatureAlgorithm.HS256)
                .compact();

        //refresh token을 redis에 저장
        redisService.setValuesWithTimeout(authentication.getName(), refreshToken, REFRESH_TOKEN_EXPIRE_TIME);

        return refreshToken;
    }


    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    public Authentication getAuthentication(String token){
        String userPrincipal = Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build().parseClaimsJws(token)
                .getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);

        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

}
