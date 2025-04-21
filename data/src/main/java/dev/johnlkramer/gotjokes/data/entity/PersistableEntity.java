package dev.johnlkramer.gotjokes.data.entity;

import dev.johnlkramer.gotjokes.ModelType;
import java.util.List;

public interface PersistableEntity<M extends ModelType> {
    M toModel();

    static <M extends ModelType, E extends PersistableEntity<M>> List<M> entitiesToModel(List<E> entities) {
        return entities.stream().map(e -> e.toModel()).toList();
    }
}
