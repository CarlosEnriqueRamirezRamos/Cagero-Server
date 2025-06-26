package com.example.Cagero_Automatico.Configuration;

import com.example.Cagero_Automatico.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    // Configuración principal del filtro de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // 🔴 Desactiva CSRF para poder trabajar con el cliente
            .authorizeHttpRequests(auth -> auth
                // Permite el acceso libre al endpoint de login
                .requestMatchers("/auth/login").permitAll()
                .anyRequest().authenticated()
            )
            // Manejo de sesión:
            .sessionManagement(session -> session
                // Establece que no se usará sesiones en servidor
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // ✅ Habilita autenticación HTTP básica (usuario y contraseña enviados en cada request)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Bean necesario para usar AuthenticationManager (útil si tú haces la autenticación manualmente)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean para cifrar y verificar contraseñas usando BCrypt (muy recomendable)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 
