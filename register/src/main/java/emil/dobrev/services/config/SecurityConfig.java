package emil.dobrev.services.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthFilter;
    @Qualifier("doctorAuthenticationProvider")
    private final AuthenticationProvider doctorAuthenticationProvider;
    @Qualifier("patientAuthenticationProvider")
    private final AuthenticationProvider patientAuthenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          AuthenticationProvider doctorAuthenticationProvider,
                          AuthenticationProvider patientAuthenticationProvider
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.doctorAuthenticationProvider = doctorAuthenticationProvider;
        this.patientAuthenticationProvider = patientAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests((authz) ->
                                authz
                                        .requestMatchers("/api/v1/doctors/login", "/api/v1/patients/login").permitAll()

                                        .requestMatchers(HttpMethod.POST, "/api/v1/doctors", "/api/v1/patients").permitAll()
                                        .anyRequest().authenticated()

                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(doctorAuthenticationProvider)
                .authenticationProvider(patientAuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
