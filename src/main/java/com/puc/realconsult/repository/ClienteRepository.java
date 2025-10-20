package com.puc.realconsult.repository;

import com.puc.realconsult.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
    Optional<ClienteModel> findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);

    @Query(value = """
        SELECT * FROM cliente 
        WHERE LOWER(nome_empresa) LIKE LOWER(CONCAT('%', :termo, '%'))
           OR REPLACE(REPLACE(REPLACE(cnpj, '.', ''), '/', ''), '-', '') 
              LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(:termo, '.', ''), '/', ''), '-', ''), '%')
    """, nativeQuery = true)
    List<ClienteModel> buscarPorNomeOuCnpj(@Param("termo") String termo);

    //Buscas no Header
    List<ClienteModel> findByNomeEmpresaContainingIgnoreCase(String nomeEmpresa);

}
