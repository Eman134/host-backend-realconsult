package com.puc.realconsult.controller;

import com.puc.realconsult.dto.ClienteDTO;
import com.puc.realconsult.model.ClienteModel;
import com.puc.realconsult.service.ClienteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService service;

    @GetMapping("/listartodos")
    public ResponseEntity<List<ClienteModel>> listarTodos(){
        List<ClienteModel> clientes = service.listar();
        return clientes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscarporid/{id}")
    public ResponseEntity<ClienteModel> buscarPorId(@PathVariable("id") long id){
        Optional<ClienteModel> cliente = service.buscarPorId(id);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/criar")
    public ResponseEntity<ClienteModel> criar(@Valid @RequestBody ClienteModel cliente){
        service.cadastrar(cliente);
        return ResponseEntity.ok().body(cliente);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ClienteModel> deletar(@Valid @PathVariable("id") long id){
        service.deletarCliente(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/buscarporcnpj/{cnpj}")
    public ResponseEntity<ClienteModel> buscarPorCnpj(@PathVariable("cnpj") String cnpj){
        Optional<ClienteModel> cliente = service.buscarPorCnpj(cnpj);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarpornome")
    public ResponseEntity<List<ClienteModel>> buscarPorNome(@RequestParam("nome") String nome){
        List<ClienteModel> clientes = service.listarPorNome(nome);
        return  clientes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(clientes);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ClienteModel> atualizar(@Valid @RequestBody ClienteDTO cliente, @PathVariable("id") long id){
        service.atualizarCliente(id ,cliente);
        return ResponseEntity.ok().build();
    }
}
