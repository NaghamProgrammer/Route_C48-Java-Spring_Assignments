package masrdelivery.repository;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T, ID> {
    void add(T entity, ID id);
    Optional<T> findById(ID id);
    T getById(ID id);
    void remove(ID id);
    Collection<T> findAll();
    boolean exists(ID id);
}
