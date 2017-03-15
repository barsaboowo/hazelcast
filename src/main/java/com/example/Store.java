package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by b on 14/3/17.
 */
@Service
public class Store<KeyType extends Serializable, EntityType extends Serializable> implements com.hazelcast.core.MapStore<KeyType, EntityType> {

    @Autowired
    private CassandraRepository<EntityType> repository;

    @Override
    public void store(KeyType key, EntityType value) {
        repository.save(value);
    }

    @Override
    public void storeAll(Map<KeyType, EntityType> map) {
        repository.save(map.values());
    }

    @Override
    public void delete(KeyType key) {
        repository.delete(BasicMapId.id("id", key));
    }

    @Override
    public void deleteAll(Collection<KeyType> collection) {
        collection.forEach(this::delete);
    }

    @Override
    public EntityType load(KeyType key) {
        return repository.findOne(BasicMapId.id("uuid", key));
    }

    @Override
    public Map<KeyType, EntityType> loadAll(Collection<KeyType> collection) {
        Map<KeyType, EntityType> entityTypeMap = new HashMap<>();
        collection.forEach(key -> {
            EntityType entity = load(key);
            Optional.ofNullable(entity).ifPresent(e -> entityTypeMap.put(key, e));
        });
        return entityTypeMap;
    }

    @Override
    public Iterable<KeyType> loadAllKeys() {
        return null;
    }
}
