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
                        .requestMatchers("/", "/sign-in", "/log-in", "/css/**", "/photo/**", "/log-out", "/about").permitAll()
                        .anyRequest().authenticated()  // 其他所有请求需要认证
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/log-in")  // 自定义登录页面
                        .defaultSuccessUrl("/select", true)  // 登录成功后的重定向
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/log-out") // 指定触发注销操作的URL，默认是"/logout"
                        .logoutSuccessUrl("/log-in") // 注销成功后重定向到的URL
                        .deleteCookies("JSESSIONID") // 注销时删除特定的cookies，例如删除会话ID
                        .invalidateHttpSession(true) // 注销时使会话无效，默认为true
                        .clearAuthentication(true) // 清除认证信息，默认为true
                        .permitAll() // 允许所有用户访问注销URL
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

