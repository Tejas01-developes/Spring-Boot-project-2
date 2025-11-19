package employees.data.project2.refreshtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class refreshtoken {
    @Value("${secret.key}")
    private String key;

//private final String key="tejas123";
    public String accesstoken(String username,String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .signWith(SignatureAlgorithm.HS256,key.getBytes())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ 15 * 60 * 1000))
                .compact();

    }

    public String refreshtoken(String username,String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ 7 * 24 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256,key.getBytes())
                .compact();
    }

    public boolean verifytoken(String token){
         Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return true;
    }

    public String extractusername(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

public String extractrole(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role",String.class);

}





}
