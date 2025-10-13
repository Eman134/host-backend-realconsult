package com.puc.realconsult.controller;


import com.puc.realconsult.model.ClienteModel;
import com.puc.realconsult.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class UsoApiClienteController {

    @Autowired
    private ClienteService clienteService;

    // =========================
    // Consultas contratadas
    // =========================
    @GetMapping("/{id}/consultascontratadas")
    public Map<String, Integer> getConsultasContratadas(@PathVariable Long id) {
        int total = clienteService.getConsultasContratadas(id);
        return Map.of("consultasContratadas", total);
    }

    // =========================
    // Consultas realizadas
    // =========================
    @GetMapping("/{id}/consultasrealizadas")
    public Map<String, Integer> getConsultasRealizadas(@PathVariable Long id) {
        int total = clienteService.getConsultasRealizadas(id);
        return Map.of("consultasRealizadas", total);
    }

    // =========================
    // Saldo de consultas
    // =========================
    @GetMapping("/{id}/saldoconsultas")
    public Map<String, Integer> getSaldoConsultas(@PathVariable Long id) {
        int saldo = clienteService.getSaldoConsultas(id);
        return Map.of("saldoConsultas", saldo);
    }

    // =========================
    // CRUD
    // =========================
    @GetMapping
    public Iterable<ClienteModel> listarTodos() {
        return clienteService.listarTodos();
    }

    @PostMapping
    public ClienteModel criar(@RequestBody ClienteModel cliente) {
        return clienteService.criar(cliente);
    }

    @PutMapping("/{id}")
    public ClienteModel atualizar(@PathVariable Long id, @RequestBody ClienteModel clienteAtualizado) {
        return clienteService.atualizar(id, clienteAtualizado);
    }
    @GetMapping("/teste")
    public ResponseEntity<String> teste(){
        return ResponseEntity.ok().body("funcionando");
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return Map.of("mensagem", "✅ Cliente removido com sucesso.");
    }
}
