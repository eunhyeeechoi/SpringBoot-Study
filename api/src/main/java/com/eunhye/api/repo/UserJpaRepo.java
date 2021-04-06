package com.eunhye.api.repo;

import com.eunhye.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {

    //Optional<User> findUid(String email);

    //Optional<User> findByUidAndProvider(String uid, String provider);

    // default List<User> findAll() {
    //   return User;
    //}
}
