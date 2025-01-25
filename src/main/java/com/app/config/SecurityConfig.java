package com.app.config;

import com.app.config.filter.JwtTokenValidator;
import com.app.service.UserDetailServiceImpl;
import com.app.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    // definimos los componentes de springSecurity

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf-> csrf.disable()) // deshabilitada, xq no vamos a trabjar con form
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // sin estado, xq no vamos a guardar la sesion en memoria, si no que el tiempo de duracion de la session va a depender del token, de cuando expire
                .authorizeHttpRequests(http->{
                    // configurar los endpoints publicos
                    http.requestMatchers(HttpMethod.GET, "/auth/get").permitAll(); // si hace match con esa url, qué va a hacer??. permite a todos al endpoint /get. endpoint publico

                    // configurar los endpoints privados. Se puede validar por el permiso o tambien por el rol hasRole(""), hasAuthority(""), hasAnyAuthority("")....

                    http.requestMatchers(HttpMethod.POST, "/auth/post").hasAnyRole("ADMIN", "DEVELOPER");// si hace match a /post y tiene el rol ADMIN puede pasar
                    http.requestMatchers(HttpMethod.PATCH, "/auth/patch").hasAnyAuthority("REFACTOR"); // si hace match a /patch y tiene permiso de REFACTOR

                    // configurar el resto de endpoints - NO ESPECIFICADOS
                    http.anyRequest().denyAll(); // cualquier otro request diferente a los de arriba, denegar el acceso
//                    http.anyRequest().authenticated(); // mas restrictivo es denyAll, authenticate deja pasar si estas autenticado
                }).addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }


// se puede trabajar la configuracion de seguridad con anotaciones de springSecurity, es otra forma de hacerlo, usando las anotaciones
// @EnableMethodSecurity, que permite usar antoaciones. Las condiciones Se configuran directamente en el controlador usando @Preauthorize

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .csrf(csrf-> csrf.disable()) // deshabilitada, xq no vamos a trabjar con form
//                .httpBasic(Customizer.withDefaults())
//                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // sin estado, xq no vamos a guardar la sesion en memoria, si no que el tiempo de duracion de la session va a depender del token, de cuando expire
//                .build();
//    }



    // administra la autenticacion. Se pasa como parametro el objeto AuthenticationConfiguration, que ya existe no hace falta inyectarlo
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    // DaoAuthenticatinProvider necesita dos componenetes : el userDetail y el passwordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);// este es el q se va a conectar a la bd
        return provider;
    }




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // NoOpPassWord..deprecado,  SOLO para pruebas ojo, ya que no codifica. mas adelante usaremos bcrypt
    }



    }
