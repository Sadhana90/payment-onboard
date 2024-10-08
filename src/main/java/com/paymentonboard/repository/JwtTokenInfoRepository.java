package com.paymentonboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymentonboard.entity.JwtTokenInfo;

import java.util.Optional;

@Repository
public interface JwtTokenInfoRepository extends JpaRepository<JwtTokenInfo, Long> {

    Optional<JwtTokenInfo> findByTokenAndIsActive(String token, boolean isActive);
}
