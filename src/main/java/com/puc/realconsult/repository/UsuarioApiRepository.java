package com.puc.realconsult.repository;

import com.puc.realconsult.model.UsuarioApiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioApiRepository extends JpaRepository<UsuarioApiModel, Long> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    UsuarioApiModel findByLogin(String login);
    UsuarioApiModel findByEmail(String email);
}
