package zazadev.tasksystem.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "30820154020100300d06092a864886f70d01010105000482013e3082013a020100024100b46da400b871cd5741fb33ded12bd7921cf6a6342307b338c12353976fc554cdcccc0e1192d04754550584438341fb8b989eeb5c3845941e87671895219e01870203010001023f1524c413518a048635353adee1a4419eddc609b41266d499d20851c36d55b29a39044fd1ef17a7d063f54ccdb7a2ebc6a27dbf1e89c44f12fea010f84f9221022100db06f0e7fca247ce0650b2aa6cc6a86bb609d426e0df0eee714a8de006887c49022100d2e2af9ef4cb19ec50a664b28a319c67f15ea310c74e39be90248e295abc6f4f0220193e0e8b9df911ef7398df4e76e4da4a379ba060aeafe667a294e60ef891c84902210085f6bad99f67e5efd8cbdaec101db3ddd3d6ea7f9e1c73523afefdd00773ecc1022100aede578cff20805913f154697056f98eca5d23679057378a6e88462ad266f72b";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
