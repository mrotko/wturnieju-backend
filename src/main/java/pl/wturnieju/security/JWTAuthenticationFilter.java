package pl.wturnieju.security;

import static pl.wturnieju.security.SecurityConstants.EXPIRATION_TIME;
import static pl.wturnieju.security.SecurityConstants.HEADER_STRING;
import static pl.wturnieju.security.SecurityConstants.SECRET;
import static pl.wturnieju.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import pl.wturnieju.dto.LoginDTO;
import pl.wturnieju.exception.UserNotFoundException;
import pl.wturnieju.model.User;
import pl.wturnieju.service.IUserService;

@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private IUserService userService;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) {
        try {
            LoginDTO credentials = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);

            if (!userService.check(credentials.getEmail(), credentials.getPassword())) {
                throw new UserNotFoundException(
                        "Login failed. User " + credentials.getEmail() + " not found");
            }

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    credentials.getEmail(),
                    credentials.getPassword(),
                    new ArrayList<>()
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(authResult.getPrincipal()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
