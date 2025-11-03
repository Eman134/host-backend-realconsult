package com.puc.realconsult.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.puc.realconsult.dto.EconomiaDTO;
import com.puc.realconsult.model.ClienteModel;
import com.puc.realconsult.service.ClienteService;
import com.puc.realconsult.service.EconomiaService;

@RestController
@RequestMapping("/api/economies")
public class EconomiaController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EconomiaService economiaService;

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

    @GetMapping("/auditoria/{auditoriaId}")
    public ResponseEntity<EconomiaDTO> getEconomiaPorAuditoria(
            @PathVariable Long auditoriaId,
            @RequestParam(required = false) Integer mesReferencia,
            @RequestParam(required = false) Integer anoReferencia) {
        try {
            System.out.println("DEBUG EconomiaController - GET /auditoria/" + auditoriaId + 
                    " (mesRef: " + mesReferencia + ", anoRef: " + anoReferencia + ")");
            EconomiaDTO economia = economiaService.calcularEconomiaPorAuditoria(auditoriaId, mesReferencia, anoReferencia);
            System.out.println("DEBUG EconomiaController - Retornando dados para auditoria " + auditoriaId);
            return ResponseEntity.ok(economia);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR EconomiaController - Auditoria não encontrada: " + auditoriaId);
            System.err.println("  - Exceção: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("ERROR EconomiaController - Erro ao calcular economia para auditoria " + auditoriaId);
            System.err.println("  - Exceção: " + e.getClass().getName());
            System.err.println("  - Mensagem: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/auditoria/{auditoriaId}/mensal")
    public ResponseEntity<List<EconomiaDTO>> getEconomiaMensalPorAuditoria(
            @PathVariable Long auditoriaId,
            @RequestParam(required = false) Integer ano) {
        
        // Log imediato para garantir que o método está sendo chamado
        System.out.println("========================================");
        System.out.println("DEBUG EconomiaController - MÉTODO CHAMADO!");
        System.out.println("  - Endpoint: /auditoria/" + auditoriaId + "/mensal");
        System.out.println("  - Ano filtro: " + ano);
        System.out.println("========================================");
        
        try {
            
            // Validar auditoriaId
            if (auditoriaId == null || auditoriaId <= 0) {
                System.err.println("ERROR EconomiaController - auditoriaId inválido: " + auditoriaId);
                return ResponseEntity.badRequest().body(List.of());
            }
            
            List<EconomiaDTO> economiasMensais = economiaService.calcularEconomiaMensalPorAuditoria(auditoriaId, ano);
            
            System.out.println("DEBUG EconomiaController - Retornando " + economiasMensais.size() + " meses de dados para auditoria " + auditoriaId);
            
            if (!economiasMensais.isEmpty()) {
                System.out.println("DEBUG EconomiaController - Primeiro mês: " + economiasMensais.get(0).mesReferencia() + "/" + economiasMensais.get(0).anoReferencia());
                System.out.println("DEBUG EconomiaController - Último mês: " + economiasMensais.get(economiasMensais.size() - 1).mesReferencia() + "/" + economiasMensais.get(economiasMensais.size() - 1).anoReferencia());
            } else {
                System.out.println("DEBUG EconomiaController - Nenhum dado mensal encontrado para auditoria " + auditoriaId);
            }
            
            // Sempre retornar 200 OK, mesmo que a lista esteja vazia (é válido não ter dados mensais)
            return ResponseEntity.ok(economiasMensais);
            
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR EconomiaController - Auditoria não encontrada: " + auditoriaId);
            System.err.println("  - Mensagem: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("ERROR EconomiaController - Erro ao buscar dados mensais para auditoria " + auditoriaId);
            System.err.println("  - Exceção: " + e.getClass().getName());
            System.err.println("  - Mensagem: " + e.getMessage());
            e.printStackTrace();
            // Retornar 200 com lista vazia em vez de 400, pois lista vazia é um resultado válido
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<EconomiaDTO>> getEconomiaPorCliente(@PathVariable Long clienteId) {
        try {
            List<EconomiaDTO> economias = economiaService.calcularEconomiaPorCliente(clienteId);
            return ResponseEntity.ok(economias);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @GetMapping("/cliente/{clienteId}/agregado")
    public ResponseEntity<EconomiaDTO> getEconomiaAgregadaPorCliente(
            @PathVariable Long clienteId,
            @RequestParam(required = false) Integer mesReferencia,
            @RequestParam(required = false) Integer anoReferencia) {
        try {
            EconomiaDTO economia = economiaService.calcularEconomiaAgregadaPorCliente(clienteId, mesReferencia, anoReferencia);
            return ResponseEntity.ok(economia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/cliente/{clienteId}/agregado/mensal")
    public ResponseEntity<List<EconomiaDTO>> getEconomiaMensalAgregadaPorCliente(
            @PathVariable Long clienteId,
            @RequestParam(required = false) Integer ano) {
        try {
            List<EconomiaDTO> economias = economiaService.calcularEconomiaMensalPorCliente(clienteId, ano);
            return ResponseEntity.ok(economias);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @GetMapping
    public ResponseEntity<List<EconomiaDTO>> listarTodasEconomias() {
        try {
            List<EconomiaDTO> economias = economiaService.listarTodasEconomias();
            return ResponseEntity.ok(economias);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }
}
