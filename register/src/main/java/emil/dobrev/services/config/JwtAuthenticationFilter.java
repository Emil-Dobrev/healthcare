package emil.dobrev.services.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Qualifier("doctorDetailsService")
    private final UserDetailsService doctorUserDetailService;
    @Qualifier("patientDetailsService")
    private final UserDetailsService patientUserDetailService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            @Qualifier("doctorDetailsService") UserDetailsService doctorUserDetailService,
            @Qualifier("patientDetailsService") UserDetailsService patientUserDetailService
    ) {
        this.jwtService = jwtService;
        this.doctorUserDetailService = doctorUserDetailService;
        this.patientUserDetailService = patientUserDetailService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userId = jwtService.extractUsername(jwt);

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            if (request.getRequestURI().startsWith("/api/v1/doctors")) {
                userDetails = doctorUserDetailService.loadUserByUsername(userId);
            } else if (request.getRequestURI().startsWith("/api/v1/patients")) {
                userDetails = patientUserDetailService.loadUserByUsername(userId);
            }

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}