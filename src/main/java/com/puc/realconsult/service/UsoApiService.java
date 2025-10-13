package com.puc.realconsult.service;


import com.puc.realconsult.exception.ResourceNotFound;
import com.puc.realconsult.model.ClienteModel;
import com.puc.realconsult.model.RequisicoesApiModel;
import com.puc.realconsult.model.UsuarioApiModel;
import com.puc.realconsult.repository.ClienteRepository;
import com.puc.realconsult.repository.RequisicoesApiRepository;
import com.puc.realconsult.repository.UsuarioApiRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsoApiService {
    private final UsuarioApiRepository usuarioApiRepository;
    private final ClienteRepository clienteRepository;
    private final RequisicoesApiRepository requisicoesApiRepository;


    // TODO busca todas as requisições feitas por um cliente
    public List<RequisicoesApiModel> buscarUsoPorCliente(Long idCliente){
        // Para  ligar o login da requisição da api com o id do cliente
        UsuarioApiModel usuarioApiModel = usuarioApiRepository.findByIdCliente(idCliente);
        // Pega o login do cliente
        String loginCliente = usuarioApiModel.getLogin();
        // Retorna a lista de requisições feitas pelo cliente
        return requisicoesApiRepository.findByLogin(loginCliente);
    }

    // TODO busca todas as requisições feitas a partir do login
    public ClienteModel buscarClientePorLogin(String login){
        UsuarioApiModel usuarioApiModel = usuarioApiRepository.findByLogin(login);

        ClienteModel cliente = clienteRepository.findById(usuarioApiModel.getIdCliente().getIdCliente()).orElse(null);

        if(cliente == null){
            throw new ResourceNotFound("Cliente não encontrado");
        }

        return cliente;
    }

    public int totalConsulRealizada(Long idCliente){
        ClienteModel cliente = clienteRepository.findById(idCliente).orElse(null);
        if(cliente == null){
            throw new ResourceNotFound("Cliente não encontrado");
        }
        return cliente.getNumeroConsultasRealizadas();
    }

    public int totConsulSaldo(Long idCliente){
        ClienteModel cliente = clienteRepository.findById(idCliente).orElse(null);
        if(cliente == null){
            throw new ResourceNotFound("Cliente não encontrado");
        }
        return cliente.getNumeroConsultas() - cliente.getNumeroConsultasRealizadas();
    }

    public List<RequisicoesApiModel> totConsulRealizadaPorData(
            Long idCliente, LocalDateTime dataInicio, LocalDateTime dataFim) {

        return buscarUsoPorCliente(idCliente).stream()
                .filter(req -> {
                    if (req.getBegin() == null) return false;
                    // Converte o Timestamp do banco para LocalDateTime
                    LocalDateTime dataRequisicao = req.getBegin().toLocalDateTime();
                    return ( !dataRequisicao.isBefore(dataInicio) ) &&
                            ( !dataRequisicao.isAfter(dataFim) );
                })
                .toList();
    }
    public List<RequisicoesApiModel> totConsulRealiUf(String uf){
        List<RequisicoesApiModel> lista = requisicoesApiRepository.findByUf(uf);
        if (lista.isEmpty()) return Collections.emptyList();
        return lista;
    }

    public List<RequisicoesApiModel> filtrarRequisicoes(
            String baseMapa,
            String uf,
            String tipoContrato,
            Timestamp inicio,
            Timestamp fim
    ) {
        // Busca todas inicialmente
        List<RequisicoesApiModel> todos = requisicoesApiRepository.findAll();

        return todos.stream()
                .filter(r -> baseMapa == null || r.getLogin() != null && r.getLogin() != null &&
                        r.getLogin().equals(r.getLogin()) &&
                        r.getLogin() != null && r.getLogin() != null) // ajusta conforme relação
                .filter(r -> uf == null || r.getLogin() != null && r.getLogin() != null) // ajustar
                .filter(r -> tipoContrato == null || r.getLogin() != null && r.getLogin() != null) // ajustar
                .filter(r -> inicio == null || !r.getBegin().before(inicio))
                .filter(r -> fim == null || !r.getBegin().after(fim))
                .collect(Collectors.toList());
    }

    public int totalConsultasRealizadasComFiltro(
            Long idCliente,
            String baseMapa,
            String uf,
            String tipoContrato,
            Timestamp inicio,
            Timestamp fim
    ) {
        // Busca o cliente
        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFound("Cliente não encontrado"));

        // Filtra as requisições do cliente
        List<RequisicoesApiModel> filtradas = filtrarRequisicoes(baseMapa, uf, tipoContrato, inicio, fim)
                .stream()
                .filter(r -> r.getLogin().equals(cliente.getIdCliente().toString())) // ajustar se necessário para login
                .toList();

        return filtradas.size(); // retorna quantidade realizadas
    }

    public int totalConsultasSaldoComFiltro(
            Long idCliente,
            String baseMapa,
            String uf,
            String tipoContrato,
            Timestamp inicio,
            Timestamp fim
    ) {
        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFound("Cliente não encontrado"));

        // Consultas realizadas filtradas
        int realizadasFiltradas = totalConsultasRealizadasComFiltro(
                idCliente, baseMapa, uf, tipoContrato, inicio, fim
        );

        // Saldo restante = total contratado - realizadas filtradas
        return cliente.getNumeroConsultas() - realizadasFiltradas;
    }
}
