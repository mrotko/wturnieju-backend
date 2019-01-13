package pl.wturnieju.controller.dto.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import pl.wturnieju.config.AuthorityType;

@Getter
@Setter
public class UserConfigDTO {

    public Set<AuthorityType> authorityTypes = new HashSet<>();

    public UserConfigDTO() {
        initAuthorities();
    }

    private void initAuthorities() {
        authorityTypes.clear();
        authorityTypes.addAll(Arrays.asList(AuthorityType.values()));
    }
}
