package com.lightcode.starter.security.service.support;

import com.lightcode.starter.security.domain.Authentication;
import com.lightcode.starter.security.domain.UserDetails;
import com.lightcode.starter.security.properties.SecurityProperties;
import com.lightcode.starter.security.service.TokenService;
import com.lightcode.starter.security.utils.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author lijunping on 2022/3/31
 */
public class JwtTokenService implements TokenService {

    private final SecurityProperties securityProperties;

    public JwtTokenService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public Authentication readAuthentication(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(securityProperties.getSecretKeyBytes()).build().parseClaimsJws(accessToken).getBody();
            final String subject = claims.getSubject();
            UserDetails userDetails = JSON.parse(subject, UserDetails.class);
            Authentication authentication = new Authentication();
            authentication.setUserDetails(userDetails);
            return authentication;
        }catch (Exception e){
            throw new SecurityException(e.getMessage());
        }
    }
}