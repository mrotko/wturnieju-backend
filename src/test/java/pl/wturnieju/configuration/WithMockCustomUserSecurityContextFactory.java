package pl.wturnieju.configuration;

import org.bson.types.ObjectId;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import pl.wturnieju.model.User;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = new User();
        user.setUsername(customUser.username());
        user.setPassword("12345");
        user.setId(new ObjectId().toString());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
                user.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
