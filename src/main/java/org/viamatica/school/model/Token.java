package org.viamatica.school.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.viamatica.school.utility.Conf;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@ApplicationScoped
public class Token {

    @ConfigProperty(name = "session.timeExp")
    int valueTimeExp;

    private String secret = Conf.secretKeyToken;

    // Recuperar el nombre de usuario del token jwt
    public String getUsernameFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Recuperar la fecha de caducidad del token jwt
    private Date getExpirationDateFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Para recuperar cualquier información del token, necesitaremos la clave secreta
    private Claims getAllClaimsFromToken(String token) throws Exception {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // while creating the token -
    // 1. Definir reclamos del token, como Emisor, Vencimiento, Asunto y la ID
    // 2. Firme el JWT utilizando el algoritmo HS512 y la clave secreta.
    // 3. De acuerdo con JWS Compact
    // Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    // Compactación de la JWT a una cadena segura para URL
    private String doGenerateToken(Map<String, Object> claims, String usuario) {
        return Jwts.builder().setClaims(claims).setSubject(usuario).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + valueTimeExp*1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // Validar token
    public Boolean validateToken(String token, String usuario) throws Exception {
        final String username = getUsernameFromToken(token);
        return (username.equals(usuario));
    }

    // Comprobar si el token ha caducado
    public Boolean isTokenExpired(String token) {

        Boolean output = true;
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception eex) {

        }

       return output;
    }

    // Generar token para la usuario
    public String generateToken(String usuario) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, usuario);
    }


}
