package com.spring.jwtpractice.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public abstract class JwtUtil {
    private static final int expireHourToken = 24;
    private static final int expireHourRefreshToken = 72;
    private static final String secret = "lalalalalalalallalalalslslslslslslalslslslalslslslslslalslsllssl";

    public static String createAccessToken(String username, String issuer, List<String> roles) {
        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer(issuer)
                    .claim("roles", roles)
                    .expirationTime(Date.from(Instant.now().plusSeconds(expireHourToken)))
                    .issueTime(new Date())
                    .build();
            Payload payload = new Payload(claims.toJSONObject());
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS512), payload);
            jwsObject.sign(new MACSigner(secret));
            return jwsObject.serialize();
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

    }

    public static String createRefreshToken(String username) {
        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .expirationTime(Date.from(Instant.now().plusSeconds(expireHourToken)))
                    .issueTime(new Date())
                    .build();
            Payload payload = new Payload(claims.toJSONObject());
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS512), payload);
            jwsObject.sign(new MACSigner(secret));
            return jwsObject.serialize();
        } catch (KeyLengthException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public static UsernamePasswordAuthenticationToken parseToken(String token) throws ParseException, JOSEException, BadJOSEException {
        byte[] secretKey = secret.getBytes();
        SignedJWT signedJWT = SignedJWT.parse(token);
        signedJWT.verify(new MACVerifier(secretKey));
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<SecurityContext>(JWSAlgorithm.HS512, new ImmutableSecret<>(secretKey));
        jwtProcessor.setJWSKeySelector(keySelector);
        jwtProcessor.process(signedJWT, null);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        String username = claims.getSubject();
        List<String> roles = (List<String>) claims.getClaim("roles");
        List<SimpleGrantedAuthority> authorities = roles == null ? null : roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

}
