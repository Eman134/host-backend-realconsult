package com.puc.realconsult.controller;


import com.puc.realconsult.model.ClienteModel;
import com.puc.realconsult.model.RequisicoesApiModel;
import com.puc.realconsult.repository.RequisicoesApiRepository;
import com.puc.realconsult.service.ClienteService;
import com.puc.realconsult.service.UsoApiService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UsoApiClienteController {

    private final UsoApiService usoApiService;
    private final ClienteService clienteService;


    // 🔹 Retorna total de consultas realizadas filtradas
    @GetMapping("/cliente/{idCliente}/realizadas")
    public ResponseEntity<Integer> totalRealizadas(
            @PathVariable Long idCliente,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) String baseMapa,
            @RequestParam(required = false) String tipoContrato,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim
    ) {

        Timestamp inicioTS = dataInicio != null ? Timestamp.valueOf(dataInicio) : null;
        Timestamp fimTS = dataFim != null ? Timestamp.valueOf(dataFim) : null;

        int total = usoApiService.totalConsultasRealizadasComFiltro(
                idCliente, baseMapa, uf, tipoContrato, inicioTS, fimTS
        );

        return ResponseEntity.ok(total);
    }

    // 🔹 Retorna total de consultas restantes filtradas
    @GetMapping("/cliente/{idCliente}/saldo")
    public ResponseEntity<Integer> totalSaldo(
            @PathVariable Long idCliente,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) String baseMapa,
            @RequestParam(required = false) String tipoContrato,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim
    ) {

        Timestamp inicioTS = dataInicio != null ? Timestamp.valueOf(dataInicio) : null;
        Timestamp fimTS = dataFim != null ? Timestamp.valueOf(dataFim) : null;

        int saldo = usoApiService.totalConsultasSaldoComFiltro(
                idCliente, baseMapa, uf, tipoContrato, inicioTS, fimTS
        );

        return ResponseEntity.ok(saldo);
    }


    @GetMapping("/requisicoes/filtrar")
    public ResponseEntity<List<RequisicoesApiModel>> filtrarRequisicoes(
            @RequestParam(required = false) String baseMapa,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) String tipoContrato,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        Timestamp tsInicio = inicio != null ? Timestamp.valueOf(inicio) : null;
        Timestamp tsFim = fim != null ? Timestamp.valueOf(fim) : null;

        List<RequisicoesApiModel> filtradas = usoApiService.filtrarRequisicoes(baseMapa, uf, tipoContrato, tsInicio, tsFim);
        return ResponseEntity.ok(filtradas);
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
