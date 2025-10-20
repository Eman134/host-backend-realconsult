package com.puc.realconsult.service;

import com.puc.realconsult.dto.ClienteDTO;
import com.puc.realconsult.exception.BadRequestException;
import com.puc.realconsult.exception.ConflictException;
import com.puc.realconsult.exception.ResourceNotFound;
import com.puc.realconsult.model.ClienteModel;
import com.puc.realconsult.repository.ClienteRepository;
import com.puc.realconsult.utils.CnpjUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // =========================
    // Validação e busca do cliente
    // =========================
    private ClienteModel validarClienteExistente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("❌ Cliente com ID " + id + " não encontrado."));
    }

    // =========================
    // Consultas contratadas
    // =========================
    public int getConsultasContratadas(Long id) {
        ClienteModel cliente = validarClienteExistente(id);
        return cliente.getNumeroConsultas();
    }

    // =========================
    // Consultas realizadas
    // =========================
    public int getConsultasRealizadas(Long id) {
        ClienteModel cliente = validarClienteExistente(id);
        return cliente.getNumeroConsultasRealizadas();
    }

    // =========================
    // Saldo de consultas
    // =========================
    public int getSaldoConsultas(Long id) {
        ClienteModel cliente = validarClienteExistente(id);

        int contratadas = cliente.getNumeroConsultas();
        int realizadas = cliente.getNumeroConsultasRealizadas();

        if (realizadas > contratadas) {
            throw new IllegalStateException("⚠️ O número de consultas realizadas excede o contratado.");
        }

        return contratadas - realizadas;
    }

    // =========================
    // CRUD básico
    // =========================
    public Iterable<ClienteModel> listarTodos() {
        return clienteRepository.findAll();
    }

    public ClienteModel criar(ClienteModel cliente) {
        validarCamposObrigatorios(cliente);
        return clienteRepository.save(cliente);
    }

    public ClienteModel atualizar(Long id, ClienteModel atualizado) {
        ClienteModel existente = validarClienteExistente(id);
        validarCamposObrigatorios(atualizado);

        existente.setNumeroConsultas(atualizado.getNumeroConsultas());
        existente.setNumeroConsultasRealizadas(atualizado.getNumeroConsultasRealizadas());
        existente.setNomeEmpresa(atualizado.getNomeEmpresa());
        existente.setCnpj(atualizado.getCnpj());
        existente.setPath(atualizado.getPath());
        existente.setTipoCartao(atualizado.getTipoCartao());
        existente.setTipoRoteirizacao(atualizado.getTipoRoteirizacao());
        existente.setPerfilConsulta(atualizado.getPerfilConsulta());

        return clienteRepository.save(existente);
    }

    public void deletar(Long id) {
        ClienteModel cliente = validarClienteExistente(id);
        clienteRepository.delete(cliente);
    }

    // =========================
    // Validação de regras de negócio
    // =========================
    private void validarCamposObrigatorios(ClienteModel cliente) {
        if (cliente.getNomeEmpresa() == null || cliente.getNomeEmpresa().isBlank()) {
            throw new IllegalArgumentException("⚠️ O campo 'nomeEmpresa' é obrigatório.");
        }

        if (cliente.getCnpj() == null || cliente.getCnpj().isBlank()) {
            throw new IllegalArgumentException("⚠️ O campo 'cnpj' é obrigatório.");
        }

        if (cliente.getPath() == null || cliente.getPath().isBlank()) {
            throw new IllegalArgumentException("⚠️ O campo 'path' é obrigatório.");
        }

        if (cliente.getNumeroConsultas() < 0 || cliente.getNumeroConsultasRealizadas() < 0) {
            throw new IllegalArgumentException("⚠️ O número de consultas não pode ser negativo.");
        }
    }

    public void validarCnpj(ClienteModel cliente) {
        cliente.setCnpj(CnpjUtils.limparCnpj(cliente.getCnpj()));

        if (clienteRepository.existsByCnpj(cliente.getCnpj())){
            throw new ConflictException("Já existe um cliente com esse cnpj");
        }
        if (!CnpjUtils.validarCnpj(cliente.getCnpj())){
            throw new BadRequestException("Cnpj invalido");
        }
        if (cliente.getNomeEmpresa() == null || cliente.getNomeEmpresa().equals("")) {
            throw new BadRequestException("Nome deve ser preenchido");
        }
    }

    public void cadastrar(ClienteModel cliente) {
        validarCnpj(cliente);
        clienteRepository.save(cliente);
    }

    public List<ClienteModel> listar() {
        List<ClienteModel> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            return Collections.emptyList();
        }
        return clientes;
    }

    public Optional<ClienteModel> buscarPorCnpj(String cnpj) {
        return clienteRepository.findByCnpj(cnpj);
    }

    public Optional<ClienteModel> buscarPorId(long id) {
        return clienteRepository.findById(id);
    }

    public void atualizarCliente(Long id, ClienteDTO novosDados) {
        ClienteModel clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Cliente não encontrado com id " + id));

        if(novosDados.getNomeEmpresa() != null ){
            clienteExistente.setNomeEmpresa(novosDados.getNomeEmpresa());
        }
        if(novosDados.getCnpj() != null ){
            clienteExistente.setCnpj(novosDados.getCnpj());
        }

        clienteRepository.save(clienteExistente);
    }

    public List<ClienteModel> listarPorNome(String termo) {
        List<ClienteModel> clientes = clienteRepository.buscarPorNomeOuCnpj(termo);
        return clientes.isEmpty() ? Collections.emptyList() : clientes;
    }

    public void deletarCliente(Long id) {
        ClienteModel cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Cliente não encontrado com o id" + id));
        clienteRepository.delete(cliente);
    }
}
