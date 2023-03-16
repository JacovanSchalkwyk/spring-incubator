package entelect.training.incubator.spring.flight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Disclaimer! In a production system you will never store your credentials in either clear text or in the code.
     * It is done here so that development is both easy to understand and change.
     * The commented code below shows you how to connect to a DB. You will also want to use some kind of password encoding/hashing.
     */

    //    @Autowired
    //    private DataSource securityDataSource;
    //
    //    @Override
    //    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //        auth.jdbcAuthentication().dataSource(securityDataSource);
    //    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("{noop}the_cake").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("{noop}is_a_lie").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // !!! Disclaimer: NEVER DISABLE CSRF IN PRODUCTION !!!
                .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/flights/specials").hasAnyRole("SYSTEM", "ADMIN",
                        "LOYALTY_USER")
                .antMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                .antMatchers(HttpMethod.GET, "/flights/**").permitAll()
                .antMatchers(HttpMethod.GET, "/notification/**").hasAnyRole("SYSTEM", "ADMIN")
                .antMatchers(HttpMethod.POST, "/flights").hasAnyRole("SYSTEM", "ADMIN")
//                .anyRequest().denyAll()
                .and()
                .httpBasic();
    }
}
