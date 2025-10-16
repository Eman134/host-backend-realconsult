// TODO essa classe representa a entidade das requisições feitas via api

package com.puc.realconsult.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "requisicoes_api")
public class RequisicoesApiModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_job")
    private Integer idJob;

    @Column(name = "session_id")
    private Integer sessionId;

    @Column(length = 50, nullable = false)
    private String login;

    @Column(nullable = false, columnDefinition = "timestamp default current_timestamp")
    private Timestamp begin;

    @Column(name = "begin_status")
    private Timestamp beginStatus;

    @Column(length = 3, nullable = false)
    private String status;

    @Column(name = "status_retorno", length = 2000)
    private String statusRetorno;

    @Column(name = "ida_volta", nullable = false)
    private Integer idaVolta;

    @Column(name = "hora_entrada")
    private Integer horaEntrada;

    @Column(name = "hora_saida")
    private Integer horaSaida;

    @Column
    private Float raio;

    @Column(name = "tipo_dia", length = 10)
    private String tipoDia;

    @Column(name = "qtde_linhas")
    private Integer qtdeLinhas;

    @Column(name = "origin_address", length = 255)
    private String originAddress;

    @Column(name = "destination_address", length = 255)
    private String destinationAddress;

    @Column(name = "enable_walking", nullable = false)
    private Boolean enableWalking;

    @Column(nullable = false)
    private Integer distance;

    @Column(name = "xml_result", length = 50)
    private String xmlResult;

    @Column(name = "time_proc")
    private Double timeProc;

    @Column(name = "cpu_average")
    private Double cpuAverage;

    @Column
    private Double memory;

    @Column(name = "id_batch")
    private Integer idBatch;

    @Column(name = "proposed_value")
    private Double proposedValue;

    @Column(name = "qtde_dias_mes")
    private Integer qtdeDiasMes;

    @Column(name = "vale_dia")
    private Double valeDia;

    @Column(name = "lat_origem")
    private Double latOrigem;

    @Column(name = "long_origem")
    private Double longOrigem;

    @Column(name = "lat_destino")
    private Double latDestino;

    @Column(name = "long_destino")
    private Double longDestino;

    @Column(name = "escolha_ida")
    private Integer escolhaIda;

    @Column(name = "escolha_volta")
    private Integer escolhaVolta;

    @Column(name = "job_info", length = 1000)
    private String jobInfo;

    @Column(name = "informacoes_adicionais", length = 300)
    private String informacoesAdicionais;

    @Column(name = "consulta_valida", nullable = false)
    private Boolean consultaValida;

    @Column(name = "error_msg", length = 300)
    private String errorMsg;

    // ============================
    // Getters e Setters
    // ============================

    public Integer getIdJob() {
        return idJob;
    }

    public void setIdJob(Integer idJob) {
        this.idJob = idJob;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Timestamp getBegin() {
        return begin;
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public Timestamp getBeginStatus() {
        return beginStatus;
    }

    public void setBeginStatus(Timestamp beginStatus) {
        this.beginStatus = beginStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusRetorno() {
        return statusRetorno;
    }

    public void setStatusRetorno(String statusRetorno) {
        this.statusRetorno = statusRetorno;
    }

    public Integer getIdaVolta() {
        return idaVolta;
    }

    public void setIdaVolta(Integer idaVolta) {
        this.idaVolta = idaVolta;
    }

    public Integer getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Integer horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Integer getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(Integer horaSaida) {
        this.horaSaida = horaSaida;
    }

    public Float getRaio() {
        return raio;
    }

    public void setRaio(Float raio) {
        this.raio = raio;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public Integer getQtdeLinhas() {
        return qtdeLinhas;
    }

    public void setQtdeLinhas(Integer qtdeLinhas) {
        this.qtdeLinhas = qtdeLinhas;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Boolean getEnableWalking() {
        return enableWalking;
    }

    public void setEnableWalking(Boolean enableWalking) {
        this.enableWalking = enableWalking;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getXmlResult() {
        return xmlResult;
    }

    public void setXmlResult(String xmlResult) {
        this.xmlResult = xmlResult;
    }

    public Double getTimeProc() {
        return timeProc;
    }

    public void setTimeProc(Double timeProc) {
        this.timeProc = timeProc;
    }

    public Double getCpuAverage() {
        return cpuAverage;
    }

    public void setCpuAverage(Double cpuAverage) {
        this.cpuAverage = cpuAverage;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public Integer getIdBatch() {
        return idBatch;
    }

    public void setIdBatch(Integer idBatch) {
        this.idBatch = idBatch;
    }

    public Double getProposedValue() {
        return proposedValue;
    }

    public void setProposedValue(Double proposedValue) {
        this.proposedValue = proposedValue;
    }

    public Integer getQtdeDiasMes() {
        return qtdeDiasMes;
    }

    public void setQtdeDiasMes(Integer qtdeDiasMes) {
        this.qtdeDiasMes = qtdeDiasMes;
    }

    public Double getValeDia() {
        return valeDia;
    }

    public void setValeDia(Double valeDia) {
        this.valeDia = valeDia;
    }

    public Double getLatOrigem() {
        return latOrigem;
    }

    public void setLatOrigem(Double latOrigem) {
        this.latOrigem = latOrigem;
    }

    public Double getLongOrigem() {
        return longOrigem;
    }

    public void setLongOrigem(Double longOrigem) {
        this.longOrigem = longOrigem;
    }

    public Double getLatDestino() {
        return latDestino;
    }

    public void setLatDestino(Double latDestino) {
        this.latDestino = latDestino;
    }

    public Double getLongDestino() {
        return longDestino;
    }

    public void setLongDestino(Double longDestino) {
        this.longDestino = longDestino;
    }

    public Integer getEscolhaIda() {
        return escolhaIda;
    }

    public void setEscolhaIda(Integer escolhaIda) {
        this.escolhaIda = escolhaIda;
    }

    public Integer getEscolhaVolta() {
        return escolhaVolta;
    }

    public void setEscolhaVolta(Integer escolhaVolta) {
        this.escolhaVolta = escolhaVolta;
    }

    public String getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(String jobInfo) {
        this.jobInfo = jobInfo;
    }

    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public Boolean getConsultaValida() {
        return consultaValida;
    }

    public void setConsultaValida(Boolean consultaValida) {
        this.consultaValida = consultaValida;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
