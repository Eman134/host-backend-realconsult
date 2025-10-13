package com.puc.realconsult.repository;

import com.puc.realconsult.model.DespesaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<DespesaModel, Long> {
    
    // Buscar despesas por termo (título, cliente ou categoria)
    @Query("SELECT d FROM DespesaModel d WHERE " +
           "LOWER(d.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(d.clienteContrato) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(d.categoria) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<DespesaModel> findByTermo(@Param("termo") String termo);
    
    // Buscar despesas por categoria
    List<DespesaModel> findByCategoria(String categoria);
    
    // Buscar despesas por período
    List<DespesaModel> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);
    
    // Buscar despesas por categoria e período
    List<DespesaModel> findByCategoriaAndDataBetween(String categoria, LocalDate dataInicio, LocalDate dataFim);
    
    // Buscar despesas por status
    List<DespesaModel> findByStatus(DespesaModel.StatusDespesa status);
    
    // Ordenar por data decrescente
    List<DespesaModel> findAllByOrderByDataDesc();
}
