package pl.wturnieju.dto;

import pl.wturnieju.model.Persistent;

public interface EntityMapping<T extends Persistent> {

    void assignFields(T entity);
}
