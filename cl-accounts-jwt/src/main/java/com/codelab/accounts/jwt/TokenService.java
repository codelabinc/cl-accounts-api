package com.codelab.accounts.jwt;

import com.codelab.accounts.domain.enumeration.TokenClaimsConstant;
import com.codelab.accounts.domain.response.TokenResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import javax.inject.Singleton;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Named
@Singleton
public final class TokenService {

    private final JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

    @Value("${TOKEN_SECRET:}")
    private String tokenSecret;

    public JWTClaimsSet decodeToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(tokenSecret);
        signedJWT.verify(verifier);
        return signedJWT.getJWTClaimsSet();
    }

    public Object getClaim(JWTClaimsSet claimsSet, String name) {
        return claimsSet.getClaim(name);
    }

    public String createToken(TokenResponse tokenResponse) throws JOSEException {
        JWSSigner signer = new MACSigner(tokenSecret);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(tokenResponse.getUser().getId()))
                .issuer("https://codelab.com")
                .claim(TokenClaimsConstant.ACCOUNT_CODE.getValue(), tokenResponse.getAccount().getCode())
                .claim(TokenClaimsConstant.ROLES.getValue(), tokenResponse.getRoles())
                .claim(TokenClaimsConstant.PERMISSIONS.getValue(), tokenResponse.getPermissions())
                .claim(TokenClaimsConstant.USER_FULL_NAME.getValue(), tokenResponse.getUser().getLastName() + " " + tokenResponse.getUser().getFirstName())
                .claim(TokenClaimsConstant.ACCOUNT_NAME.getValue(), tokenResponse.getAccount().getName())
                .expirationTime(Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

}
