package com.cibertec.pos_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cibertec.pos_system.service.impl.UsuarioDetalleServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        return new UsuarioDetalleServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //DaoAuthenticationProvider es una implementacion del AuthenticationProvider
    //Se utiliza comunmente para autenticar usuarios en bases de datos
    //Es responsable de verificar las credenciales del usuario y autenticar el usuario
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        //cuando se realiza una solicitud el administrador de autenticación usará el provider
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    //Define las reglas de autorizacion para las solicitudes HTTP
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        .csrf( csrf -> csrf.disable())
        .authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/", "/web/index", "/css/**", "/js/**","/img/**", "/images/**","**").permitAll() //  PERMITIR ACCESO
                        .requestMatchers("/users").hasAnyAuthority("USER","CREATOR","EDITOR","ADMIN")
                        .requestMatchers("/users/nueva").hasAnyAuthority("ADMIN","CREATOR")
                        .requestMatchers("/users/editar/*").hasAnyAuthority("ADMIN","EDITOR")
                        .requestMatchers("/users/eliminar/*").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/menu", true)
                        .permitAll())
                .logout(l -> l.permitAll())
                .exceptionHandling(e -> e.accessDeniedPage("/403"));
        return httpSecurity.build();
    }
}
