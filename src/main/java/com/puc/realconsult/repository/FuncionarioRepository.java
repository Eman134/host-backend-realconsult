package com.puc.realconsult.repository;

import com.puc.realconsult.model.FuncionarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import com.puc.realconsult.model.FuncionarioModel;


import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<FuncionarioModel, Long> {

    Optional<FuncionarioModel> findByMatricula(Long matricula);

    List<FuncionarioModel> findByNomeFuncionarioContainingIgnoreCase(String nomeFuncionario);

    List<FuncionarioModel> findByAuditoriaId(Long auditoriaId);

    List<FuncionarioModel> findByAuditoriaIdAndNomeFuncionarioContainingIgnoreCase(Long auditoriaId, String nome);

    FuncionarioModel findByAuditoriaIdAndMatricula(Long auditoriaId, Long matricula);
    
    FuncionarioModel findByAuditoriaIdAndMatriculaAndMesReferenciaAndAnoReferencia(
        Long auditoriaId, Long matricula, Integer mesReferencia, Integer anoReferencia);
    
    List<FuncionarioModel> findByAuditoriaIdAndMesReferenciaAndAnoReferencia(
        Long auditoriaId, Integer mesReferencia, Integer anoReferencia);
}