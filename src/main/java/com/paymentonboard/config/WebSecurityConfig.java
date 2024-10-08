package com.paymentonboard.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.paymentonboard.service.CustomUserDetailsService;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String NON_SECURE_URLS = "/token,/forgetPassword,/get-Signup,/verified-OTP,/create-passwordSelf,/update-user-password,/encrypt,/decrypt,/getImage/*,/reset-password,"+
    "/kyc/validate,/bankAccount/validate,/txn-status/validate,/IFSC/validate,/health-check,/v2/api-docs,/swagger-ui/**,/configuration/ui,/swagger-resources/**,/configuration/security,/swagger-ui.html,/webjars/**";

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().authorizeRequests().antMatchers(NON_SECURE_URLS.split(",")).permitAll().anyRequest()
                .authenticated().and().exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntrypoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
        super.configure(auth);
    }

}
