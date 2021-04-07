package com.eunhye.api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);
    // UserEntity 의 영속성을 관리하고, 관련 CRUD 를 자동으로 생성
    // Optional<User> findByUserId(String id);
    // 로그인시 DB 에 저장된 User 의정보를 가져오기 위해 사용
}
