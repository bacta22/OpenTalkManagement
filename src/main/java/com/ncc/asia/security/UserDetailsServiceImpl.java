package com.ncc.asia.security;

import com.ncc.asia.entity.Role;
import com.ncc.asia.entity.User;
import com.ncc.asia.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    // loads user-specific data based on username => return UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsernameIs(username);
        if (user == null) {
            //log.error("User not found in database");
            throw  new UsernameNotFoundException("User not found in database");
        }
        // log.info("User found in database: {}",username);

        return new UserDetailsImpl(user);
    }
}
