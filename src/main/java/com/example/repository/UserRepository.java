package com.example.repository;

import com.example.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by b on 15/3/17.
 */
@Repository
public interface UserRepository extends CassandraRepository<User> {

    @Query("select uuid from user")
    public List<String> getAllKeys();
}
