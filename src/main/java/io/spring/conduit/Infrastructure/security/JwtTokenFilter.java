package io.spring.conduit.Infrastructure.security;

import io.spring.conduit.service.JwtService;
import io.spring.conduit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private UserService userService;
    private JwtService jwtService;
    private AuthenticationFacade authenticationFacade;

    private final String HEADER = "Authorization";

    @Autowired
    public JwtTokenFilter(UserService userService, JwtService jwtService, AuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getTokenString(request.getHeader(HEADER)).ifPresent(token -> {
            jwtService.getSubFromToken(token).ifPresent(id -> {
                if(authenticationFacade.getAuthentication()==null){
                    userService.findById(id).ifPresent(user -> {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                Collections.emptyList()
                        );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    });
                }
            });
        });
    }

    private Optional<String> getTokenString(String header){
        if (header==null){
            return Optional.empty();
        } else {
            String[] split = header.split(" ");
            if (split.length < 2) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(split[1]);
            }
        }
    }

}
