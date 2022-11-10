package com.aminov.corporativelibrary.configs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    } 

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).withDefaultSchema()
            .usersByUsernameQuery(
                "select login, password, 'true' from \"user\" where login = ?" )
            .authoritiesByUsernameQuery(
                "select u.login, r.name from \"user\" as u, role as r where u.role_id = r.id and u.login = ?" );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .mvcMatchers("/admin").hasAuthority("manager") // , "/authors/new"
                // .mvcMatchers("/authors/**", "/books/**").hasAnyAuthority("reader", "manager")
                // .mvcMatchers("/authors/edit").hasAuthority("manager")   // не работает
                // .mvcMatchers("/admin").hasAuthority("manager")
                .mvcMatchers("/h2-console/**", "/", "/home/**", "/css/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();

    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/h2-console/**");
    }



    // UserServiceImpl userServiceImpl;

    // public SecurityConfig(UserServiceImpl userServiceImpl) {
    //     this.userServiceImpl = userServiceImpl;
    // }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     return http
    //         .authorizeHttpRequests((requests) -> requests
    //             .antMatchers("/authors/**").hasRole("manager")
    //             .antMatchers("/authors").hasAnyRole("manager", "reader")
    //             .antMatchers("/", "/home").permitAll() // , "/login", "/logout"
    //             .anyRequest().authenticated())
    //         .userDetailsService(userServiceImpl)
    //         .formLogin((form) -> form
    //             // .loginPage("/login")
    //             .permitAll())
    //         .logout((logout) -> logout.permitAll())
    //         .build();
    // }

    // @Bean
    // PasswordEncoder passwordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }
    
}
