package webeng.contactlist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import webeng.contactlist.model.UserRepository;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserRepository userRepository;

    public WebSecurityConfig(UserRepository userRepository){

        this.userRepository = userRepository;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/*.css").permitAll()
            .antMatchers("/about").permitAll()
            .antMatchers("/contacts").authenticated()
            .regexMatchers("/contacts/[0-9]+").authenticated()
            .anyRequest().hasRole("ADMIN")
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout().permitAll().
            and().csrf().ignoringAntMatchers("/h2-console/**")
            .and().headers()
            .frameOptions().sameOrigin();

    }

    @Bean //bedeutet Objekt welches von Spring verwaltet wird
    @Override
    protected UserDetailsService userDetailsService() {
        return username -> {
            return userRepository.findAll().stream().filter(v -> v.getUsername().equals(username)).findFirst().orElseThrow(
                () -> {throw new UsernameNotFoundException(username);}
            );
        };
    }

}
