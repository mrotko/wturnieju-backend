package pl.wturnieju.dto.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import pl.wturnieju.config.AuthorityType;

@Data
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
