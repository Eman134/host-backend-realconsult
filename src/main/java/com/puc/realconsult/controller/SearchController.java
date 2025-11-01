package com.puc.realconsult.controller;

import com.puc.realconsult.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final ClienteRepository clienteRepository;
    private final DespesaRepository despesaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> search(@RequestParam String q) {
        String termo = q.trim();

        Map<String, Object> resultados = new LinkedHashMap<>();

        resultados.put("clientes", Optional.ofNullable(
                clienteRepository.findByNomeEmpresaContainingIgnoreCase(termo)
        ).orElse(Collections.emptyList()));

        resultados.put("despesas", Optional.ofNullable(
                despesaRepository.findByTituloContainingIgnoreCase(termo)
        ).orElse(Collections.emptyList()));

        resultados.put("usuarios", Optional.ofNullable(
                userRepository.findByNomeContainingIgnoreCase(termo)
        ).orElse(Collections.emptyList()));

        return ResponseEntity.ok(resultados);
    }
}