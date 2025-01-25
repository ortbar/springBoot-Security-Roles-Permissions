package com.app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.AlgorithmConstraints;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    //NECESITIAREMOS UN USERgenerator que va a ser un usuario generador del token y tb, por otro lado una clave privada

    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public String createToken(Authentication authentication) {

        // codificar el private con el algoritmo de encriptacion
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        // extraer usuario autenticado. el objeto Authentication se compone del getPrincipal (que guarda el usuario) y el autorites (los permisos de dicho usuario)

        String username = authentication.getPrincipal().toString();
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); // coge cada uno de los permisos y los separa por comas

        // CREAR TOKEN - usamos la libreria java-jwt, Ver doc de la libreria


        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username) // a quien se le va a generar el token
                .withClaim("authorities", authorities) // que claim va atener el token
                .withIssuedAt(new Date()) // fecha en que se genera el token
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) // en qué momento expira el token? 30 min convertido a milisegundos sumados al momento actual
                .withJWTId(UUID.randomUUID().toString()) // asignar un id al token con valor random
                .withNotBefore(new Date(System.currentTimeMillis())) // a partir de qué momento se considera válido el token. A partir del momento actual
                .sign(algorithm); // firmamos con el privateKey encriptada con el algoritmo.

        return jwtToken;
    }

    /* VALIDAR TOKEN - ver doc de la libreria */

    public DecodedJWT validateToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;

        } catch (JWTCreationException exception) {
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    /* EXTRAER EL USUARIO QUE VIENE DENTRO DEL TOKEN */

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject().toString();
    }


    /*  EXTRAER UN CLAIM ESPECIFICO  */

    public Claim getSpecificClaim (DecodedJWT decodedJWT, String claimName) {

        return decodedJWT.getClaim(claimName);

    }

    /*  EXTRAER TODOS LOS CLAIMS  */

    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {

        return decodedJWT.getClaims();
    }



}
