package com.puc.realconsult.repository;

import com.puc.realconsult.model.UsuarioApiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioApiRepository extends JpaRepository<UsuarioApiModel, Long> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    UsuarioApiModel findByLogin(String login);
    UsuarioApiModel findByEmail(String email);
    @Query("SELECT u FROM UsuarioApiModel u WHERE u.idCliente.idCliente = :idCliente")
    UsuarioApiModel findByIdCliente(@Param("idCliente") Long idCliente);
}
