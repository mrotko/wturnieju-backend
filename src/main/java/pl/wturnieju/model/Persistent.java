package pl.wturnieju.model;

import org.springframework.data.annotation.Id;
import org.springframework.lang.NonNull;

import lombok.Data;

@Data
public class Persistent implements Comparable<Persistent> {

    @Id
    protected String id;

    protected Timestamp createdAt = Timestamp.now();

    @Override
    public final int compareTo(@NonNull Persistent o) {
        return id.compareTo(o.getId());
    }
}
