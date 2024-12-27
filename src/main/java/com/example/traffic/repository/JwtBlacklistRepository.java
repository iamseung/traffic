package com.example.traffic.repository;

import com.example.traffic.entity.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    Optional<JwtBlacklist> findByToken(String token);

    // 가장 최근에 만료 요청을 한
    @Query(value = "SELECT * FROM jwt_blacklist WHERE username = :username ORDER BY expiration_time LIMIT 1", nativeQuery = true)
    Optional<JwtBlacklist> findTopByUsernameOrderByExpirationTime(@Param("username") String username);
}
