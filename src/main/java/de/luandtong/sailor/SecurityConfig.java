package de.luandtong.sailor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/sign-in", "/log-in", "/css/**", "/photo/**").permitAll()  // 允许不需要认证的路径
                        .anyRequest().authenticated()  // 其他所有请求需要认证
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/log-in")  // 自定义登录页面
                        .defaultSuccessUrl("/select", true)  // 登录成功后的重定向
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll()  // 注销成功后的重定向
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

