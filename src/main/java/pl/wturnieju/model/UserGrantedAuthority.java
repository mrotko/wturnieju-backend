package pl.wturnieju.model;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import pl.wturnieju.config.AuthorityType;

@Data
public class UserGrantedAuthority implements GrantedAuthority {

    private AuthorityType authorityType;

    @JsonCreator
    public UserGrantedAuthority(@JsonProperty("authority") AuthorityType authorityType) {
        this.authorityType = authorityType;
    }

    @Override
    public String getAuthority() {
        return authorityType.name();
    }
}
