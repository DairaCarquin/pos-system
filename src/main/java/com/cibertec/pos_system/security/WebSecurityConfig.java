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

    // Bean que devuelve nuestra clase que carga los datos del usuario (desde la BD)
    @Bean
    public UserDetailsService userDetailsService(){
        return new UsuarioDetalleServiceImpl();
    }

    // sirve para encriptar las contraseñas
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //se encarga de validar si el usuario existe y si la contraseña es correcta
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    //se encarga de validar si el usuario existe y si la contraseña es correcta
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
        httpSecurity.authorizeHttpRequests(
                auth -> auth

                        .requestMatchers("/users").hasAnyAuthority("USER","CREATOR","EDITOR","ADMIN")
                        .requestMatchers("/users/nueva").hasAnyAuthority("ADMIN","CREATOR")
                        .requestMatchers("/users/editar/*").hasAnyAuthority("ADMIN","EDITOR")
                        .requestMatchers("/users/eliminar/*").hasAnyAuthority("ADMIN")
                        .requestMatchers("/clientes").hasAnyAuthority("USER", "CREATOR", "EDITOR", "ADMIN")
                        .requestMatchers("/clientes/nuevo").hasAnyAuthority("ADMIN", "CREATOR")
                        .requestMatchers("/clientes/editar/*").hasAnyAuthority("ADMIN", "EDITOR")
                        .requestMatchers("/clientes/eliminar/*").hasAnyAuthority("ADMIN")
                        // Agregar rutas para clientesVista
                        .requestMatchers("/clientesVista/**").hasAnyAuthority("CLIENTES", "ADMIN")
                        .requestMatchers("/clientesVista/nuevo").hasAnyAuthority("ADMIN", "CREATOR")
                        .requestMatchers("/clientesVista/editar/*").hasAnyAuthority("ADMIN", "EDITOR")
                        .requestMatchers("/clientesVista/eliminar/*").hasAnyAuthority("ADMIN")
                        .requestMatchers("/user").hasAnyAuthority("USUARIOS", "ADMIN") // TODO: Determinar cual es 
                        .requestMatchers("/cliente").hasAnyAuthority("CLIENTES", "ADMIN") // TODO: Determinar cual es 
                        .requestMatchers("/rol").hasAnyAuthority("ROLES", "ADMIN")
                        .requestMatchers("/producto").hasAnyAuthority("PRODUCTOS", "ADMIN")
                        .requestMatchers("/categoria").hasAnyAuthority("CATEGORIAS", "ADMIN")
                        .requestMatchers("/local").hasAnyAuthority("LOCALES", "ADMIN")
                        .requestMatchers("/shopping").hasAnyAuthority("COMPRAS", "ADMIN")
                        .requestMatchers("/medioPago").hasAnyAuthority("MEDIOS_DE_PAGO", "ADMIN")
                        .requestMatchers("/proveedores").hasAnyAuthority("PROVEEDORES", "ADMIN")
                       
                        .requestMatchers("/caja").hasAnyAuthority("CAJA", "ADMIN")
                        .anyRequest().authenticated()) // lo demás necesita que el usuario esté logueado

                .formLogin(form -> form
                        .loginPage("/login")  // ruta donde está el formulario de login
                        .defaultSuccessUrl("/menu", true) // si entra bien, va al menú principal
                        .permitAll())
                .logout(l -> l.permitAll())  // todos pueden cerrar sesión
                .exceptionHandling(e -> e.accessDeniedPage("/403"));  // si no tiene permiso, va a /403
        return httpSecurity.build(); // devuelve toda la configuración de seguridad
    }
}
