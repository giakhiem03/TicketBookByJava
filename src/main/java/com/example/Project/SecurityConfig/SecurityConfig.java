package com.example.Project.SecurityConfig;

import com.example.Project.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.authorizeRequests(authorize -> authorize
                            .requestMatchers("/", "/home/**", "/login", "/logout","/register/**").permitAll()
                            .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                            .anyRequest().authenticated()
                    )
                    .formLogin(formLogin -> formLogin
                            .loginPage("/login")
                            .loginProcessingUrl("/login")
                            .defaultSuccessUrl("/home", true)
                            .failureUrl("/login?error")
                            .permitAll())
                    .logout(logout -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login") // Trang chuyển hướng sau khi đăng xuất.
                            .deleteCookies("JSESSIONID") // Xóa cookie.
                            .invalidateHttpSession(true) // Hủy phiên làm việc.
                            .clearAuthentication(true) // Xóa xác thực.
                            .permitAll())
                    .httpBasic(withDefaults())
                    .authenticationProvider(authenticationProvider());
            return http.build();
        }
        @Autowired
        private CustomUserDetailsService uds;
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(uds);
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
        }
}
