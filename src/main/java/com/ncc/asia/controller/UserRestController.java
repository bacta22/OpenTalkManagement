package com.ncc.asia.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncc.asia.dto.user.UserDTODisplay;
import com.ncc.asia.dto.user.UserDTOModify;
import com.ncc.asia.entity.Role;
import com.ncc.asia.entity.User;
import com.ncc.asia.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// Declare Spring beans with @Component annotation
@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService service;

    @Autowired
    public UserRestController(UserService service) {
        this.service = service;
    }

    @Value("${app.jwt.secret}")
    private String secretKey;

    // CRUD
    // Find all
    @GetMapping
    public ResponseEntity<List<UserDTODisplay>> findAll () {
        return ResponseEntity.ok().body(service.findAll());
    }

    // Find by id
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTODisplay> findById (@PathVariable("userId") int id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    // Create
    @PostMapping
    public ResponseEntity<UserDTODisplay> create (@RequestBody UserDTOModify modifyUserDTO) {
        modifyUserDTO.setId(0);
        UserDTODisplay userDTO = service.save(modifyUserDTO);
        return new ResponseEntity<UserDTODisplay>(userDTO,HttpStatus.CREATED);
    }

    // Update
    @PutMapping
    public ResponseEntity<UserDTODisplay> update (@RequestBody UserDTOModify modifyUserDTO) {
        UserDTODisplay userDTO = service.save(modifyUserDTO);
        return new ResponseEntity<UserDTODisplay>(userDTO,HttpStatus.OK);
    }

    // Delete by id
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById (@PathVariable("userId") int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Find employee based on some filter
    @GetMapping("/filter")
    public ResponseEntity<Page<UserDTODisplay>> filter (@RequestParam(required = false) Boolean enabled,
                                                        @RequestParam(required = false) String branch,
                                                        @RequestParam(required = false) String username,
                                                        @RequestParam(defaultValue = "0") Integer pageNo,
                                                        @RequestParam(defaultValue = "3") Integer pageSize) {
        Page<UserDTODisplay> userDTODisplayPage = service.findUserBySomeFilter(enabled,branch,username,pageNo,pageSize);
        return new ResponseEntity<>(userDTODisplayPage,OK);
    }

    // Find user joined open talk sort by ascending the number of joined open talk
    @GetMapping("/sortByOT")
    public ResponseEntity<Page<UserDTODisplay>> sortByNumberOfOpenTalk
                                                (@RequestParam(required = false) Boolean enabled,
                                                 @RequestParam(required = false) String branch,
                                                 @RequestParam(required = false) String username,
                                                 @RequestParam(defaultValue = "0") Integer pageNo,
                                                 @RequestParam(defaultValue = "3") Integer pageSize){
        Page<UserDTODisplay> userDTODisplayPage = service.findUserBySortByNoOfOpenTalk(enabled,branch,username,pageNo,pageSize);
        return ResponseEntity.ok().body(userDTODisplayPage);
    }




    // Register host open talk
    @PutMapping("/registerHost")
    public ResponseEntity<String> registerHost (@RequestParam(required = true) Integer idHost,
                                                @RequestParam(required = true) Integer idOpenTalk) {
        service.registerHost(idHost,idOpenTalk);
        return ResponseEntity.ok().body("Register host user id: " + idHost + " for Open Talk id: " + idOpenTalk);
    }

    // Register joined open talk
    @PutMapping("/registerJoined")
    public ResponseEntity<String> registerJoined (@RequestParam(required = true) Integer idHost,
                                                @RequestParam(required = true) Integer idOpenTalk) {
        service.registerJoinedOpenTalk(idHost,idOpenTalk);
        return ResponseEntity.ok().body("Register user id: " + idHost + " join Open Talk id: " + idOpenTalk);
    }

    // Find user have not hosted open talk in this year, or have not hosted any open talk
    @GetMapping("/haveNotHosted")
    public ResponseEntity<Page<UserDTODisplay>> findUserHaveNotHostedOpenTalk
                                (@RequestParam(defaultValue = "0") Integer pageNo,
                                 @RequestParam(defaultValue = "3") Integer pageSize) {
        Page<UserDTODisplay> userDTODisplayPage = service.findUserHaveNotHostedOpenTalk(pageNo,pageSize);
        return new ResponseEntity<>(userDTODisplayPage,OK);
    }

    // Find random user for host open talk
    @GetMapping("/random")
    public UserDTODisplay findRandomUserForHost () {
        return service.findRandomUserForHost();
    }

    // Get refresh token
    @GetMapping("/token/refresh")
    public void refreshToken (HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = service.findByName(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+ 10*60*1000)) // 10 minutes
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles",user.getRoles()
                                .stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            } catch (Exception exception) {
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
// ResponseEntity represents the whole HTTP response: status code, headers, and body. As a result,
// we can use it to fully configure the HTTP response.