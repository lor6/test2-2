package com.baeldung.oauth2.authorization.server.handler;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;

import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;

@Named("refresh_token")
public class RefreshTokenGrantTypeHandler extends AbstractGrantTypeHandler {

    @Override
    public JsonObject createAccessToken(String clientId, MultivaluedMap<String, String> params) throws Exception {
        String refreshToken = params.getFirst("refresh_token");
        if (refreshToken == null || "".equals(refreshToken)) {
            throw new WebApplicationException("invalid_grant");
        }

        //Decode refresh token
        SignedJWT signedRefreshToken = SignedJWT.parse(refreshToken);
        JWSVerifier verifier = getJWSVerifier();

        if (!signedRefreshToken.verify(verifier)) {
            throw new WebApplicationException("Invalid refresh token.");
        }
        if (!(new Date().before(signedRefreshToken.getJWTClaimsSet().getExpirationTime()))) {
            throw new WebApplicationException("Refresh token expired.");
        }
        String refreshTokenClientId = signedRefreshToken.getJWTClaimsSet().getStringClaim("client_id");
        if (!clientId.equals(refreshTokenClientId)) {
            throw new WebApplicationException("Invalid client_id.");
        }

        //At this point, the refresh token is valid and not yet expired
        //So create a new access token from it.
        String subject = signedRefreshToken.getJWTClaimsSet().getSubject();
        String approvedScopes = signedRefreshToken.getJWTClaimsSet().getStringClaim("scope");

        String finalScope = approvedScopes;
        String requestedScopes = params.getFirst("scope");
        if (requestedScopes != null && !requestedScopes.isEmpty()) {
            Set<String> allowedScopes = new LinkedHashSet<>();
            Set<String> rScopes = new HashSet(Arrays.asList(requestedScopes.split(" ")));
            Set<String> aScopes = new HashSet(Arrays.asList(approvedScopes.split(" ")));
            for (String scope : rScopes) {
                if (aScopes.contains(scope)) allowedScopes.add(scope);
            }
            finalScope = String.join(" ", allowedScopes);
        }

        String accessToken = getAccessToken(clientId, subject, finalScope);
        return Json.createObjectBuilder()
                .add("token_type", "Bearer")
                .add("access_token", accessToken)
                .add("expires_in", expiresInMin * 60)
                .add("scope", finalScope)
                .add("refresh_token", refreshToken)
                .build();
    }
}
