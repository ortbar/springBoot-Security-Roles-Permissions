package com.app.config.filter;

import com.app.util.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

            /* FILTRO QUE VALIDA EL TOKEN */


public class JwtTokenValidator extends OncePerRequestFilter {



    // inyectamos JwtUtils por constructor ya que no lo podemos definir com un Bean
    private JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (jwtToken != null) { // Bearer audoaudauofçfdça4f5a

                jwtToken = jwtToken.substring(7); // extrae token
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken); // decodfica token
                String username = jwtUtils.extractUsername(decodedJWT); // extrae nombre del usuario
                String stringAuthorities = jwtUtils.getSpecificClaim(decodedJWT,"authorities").asString(); // reucpero los permisos del usuario

                Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username,null, authorities);
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);



            }

            filterChain.doFilter(request, response);

    }
}
