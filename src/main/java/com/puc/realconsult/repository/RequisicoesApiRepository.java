package com.puc.realconsult.repository;

import com.puc.realconsult.model.RequisicoesApiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface RequisicoesApiRepository extends JpaRepository<RequisicoesApiModel,Long> {
    List<RequisicoesApiModel> findByLogin(String login);

    @Query("""
        SELECT w FROM RequisicoesApiModel w
        JOIN UsuarioApiModel u ON w.login = u.login
        JOIN u.idCliente c
        WHERE c.ufDefault = :uf
        """)
    List<RequisicoesApiModel> findByUf(@Param("uf") String uf);


    @Query("""
        SELECT w FROM RequisicoesApiModel w
        JOIN UsuarioApiModel u ON w.login = u.login
        JOIN u.idCliente c
        WHERE c.baseMapaCliente = :mapa
        """)
    List<RequisicoesApiModel> findByMapa(@Param("mapa") String mapa);

    @Query("""
        SELECT w FROM RequisicoesApiModel w
        JOIN UsuarioApiModel u ON w.login = u.login
        JOIN u.idCliente c
        WHERE c.perfilConsulta = :tipoContrato
        """)
    List<RequisicoesApiModel> findByTipoContrato(@Param("tipoContrato") String tipoContrato);

    List<RequisicoesApiModel> findByBeginBetween(Timestamp inicio, Timestamp fim);


}
