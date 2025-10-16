package com.puc.realconsult.controller;


import com.puc.realconsult.model.ClienteModel;
import com.puc.realconsult.model.RequisicoesApiModel;
import com.puc.realconsult.model.UsuarioApiModel;
import com.puc.realconsult.repository.RequisicoesApiRepository;
import com.puc.realconsult.service.ClienteService;
import com.puc.realconsult.service.UsoApiService;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usoapi")
@AllArgsConstructor
public class UsoApiClienteController {

    private final UsoApiService usoApiService;
    private final ClienteService clienteService;
    private final RequisicoesApiRepository requisicoesApiRepository;


    @GetMapping("/cliente/{idCliente}/realizadas/periodo")
    public ResponseEntity<Integer> listarPorPeriodos(
            @PathVariable("idCliente") Long idCliente,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        Integer total = requisicoesApiRepository.buscarReqPeriodoComLogin(
                Timestamp.valueOf(inicio),
                Timestamp.valueOf(fim),
                usoApiService.buscarUsuarioPorIdCliente(idCliente).getLogin()
        );

        return ResponseEntity.ok(total);
    }

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
                idCliente, inicioTS, fimTS
        );

        return ResponseEntity.ok(total);
    }

    // 🔹 Retorna total de consultas restantes filtradas
    @GetMapping("/cliente/{idCliente}/saldo/periodo")
    public ResponseEntity<Integer> totalSaldoPeriodo(
            @PathVariable("idCliente") Long idCliente,
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {


        int saldo = usoApiService.totalConsultasSaldoComFiltro(
                idCliente, Timestamp.valueOf(inicio), Timestamp.valueOf(fim)
        );

        return ResponseEntity.ok(saldo);
    }

    @GetMapping("/cliente/{idCliente}/saldo")
    public ResponseEntity<Integer> totalSaldo(@PathVariable("idCliente") Long idCliente){
        int saldo = usoApiService.totalConsultasSaldoComFiltro(
                idCliente, null, null);
        return ResponseEntity.ok(saldo);
    }

    @GetMapping("/requisicoes/listardata/{ano}/{idCliente}")
    public ResponseEntity<List<Timestamp>> listarRequisicoes(@PathVariable("ano") Integer ano,
                                                             @PathVariable("idCliente") Long idCliente){
        UsuarioApiModel usuarioCliente = usoApiService.buscarUsuarioPorIdCliente(idCliente);
        List<Timestamp> listaRequisicoes = requisicoesApiRepository.buscarTodasReqDatas(ano,
                usuarioCliente.getLogin());

        if(listaRequisicoes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaRequisicoes);
    }

    @GetMapping("/cliente/consultascontratadas/{idCliente}")
    public ResponseEntity<Integer> listarConsultasContratadas(@PathVariable("idCliente") Long idCliente){

        if (idCliente != null){
            return ResponseEntity.ok(clienteService.getConsultasContratadas(idCliente));
        }
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/requisicoes/filtrar")
    public ResponseEntity<List<RequisicoesApiModel>> filtrarRequisicoes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        Timestamp tsInicio = inicio != null ? Timestamp.valueOf(inicio) : null;
        Timestamp tsFim = fim != null ? Timestamp.valueOf(fim) : null;

        List<RequisicoesApiModel> filtradas = usoApiService.filtrarRequisicoes(tsInicio, tsFim);
        return ResponseEntity.ok(filtradas);
    }

}
