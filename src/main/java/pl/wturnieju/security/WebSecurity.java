package pl.wturnieju.security;

import static pl.wturnieju.security.SecurityConstants.HEADER_STRING;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.AllArgsConstructor;
import pl.wturnieju.service.IUserService;

@AllArgsConstructor
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private IUserService userService;

    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/register/**").permitAll()
                .antMatchers("/auth/login/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(createAuthenticationFilter())
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    private JWTAuthenticationFilter createAuthenticationFilter() throws Exception {
        var authFilter = new JWTAuthenticationFilter(authenticationManager(), userService);
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
        corsConfiguration.addExposedHeader(HEADER_STRING);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
