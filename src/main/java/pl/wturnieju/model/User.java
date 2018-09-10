package pl.wturnieju.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wturnieju.model.generic.GenericProfile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Persistent implements UserDetails {

    private String username;

    private List<GenericProfile> profiles = new ArrayList<>();

    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean accountExpired = false;

    @JsonIgnore
    private boolean accountLocked = false;

    @JsonIgnore
    private boolean credentialsExpired = false;

    @JsonIgnore
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
