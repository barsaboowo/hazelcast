package com.example.repository;

import com.example.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by b on 15/3/17.
 */
@Repository
public interface UserRepository extends CassandraRepository<User> {
}
