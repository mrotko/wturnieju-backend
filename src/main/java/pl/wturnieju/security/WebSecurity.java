package pl.wturnieju.security;

import static pl.wturnieju.security.SecurityConstants.HEADER_STRING;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.service.IUserService;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final IUserService userService;

    private final PasswordEncoder passwordEncoder;

    private final Environment env;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (isAllAuth()) {
            http.cors().and().csrf().disable()
                    .authorizeRequests().anyRequest().permitAll();
        } else {
            http.cors().and().csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(createAuthenticationFilter())
                    .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    private JWTAuthenticationFilter createAuthenticationFilter() throws Exception {
        var authFilter = new JWTAuthenticationFilter(authenticationManager());
        authFilter.setFilterProcessesUrl("/auth/login");
        authFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
        return authFilter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var source = new UrlBasedCorsConfigurationSource();
        var corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addExposedHeader(HEADER_STRING);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    private boolean isAllAuth() {
        return Optional.ofNullable(env.getProperty("allAuth"))
                .map(Boolean::valueOf)
                .orElse(false);
    }
}
