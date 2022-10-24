package com.ncc.asia.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private String secretKey;
    public CustomAuthorizationFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // If the user access URI login or URI get refresh token => continue authentication filter
        if (request.getServletPath().equals("/api/login") ||
            request.getServletPath().equals("/api/token/refresh")) {
            filterChain.doFilter(request,response);
        } else {
            // Get the token from Authorization header
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            // Check the valid token, then set information of user in SecurityContextHolder
            // for check user's permission access resources
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {

                    // remove 'Bearer' => get the token
                    String token = authorizationHeader.substring("Bearer ".length());
                    // create algorithm base on Secret key-before used create the token
                    Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
                    // create verifier based on algorithm
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    // decode the token
                    DecodedJWT decodedJWT = verifier.verify(token);
                    // get the user's information
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username,null,authorities);
                    // put user's information in SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    // continue filter chain
                    filterChain.doFilter(request,response);

                    // handle any exception : token was not valid, we are not available verify it,
                    // or it is expired
                } catch (Exception exception) {
                    log.error("Error logging in: {}",exception.getMessage());
                    response.setHeader("error",exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String,String> error = new HashMap<>();
                    error.put("error_message",exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }

            // Whenever do not have token => 403 code
            } else {
                filterChain.doFilter(request,response);
            }
        }

    }
}

/* OncePerRequestFilter
Let's first understand how filters work. A Filter can be called either before or after servlet
execution. When a request is dispatched to a servlet, the RequestDispatcher may forward it to
another servlet. There's a possibility that the other servlet also has the same filter.
In such scenarios, the same filter gets invoked multiple times.

But, we might want to ensure that a specific filter is invoked only once per request. A common use
case is when working with Spring Security. When a request goes through the filter chain,
we might want some of the authentication actions to happen only once for the request.
We can extend the OncePerRequestFilter in such situations. Spring guarantees that the
OncePerRequestFilter is executed only once for a given request.
 */

/*
Provides a doFilterInternal() method that we will implement parsing &
validating JWT, loading User details (using UserDetailsService),
checking Authorizaion (using UsernamePasswordAuthenticationToken).
 */


