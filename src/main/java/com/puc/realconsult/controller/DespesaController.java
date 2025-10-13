package com.puc.realconsult.controller;

import com.puc.realconsult.model.DespesaModel;
import com.puc.realconsult.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/despesas")
@CrossOrigin(origins = "http://localhost:3000")
public class DespesaController {
    
    @Autowired
    private DespesaService despesaService;
    
    @GetMapping
    public ResponseEntity<List<DespesaModel>> listarDespesas(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String periodo) {
        
        List<DespesaModel> despesas;
        
        if (busca != null && !busca.trim().isEmpty()) {
            despesas = despesaService.buscarPorTermo(busca);
        } else if (categoria != null && !categoria.trim().isEmpty()) {
            despesas = despesaService.buscarPorCategoria(categoria);
        } else if (periodo != null && !periodo.trim().isEmpty()) {
            // Assumindo formato "YYYY-MM-DD,YYYY-MM-DD" para período
            String[] datas = periodo.split(",");
            if (datas.length == 2) {
                LocalDate dataInicio = LocalDate.parse(datas[0]);
                LocalDate dataFim = LocalDate.parse(datas[1]);
                despesas = despesaService.buscarPorPeriodo(dataInicio, dataFim);
            } else {
                despesas = despesaService.listarTodas();
            }
        } else {
            despesas = despesaService.listarTodas();
        }
        
        return ResponseEntity.ok(despesas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DespesaModel> buscarPorId(@PathVariable Long id) {
        return despesaService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> criarDespesa(@Valid @RequestBody DespesaModel despesa) {
        try {
            DespesaModel despesaSalva = despesaService.salvar(despesa);
            return ResponseEntity.ok(despesaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("erro", "Erro ao criar despesa: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarDespesa(@PathVariable Long id, @Valid @RequestBody DespesaModel despesa) {
        try {
            DespesaModel despesaAtualizada = despesaService.atualizar(id, despesa);
            return ResponseEntity.ok(despesaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("erro", "Erro ao atualizar despesa: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirDespesa(@PathVariable Long id) {
        try {
            despesaService.excluir(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("erro", "Erro ao excluir despesa: " + e.getMessage()));
        }
    }
    
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        List<String> categorias = despesaService.listarCategorias();
        return ResponseEntity.ok(categorias);
    }
}
