package pl.wturnieju.model;

import org.springframework.data.annotation.Id;
import org.springframework.lang.NonNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Persistent implements Comparable<Persistent> {

    @Id
    protected String id;

    @Override
    public final int compareTo(@NonNull Persistent o) {
        return id.compareTo(o.getId());
    }
}
