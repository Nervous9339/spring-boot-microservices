package ru.nothingtdh.photoapp.api.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(environment.getProperty("api.h2console.url.path"))
                .permitAll()
                .antMatchers(HttpMethod.POST, environment.getProperty("api.registration.url.path"))
                .permitAll()
                .antMatchers(HttpMethod.POST, environment.getProperty("api.login.url.path"))
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new AuthorizationFilter(authenticationManager(), environment))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
