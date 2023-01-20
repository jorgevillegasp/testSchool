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


    /**
     *  getUsernameFromToken():
     *   Recupera el nombre de usuario del token jwt
     *   toma un token y una función de Claims como argumentos,
     *   y devuelve el valor del reclamo especificado por la función de Claims.
     * @param token: cadena de texto
     * @return
     * @throws Exception
     */
    public String getUsernameFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Recuperar la fecha de caducidad del token jwt
    private Date getExpirationDateFromToken(String token) throws Exception {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * getClaimFromToken()
     *  La función comienza llamando a "getAllClaimsFromToken" para obtener un objeto "Claims"
     *  que contiene todos los reclamos almacenados en el token. Luego, utiliza la función
     *  "claimsResolver.apply" para aplicar la función de Claims especificada como argumento
     *  y obtener el valor del reclamo deseado. Finalmente, la función devuelve ese valor.
     *
     * @param token: token del cual se desea obtener el reclamo.
     * @param claimsResolver: función de interfaz funcional que especifica cómo obtener el reclamo deseado de un objeto "Claims"
     * @return
     * @param <T>
     * @throws Exception
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Para recuperar cualquier información del token, necesitaremos la clave secreta

    /**
     * getAllClaimsFromToken() // obtener todas las reclamaciones de token
     * Para recuperar cualquier información del token, necesitaremos la clave secreta.
     * utiliza la librería Jwts para parsear un token JWT (Json Web Token)
     * y obtener los "claims" (afirmaciones) contenidos en él.
     * La clave secreta es utilizada para verificar la autenticidad del token.
     * La función devuelve un objeto "Claims" que contiene los claims del token.
     * Si el token no es válido o no puede ser parseado, se lanza una excepción "Exception".
     * @param token
     * @return
     * @throws Exception
     */
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
