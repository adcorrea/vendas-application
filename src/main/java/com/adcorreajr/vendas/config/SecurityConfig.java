package com.adcorreajr.vendas.config;

import com.adcorreajr.vendas.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                //.inMemoryAuthentication()
                .userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
               // .withUser("adcorrea")
               // .password(passwordEncoder().encode("1234"))
                //.roles("USER","ADMIN");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/clientes/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/produtos/**")
                .hasRole("ADMIN")
                .antMatchers("/api/pedidos/**")
                .hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/api/usuarios/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
