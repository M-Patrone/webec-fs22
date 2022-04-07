package webeng.contactlist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/*.css").permitAll()
            .antMatchers("/about").permitAll()
            .antMatchers("/contacts").authenticated()
            .regexMatchers("/contacts/[0-9]+").authenticated()
            .anyRequest().hasRole("ADMIN")
            .and()
            .formLogin();
    }

    @Bean //bedeutet Objekt welches von Spring verwaltet wird
    @Override
    protected UserDetailsService userDetailsService() {
        var user = User.withDefaultPasswordEncoder()
            .username("user").password("user")
            .roles()
            .build();
        var admin = User.withDefaultPasswordEncoder()
            .username("admin").password("admin")
            .roles("ADMIN").build();
    return new InMemoryUserDetailsManager(user,admin);
    }
}
