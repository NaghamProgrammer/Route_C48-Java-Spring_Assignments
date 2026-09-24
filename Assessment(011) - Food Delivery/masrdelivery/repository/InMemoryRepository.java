package masrdelivery.repository;

import masrdelivery.exceptions.DuplicateIdException;
import masrdelivery.exceptions.NotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//HashMap so lookup by id is O(1)
public class InMemoryRepository<T, ID> implements Repository<T, ID> {
    private final Map<ID, T> store = new HashMap<>();
    private final String entityLabel;

    public InMemoryRepository(String entityLabel) { this.entityLabel = entityLabel; }

    @Override public void add(T entity, ID id) {
        if (store.containsKey(id)) throw new DuplicateIdException(entityLabel + " id " + id + " already exists");
        store.put(id, entity);
    }
    @Override public Optional<T> findById(ID id) { return Optional.ofNullable(store.get(id)); }
    @Override public T getById(ID id) {
        T entity = store.get(id);
        if (entity == null) throw new NotFoundException(entityLabel + " " + id + " not found");
        return entity;
    }
    @Override public void remove(ID id) {
        if (store.remove(id) == null) throw new NotFoundException(entityLabel + " " + id + " not found");
    }
    @Override public Collection<T> findAll() { return Collections.unmodifiableCollection(store.values()); }
    @Override public boolean exists(ID id) { return store.containsKey(id); }
}
