package com.puc.realconsult.controller;

import com.puc.realconsult.model.ClienteModel;
import com.puc.realconsult.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/economies")
public class EconomiaController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/filtrar")
    public ResponseEntity<List<ClienteModel>> filtrarEconomias(
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String tipoContrato,
            @RequestParam(required = false) Long cliente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        try {
            Iterable<ClienteModel> clientesIterable = clienteService.listarTodos();
            List<ClienteModel> clientes = new ArrayList<>();
            clientesIterable.forEach(clientes::add);
            
            // Filtrar por cliente específico se fornecido
            if (cliente != null) {
                clientes = clientes.stream()
                    .filter(c -> c.getIdCliente().equals(cliente))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Filtrar por UF se fornecida
            if (uf != null && !uf.trim().isEmpty()) {
                clientes = clientes.stream()
                    .filter(c -> c.getUfDefault() != null && c.getUfDefault().equalsIgnoreCase(uf))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Filtrar por região se fornecida (baseado na UF)
            if (regiao != null && !regiao.trim().isEmpty()) {
                clientes = clientes.stream()
                    .filter(c -> {
                        String ufCliente = c.getUfDefault();
                        if (ufCliente == null) return false;
                        
                        // Mapear UF para região
                        switch (regiao.toLowerCase()) {
                            case "norte":
                                return ufCliente.matches("AC|AP|AM|PA|RO|RR|TO");
                            case "nordeste":
                                return ufCliente.matches("AL|BA|CE|MA|PB|PE|PI|RN|SE");
                            case "centro-oeste":
                                return ufCliente.matches("GO|MT|MS|DF");
                            case "sudeste":
                                return ufCliente.matches("ES|MG|RJ|SP");
                            case "sul":
                                return ufCliente.matches("PR|RS|SC");
                            default:
                                return false;
                        }
                    })
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Filtrar por tipo de contrato se fornecido
            if (tipoContrato != null && !tipoContrato.trim().isEmpty()) {
                clientes = clientes.stream()
                    .filter(c -> c.getPerfilConsulta() != null && c.getPerfilConsulta().equalsIgnoreCase(tipoContrato))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(List.of());
        }
    }

    @GetMapping("/regioes")
    public ResponseEntity<List<String>> listarRegioes() {
        try {
            List<String> regioes = List.of(
                "Norte", "Nordeste", "Centro-Oeste", "Sudeste", "Sul"
            );
            return ResponseEntity.ok(regioes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @GetMapping("/ufs")
    public ResponseEntity<List<String>> listarUFs() {
        try {
            List<String> ufs = List.of(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", 
                "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", 
                "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
            );
            return ResponseEntity.ok(ufs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }
}
