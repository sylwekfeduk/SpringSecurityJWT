package pl.fis.lbd.springsecurityJWT.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import pl.fis.lbd.springsecurityJWT.jwt.JwtTokenVerifier;
import pl.fis.lbd.springsecurityJWT.jwt.JwtUsernameAndPasswordAuthenticationFilter;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/admin").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT,"/api/user").hasAnyAuthority("USER_EDIT", "ADMIN")
                .antMatchers(HttpMethod.GET,"/api/user").hasAnyAuthority("USER_READ", "ADMIN")
                .anyRequest().authenticated();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .password(passwordEncoder().encode("admin"))
                .username("admin")
                .authorities("ADMIN")
                .build();
        UserDetails user = User.builder()
                .password(passwordEncoder().encode("user"))
                .username("user")
                .authorities("USER_READ", "USER_EDIT")
                .build();
        UserDetails spectator = User.builder()
                .password(passwordEncoder().encode("spectator"))
                .username("spectator")
                .authorities("USER_READ")
                .build();
        return new InMemoryUserDetailsManager(admin, user, spectator);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
