package com.puc.realconsult.controller;

import com.puc.realconsult.model.AuditoriaModel;
import com.puc.realconsult.service.AuditoriaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private final AuditoriaService service;

    @PostMapping
    public ResponseEntity<AuditoriaModel> criarAuditoria(@Valid @RequestBody AuditoriaModel auditoria){
        service.cadastrar(auditoria);
        return ResponseEntity.ok().body(auditoria);
    }

    @GetMapping
    public ResponseEntity<List<AuditoriaModel>> listarAuditorias() {
        List<AuditoriaModel> auditorias = service.listar();
        return ResponseEntity.ok(auditorias);
    }

}