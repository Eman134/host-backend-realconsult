package com.puc.realconsult.service;

import com.puc.realconsult.model.DespesaModel;
import com.puc.realconsult.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {
    
    @Autowired
    private DespesaRepository despesaRepository;
    
    public List<DespesaModel> listarTodas() {
        return despesaRepository.findAllByOrderByDataDesc();
    }
    
    public List<DespesaModel> buscarPorTermo(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodas();
        }
        return despesaRepository.findByTermo(termo.trim());
    }
    
    public List<DespesaModel> buscarPorCategoria(String categoria) {
        return despesaRepository.findByCategoria(categoria);
    }
    
    public List<DespesaModel> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return despesaRepository.findByDataBetween(dataInicio, dataFim);
    }
    
    public List<DespesaModel> buscarPorCategoriaEPeriodo(String categoria, LocalDate dataInicio, LocalDate dataFim) {
        return despesaRepository.findByCategoriaAndDataBetween(categoria, dataInicio, dataFim);
    }
    
    public Optional<DespesaModel> buscarPorId(Long id) {
        return despesaRepository.findById(id);
    }
    
    public DespesaModel salvar(DespesaModel despesa) {
        return despesaRepository.save(despesa);
    }
    
    public DespesaModel atualizar(Long id, DespesaModel despesaAtualizada) {
        DespesaModel despesaExistente = despesaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Despesa não encontrada com ID: " + id));
        
        despesaExistente.setTitulo(despesaAtualizada.getTitulo());
        despesaExistente.setClienteContrato(despesaAtualizada.getClienteContrato());
        despesaExistente.setCategoria(despesaAtualizada.getCategoria());
        despesaExistente.setValor(despesaAtualizada.getValor());
        despesaExistente.setData(despesaAtualizada.getData());
        despesaExistente.setDescricao(despesaAtualizada.getDescricao());
        despesaExistente.setStatus(despesaAtualizada.getStatus());
        
        return despesaRepository.save(despesaExistente);
    }
    
    public void excluir(Long id) {
        if (!despesaRepository.existsById(id)) {
            throw new RuntimeException("Despesa não encontrada com ID: " + id);
        }
        despesaRepository.deleteById(id);
    }
    
    public List<String> listarCategorias() {
        return despesaRepository.findAll().stream()
            .map(DespesaModel::getCategoria)
            .distinct()
            .sorted()
            .toList();
    }
}
