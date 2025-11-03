package com.puc.realconsult.service;


import com.puc.realconsult.exception.ResourceNotFound;
import com.puc.realconsult.exception.idNotFound;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    public UsuarioApiModel buscarUsuarioPorIdCliente(Long idCliente){
        UsuarioApiModel usuarioApiModel = usuarioApiRepository.findByIdCliente(idCliente);
        if(usuarioApiModel == null){
            throw new ResourceNotFound("Cliente não encontrado");
        }
        return usuarioApiModel;
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
            Timestamp inicio,
            Timestamp fim
    ) {
        // Busca todas inicialmente
        List<RequisicoesApiModel> todos = requisicoesApiRepository.findAll();


        // Começa o stream
        return todos.stream()
                // Filtro por intervalo de data
                .filter(r -> {
                    if (inicio == null && fim == null) return true;
                    if (r.getBegin() == null) return false;
                    return (inicio == null || !r.getBegin().before(inicio))
                            && (fim == null || !r.getBegin().after(fim));
                })
                .collect(Collectors.toList());
    }

    public int totalConsultasRealizadasComFiltro(
            Long idCliente,
            Timestamp inicio,
            Timestamp fim
    ) {

        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFound("Cliente não encontrado"));
        if(!usuarioApiRepository.existsByIdCliente(cliente) ){
            throw new idNotFound("Não existe Login para esse cliente");
        }

        List<RequisicoesApiModel> filtradas = filtrarRequisicoes(inicio, fim);
        List<RequisicoesApiModel> novaLista = new ArrayList<>();

        for (RequisicoesApiModel requisicoesApiModel : filtradas) {
            if (requisicoesApiModel.getLogin().equals(usuarioApiRepository.findByIdCliente(idCliente).getLogin())) {
                novaLista.add(requisicoesApiModel);
            }
        }
        return novaLista.size();
    }

    public int totalConsultasSaldoComFiltro(Long idCliente, Timestamp inicio, Timestamp fim) {
        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFound("Cliente não encontrado"));

        String login = buscarUsuarioPorIdCliente(idCliente).getLogin();
        int realizadasFiltradas;

        if (inicio != null && fim != null) {
            // ✅ Busca apenas dentro do período informado
            realizadasFiltradas = requisicoesApiRepository.buscarReqPeriodoComLogin(inicio, fim, login);
        } else {
            // ✅ Sem filtro → busca total de todas as requisições
            realizadasFiltradas = requisicoesApiRepository.buscarTodasReqPorLogin(login);
        }

        return cliente.getNumeroConsultas() - realizadasFiltradas;
    }

    public List<RequisicoesApiModel> ConsultasRealizadasComPeriodo(Long idCliente, Timestamp inicio, Timestamp fim) {
        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFound("Cliente não encontrado"));

        String login = buscarUsuarioPorIdCliente(idCliente).getLogin();
        List<RequisicoesApiModel> realizadas;

        if (inicio != null && fim != null) {
            // ✅ Busca apenas dentro do período informado
            realizadas = requisicoesApiRepository.buscarReqPeriodoComTodosOsCampos(inicio, fim, login);
        } else {
            // ✅ Sem filtro → busca total de todas as requisições
            realizadas = requisicoesApiRepository.buscarReqComTodosOsCampos(login);
        }

        return realizadas;
    }

}
