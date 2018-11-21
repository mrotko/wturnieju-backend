package pl.wturnieju.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User extends Persistent implements UserDetails, IProfile {

    private String username;

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

    private String name;

    private String surname;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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

    public String getFullName() {
        if (name != null && surname != null) {
            return name + " " + surname;
        }
        if (name != null) {
            return name;
        }
        return username;
    }
}
