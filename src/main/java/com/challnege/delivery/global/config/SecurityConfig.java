package com.challnege.delivery.global.config;

import com.challnege.delivery.domain.member.repository.MemberRepository;
import com.challnege.delivery.global.jwt.filter.CustomFormUsernamePasswordAuthenticationFilter;
import com.challnege.delivery.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.challnege.delivery.global.jwt.handler.LoginFailureHandler;
import com.challnege.delivery.global.jwt.handler.LoginSuccessHandler;
import com.challnege.delivery.global.jwt.jwt.JwtService;
import com.challnege.delivery.global.jwt.jwt.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(cs -> cs.disable())
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(f->f.disable())
                .httpBasic(h->h.disable());
        http
                .authorizeHttpRequests(auth->{
                    auth
                            //menu
                            .requestMatchers(HttpMethod.POST,"/restaurant/{restaurantsId}/menus").hasRole("OWNER")
                            .requestMatchers(HttpMethod.PATCH,"/restaurant/{restaurantsId}/menus/{menuId}").hasRole("OWNER")
                            .requestMatchers(HttpMethod.PATCH,"/restaurant/{restaurantsId}/menus/{menuId}/images").hasRole("OWNER")
                            .requestMatchers(HttpMethod.DELETE,"/restaurant/{restaurantsId}/menus/{menuId}").hasRole("OWNER")
                            //restaurants
                            .requestMatchers(HttpMethod.POST,"/restaurants").hasRole("OWNER")
                            .requestMatchers(HttpMethod.DELETE,"/restaurants/{id}/delete").hasRole("OWNER")
                            //order
                            .requestMatchers(HttpMethod.POST,"/restaurants/{restaurantId}/orders/{menuId}").hasRole("USER")
                            .requestMatchers(HttpMethod.PATCH,"/restaurants/{restaurantId}/orders/{orderId}").hasRole("USER")
                            .requestMatchers(HttpMethod.PATCH,"/restaurants/{restaurantId}/orders/owner/{orderId}").authenticated()
                            //review
                            .requestMatchers(HttpMethod.POST,"/restaurants/{restaurantId}/reviews").hasRole("USER")
                            .anyRequest().permitAll()

                    ;
                    http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
                    http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomFormUsernamePasswordAuthenticationFilter.class);
                });
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }


    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, memberRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }


    @Bean
    public CustomFormUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomFormUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomFormUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
        return jwtAuthenticationFilter;
    }
}

