package com.puc.realconsult.controller;

import com.puc.realconsult.model.FuncionarioModel;
import com.puc.realconsult.service.FuncionarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auditoria/{auditoriaId}")
public class FuncionarioController {

    private final FuncionarioService service;

    @PostMapping
    public ResponseEntity<?> importarPlanilha(@PathVariable Long auditoriaId, @RequestParam("file") MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            FuncionarioService.DadosImportacao dados = service.importarDados(is, auditoriaId);
            return ResponseEntity.ok().body(dados);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<FuncionarioModel>> listarFuncionarios(@PathVariable Long auditoriaId,
        @RequestParam(required = false) String busca) {
            try {
                List<FuncionarioModel> funcionarios;
                funcionarios = service.listarPorAuditoria(auditoriaId, busca);
                return ResponseEntity.ok(funcionarios);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
    }
}
