package com.ncc.asia.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncc.asia.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// UsernamePasswordAuthenticationFilter: Processes an authentication form submission.
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

//    @Value("${app.jwt.secret}")
    private String secretKey;

    // call AuthenticationManager to authenticate the user, inject it in constructor
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter (AuthenticationManager authenticationManager, String secretKey) {
        this.authenticationManager = authenticationManager;
        this.secretKey=secretKey;
    }

    // The user try to log in, this method will be called for authentication
    // Attempts to authenticate the passed Authentication object,
    // returning a fully populated Authentication object
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // Retrieve username and password
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //log.info("Username is : {} ", username);
        //log.info("Password is : {} ", password);

        // UsernamePasswordAuthenticationToken implementation Authentication
        // Simple represent username and password
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    // Override method in AbstractAuthenticationProcessingFilter:
    // + Abstract processor of browser-based HTTP-based authentication requests.
    // Default behaviour for successful authentication.
    // + Sets the successful Authentication object on the SecurityContextHolder
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication)
                                            throws IOException, ServletException {

        // Get user log in successfully for create token
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        // create access_token and refresh_token
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ 120*60*1000)) // 120 minutes
                .withIssuer(request.getRequestURI().toString())
                .withClaim("roles",user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ 240*60*1000)) // 240 minutes
                .withIssuer(request.getRequestURI().toString())
                .withClaim("roles",user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        //  typically in the Authorization header using the Bearer schema ??
//        response.setHeader("access_token",access_token);
//        response.setHeader("refresh_token",refresh_token);
        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token",access_token);
        tokens.put("refresh_token",refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setHeader("error",failed.getMessage());
        response.setStatus(UNAUTHORIZED.value());
        Map<String,String> error = new HashMap<>();
        error.put("error_message",failed.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }


    //    public String getSecretKey() {
//        return secretKey;
//    }
//
//    public void setSecretKey(String secretKey) {
//        this.secretKey = secretKey;
//    }
}

// - Header: Type of the token(JWT) - Signing algorithm
// - Payload: Contains the claims (additional data)
// + Registered claims (not mandatory but recommended) : iss, exp, sub, aud, ...
// + Public claims
// + Private claims
// - Signature : encoded header + encoded payload + a secret + the algorithm
//              specified in header and sign that
// - Note:
// + If the JWT contains the necessary data, the need to query the database
//   for certain operations may be reduced, though this may not always be the case.
// + if you send JWT tokens through HTTP headers, you should try to prevent them from
// getting too big. Some servers don't accept more than 8 KB in headers. If you are trying
// to embed too much information in a JWT token, like by including all the user's permissions,
// you may need an alternative solution, like Auth0 Fine-Grained Authorization.
// + Do note that with signed tokens, all the information contained within the token is exposed
// to users or other parties, even though they are unable to change it. This means you should
// not put secret information within the token.

/* SecretKey in JWT token:
So anyone will be able to decode them and to read them, we cannot store any sensitive
data in here. But that's not a problem at all because in the third part, so in the signature,
is where things really get interesting. The signature is created using the header,
the payload, and the secret that is saved on the server.
 */

// ObjectMapper provides functionality for reading and writing JSON, either to and from basic POJOs

