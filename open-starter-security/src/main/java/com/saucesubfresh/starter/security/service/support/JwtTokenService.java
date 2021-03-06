package com.saucesubfresh.starter.security.service.support;

import com.saucesubfresh.starter.security.enums.SecurityExceptionEnum;
import com.saucesubfresh.starter.security.exception.SecurityException;
import com.saucesubfresh.starter.security.domain.Authentication;
import com.saucesubfresh.starter.security.domain.UserDetails;
import com.saucesubfresh.starter.security.properties.SecurityProperties;
import com.saucesubfresh.starter.security.service.TokenService;
import com.saucesubfresh.starter.security.utils.JSON;
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
        String subject;
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(securityProperties.getSecretKeyBytes()).build().parseClaimsJws(accessToken).getBody();
            subject = claims.getSubject();
        }catch (Exception e){
            throw new SecurityException(SecurityExceptionEnum.UNAUTHORIZED);
        }
        UserDetails userDetails = JSON.parse(subject, UserDetails.class);
        Authentication authentication = new Authentication();
        authentication.setUserDetails(userDetails);
        return authentication;
    }
}
