package io.github.devtamakuwala.dailydine.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Firebase filter to authenticate firebase authentication token passed from front end as a Bearer token
 * */
@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = header.substring(7); // "Bearer ".length()

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            // You can add user roles/authorities from custom claims if you have them
            // List<GrantedAuthority> authorities = ...;
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    uid, null, new ArrayList<>()); // Using UID as the principal

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // Token is invalid, clear the context and let the chain continue.
            // The request will be rejected later by security rules.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
