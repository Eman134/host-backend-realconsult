package com.puc.realconsult.repository;

import com.puc.realconsult.model.ResetSenhaToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetSenhaTokenRepository extends JpaRepository<ResetSenhaToken, Long> {
    Optional<ResetSenhaToken> findByToken(String token);
}
