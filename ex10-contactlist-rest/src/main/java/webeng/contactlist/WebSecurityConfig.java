package webeng.contactlist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import webeng.contactlist.model.UserRepository;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepo;

    public WebSecurityConfig(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/about").permitAll()
                    .antMatchers("/**.css").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/api/**").permitAll()
                    .antMatchers("/contacts").authenticated()
                    .regexMatchers("/contacts/[0-9]+").authenticated()
                    .anyRequest().hasRole("ADMIN")
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .csrf()
                    .ignoringAntMatchers("/h2-console/**", "/api/**").and()
                .headers()
                    .frameOptions().sameOrigin();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
