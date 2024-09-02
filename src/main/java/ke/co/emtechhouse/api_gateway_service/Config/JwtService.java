package ke.co.emtechhouse.api_gateway_service.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    private  static  final String SECRET_KEY = "tQgV9jkD1HMA3UcHvPrflkfXfMSOuGWWXOk8D/huYflaa0Lgy14V2aW6C4Eb2AtEkqNcW83lu1ierKmAhqdYoRiG4m3UOpxX33T2YrjYTg40LFkrBUC3pInKJk+PRIMfhQbTJ12Pnt2z3jmlnpRKFxse69YOXL5RiQFa8CWJlul9ISLGDYcKQUawWUfdjA71E/fX6/R+CithHy74+Fr2bCl2gbHrjTheIsET7sqfx7tSYfTf/s8mLJ8gXJLq1B+SdICDZXIF4gMlj5YOoueHvH7ULwaRc1ubwpAuv6CirE1EFdUSpo5/gaHWp5i9bYKKYHi4kvvssr9EzNnUQz5NtM821XV6VmO7Jwpj29+7WaA=";
    public String extractUsername(String token) {
        log.info("Extracting username");
        return  extractClaim(token,Claims::getSubject);
    }
    public <T> T extractClaim(String token , Function<Claims, T> claimsResolver){
        final  Claims claims = extractAllClaims(token);
        return  claimsResolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }
    public  String generateToken(Map<String, Object> extractClaims,UserDetails userDetails){
        log.info("Generating token: "+userDetails);
        return   Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public  boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        log.info("Extract Claims");
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        log.info("Getting SignInKey  ");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
