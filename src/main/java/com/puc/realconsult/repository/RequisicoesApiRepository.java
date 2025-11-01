package com.puc.realconsult.repository;

import com.puc.realconsult.model.RequisicoesApiModel;
import com.puc.realconsult.model.UsuarioApiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    @Query(value = """
    SELECT begin FROM requisicoes_api
    WHERE begin >= CAST(CONCAT(:ano, '-01-01') AS DATETIME)
      AND begin <  CAST(CONCAT(:ano + 1, '-01-01') AS DATETIME)
      AND login = :login""", nativeQuery = true)
    List<Timestamp> buscarTodasReqDatas(@Param("ano") Integer ano, @Param("login" ) String login);

    @Query(value = """
            SELECT COUNT(BEGIN) FROM requisicoes_api
            WHERE begin >= :inicio 
            AND begin <= :fim
            AND login = :login """, nativeQuery = true)
    Integer buscarReqPeriodoComLogin(@Param("inicio") Timestamp inicio,
                                             @Param("fim") Timestamp fim,
                                             @Param("login") String login);

    @Query(value = """
            SELECT * FROM requisicoes_api
            WHERE begin >= :inicio 
            AND begin <= :fim
            AND login = :login """, nativeQuery = true)
    List<RequisicoesApiModel> buscarReqPeriodoComTodosOsCampos(@Param("inicio") Timestamp inicio,
                                                     @Param("fim") Timestamp fim,
                                                     @Param("login") String login);


    @Query(value = """
            SELECT COUNT(BEGIN) FROM requisicoes_api
            WHERE login = :login
            """,  nativeQuery = true)
    Integer buscarTodasReqPorLogin(@Param("login") String login);

    @Query(value = """
            SELECT * FROM requisicoes_api
            WHERE login = :login """, nativeQuery = true)
    List<RequisicoesApiModel> buscarReqComTodosOsCampos(@Param("login") String login);


}
