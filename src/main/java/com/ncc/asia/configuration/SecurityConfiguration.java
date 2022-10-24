package com.ncc.asia.configuration;

import com.ncc.asia.filter.CustomAuthenticationFilter;
import com.ncc.asia.filter.CustomAuthorizationFilter;
import com.ncc.asia.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Configuration // Spring @Configuration annotation indicates that the class has @Bean definition methods
@EnableWebSecurity
//@EnableScheduling
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // Declare Spring beans with @Bean annotation
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService () {
        return new UserDetailsServiceImpl();
    }

    @Value("${app.jwt.secret}")
    private String secretKey;

    // Used to create an AuthenticationManager
    // attempt to obtain an AuthenticationManager. If overridden,
    // the AuthenticationManagerBuilder should be used to specify the AuthenticationManager.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManager(),secretKey);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh").permitAll();
        http.authorizeRequests().antMatchers("/api/users", "/api/users/**")
                .hasAuthority("ROLE_ADMIN"); // user resources

        http.authorizeRequests().antMatchers("/api/opentalks/user/**")
                .hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_ADMIN"); // open talk for employee resources

        http.authorizeRequests().antMatchers("/api/opentalks/finished")
                .hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_ADMIN");

        http.authorizeRequests().antMatchers("/api/opentalks", "/api/opentalks/**")
                .hasAnyAuthority("ROLE_ADMIN"); // open talk resources (modify) for admin



        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(secretKey), UsernamePasswordAuthenticationFilter.class);
    }

    // Get an AuthenticationManager for CustomAuthenticationFilter.
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();

    }

}

/*
Allows adding a Filter before one of the known Filter classes. The known Filter instances
are either a Filter listed in addFilter(Filter) or a Filter that has already been added
using addFilterAfter(Filter, Class) or addFilterBefore(Filter, Class).
Params:
filter – the Filter to register before the type beforeFilter – the Class of the known Filter.
Returns:
the HttpSecurity for further customizations
 */

/*
  * csrf disabled?
No cookies = No CSRF
It really is that simple. Browsers send cookies along with all requests.
CSRF attacks depend upon this behavior. If you do not use cookies, and don't rely
on cookies for authentication, then there is absolutely no room for CSRF attacks,
and no reason to put in CSRF protection. If you have cookies, especially if you use
them for authentication, then you need CSRF protection. If all you want to know is
"Do I need CSRF protection for my API endpoint?" you can stop right here and leave
with your answer. Otherwise, the devil is in the details.
 */

/*
REST = Stateless
One property of REST that I have always relied upon is that it is stateless.
The application itself has state of course. If you can't store data in a database
somewhere, your application is going to be pretty limited. In this case though,
stateless has a very specific and important meaning: REST applications don't track
state for the client-side application. If you are using sessions, then you are
(almost certainly) keeping track of client-side state, and you are not a REST-full
application. So an application that uses sessions (especially for logins) that are
tracked via cookies is not a REST-full application (IMO), and is certainly vulnerable
to CSRF attacks, even if it otherwise looks like a REST application.
 */
