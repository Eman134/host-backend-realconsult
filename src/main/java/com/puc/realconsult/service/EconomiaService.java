package com.puc.realconsult.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.puc.realconsult.dto.EconomiaDTO;
import com.puc.realconsult.dto.FuncionarioEconomiaDTO;
import com.puc.realconsult.model.AuditoriaModel;
import com.puc.realconsult.model.ClienteModel;
import com.puc.realconsult.model.FuncionarioModel;
import com.puc.realconsult.repository.AuditoriaRepository;
import com.puc.realconsult.repository.FuncionarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EconomiaService {

    private final AuditoriaRepository auditoriaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public EconomiaDTO calcularEconomiaPorAuditoria(Long auditoriaId) {
        return calcularEconomiaPorAuditoria(auditoriaId, null, null);
    }

    /**
     * Calcula economia diretamente com uma lista de funcionários (útil para dados antigos sem mês/ano)
     */
    private EconomiaDTO calcularEconomiaComFuncionarios(List<FuncionarioModel> funcionarios, Long auditoriaId, Integer mesReferencia, Integer anoReferencia) {
        AuditoriaModel auditoria = auditoriaRepository.findById(auditoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Auditoria não encontrada"));

        System.out.println("DEBUG EconomiaService.calcularEconomiaComFuncionarios - Auditoria " + auditoriaId + 
                ", Mês: " + mesReferencia + ", Ano: " + anoReferencia + ", Funcionários: " + funcionarios.size());

        // Calcular totais em uma única passada (otimização)
        double custoAtualTotal = 0.0;
        double custoPropostoTotal = 0.0;
        double custoImplantadoTotal = 0.0;
        
        // Mapear funcionários e calcular totais simultaneamente (mais eficiente)
        List<FuncionarioEconomiaDTO> funcionariosDTO = new java.util.ArrayList<>();
        
        for (FuncionarioModel f : funcionarios) {
            double custoAtual = f.getCustoAtual() != null ? f.getCustoAtual() : 0.0;
            double custoProposto = f.getCustoProposto() != null ? f.getCustoProposto() : 0.0;
            double custoImplantado = f.getValorCustoImplantado() != null ? f.getValorCustoImplantado() : 
                (f.getCustoProposto() != null ? f.getCustoProposto() : 0.0);
            
            custoAtualTotal += custoAtual;
            custoPropostoTotal += custoProposto;
            custoImplantadoTotal += custoImplantado;

            funcionariosDTO.add(new FuncionarioEconomiaDTO(
                f.getFuncionarioId(),
                f.getNomeFuncionario(),
                custoAtual,
                custoProposto,
                custoImplantado
            ));
        }

        // Calcular economias
        double economiaPrevista = custoAtualTotal - custoPropostoTotal;
        double economiaReal = custoAtualTotal - custoImplantadoTotal;

        // Se temos dados mensais específicos, o valor já é mensal (não dividir)
        double economiaPrevistaMensal = economiaPrevista;
        double economiaRealMensal = economiaReal;

        // Diferença percentual
        double diferenca = economiaPrevista > 0 
            ? ((economiaReal - economiaPrevista) / economiaPrevista) * 100.0
            : 0.0;

        // Total acumulado anual
        double totalAcumulado = economiaReal;

        System.out.println("DEBUG EconomiaService.calcularEconomiaComFuncionarios - Resultado:");
        System.out.println("  - Economia Prevista: " + economiaPrevista);
        System.out.println("  - Economia Real: " + economiaReal);
        System.out.println("  - Economia Prevista Mensal: " + economiaPrevistaMensal);
        System.out.println("  - Economia Real Mensal: " + economiaRealMensal);
        System.out.println("  - Total de funcionários usados: " + funcionarios.size());

        return new EconomiaDTO(
            auditoria.getCliente() != null ? auditoria.getCliente().getIdCliente() : null,
            auditoria.getCliente() != null ? auditoria.getCliente().getNomeEmpresa() : null,
            auditoria.getId(),
            auditoria.getNome(),
            economiaPrevista,
            economiaReal,
            economiaPrevistaMensal,
            economiaRealMensal,
            diferenca,
            totalAcumulado,
            funcionariosDTO,
            mesReferencia,
            anoReferencia
        );
    }

    public EconomiaDTO calcularEconomiaPorAuditoria(Long auditoriaId, Integer mesReferencia, Integer anoReferencia) {
        AuditoriaModel auditoria = auditoriaRepository.findById(auditoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Auditoria não encontrada"));

        System.out.println("DEBUG EconomiaService.calcularEconomiaPorAuditoria - Auditoria " + auditoriaId + 
                ", Mês: " + mesReferencia + ", Ano: " + anoReferencia);

        // Buscar funcionários do mês específico ou todos (se mês/ano não fornecido)
        List<FuncionarioModel> funcionarios;
        if (mesReferencia != null && anoReferencia != null) {
            funcionarios = funcionarioRepository.findByAuditoriaIdAndMesReferenciaAndAnoReferencia(
                auditoriaId, mesReferencia, anoReferencia);
            System.out.println("DEBUG EconomiaService - Buscou funcionários específicos do mês: " + funcionarios.size());
        } else {
            // Se não tem mês/ano, buscar todos (compatibilidade com dados antigos)
            funcionarios = funcionarioRepository.findByAuditoriaId(auditoriaId);
            System.out.println("DEBUG EconomiaService - Buscou TODOS os funcionários da auditoria (sem filtro mensal): " + funcionarios.size());
        }

        // Calcular totais em uma única passada (otimização)
        double custoAtualTotal = 0.0;
        double custoPropostoTotal = 0.0;
        double custoImplantadoTotal = 0.0;
        
        // Mapear funcionários e calcular totais simultaneamente (mais eficiente)
        List<FuncionarioEconomiaDTO> funcionariosDTO = new java.util.ArrayList<>();
        
        for (FuncionarioModel f : funcionarios) {
            double custoAtual = f.getCustoAtual() != null ? f.getCustoAtual() : 0.0;
            double custoProposto = f.getCustoProposto() != null ? f.getCustoProposto() : 0.0;
            double custoImplantado = f.getValorCustoImplantado() != null ? f.getValorCustoImplantado() : 
                (f.getCustoProposto() != null ? f.getCustoProposto() : 0.0);
            
            custoAtualTotal += custoAtual;
            custoPropostoTotal += custoProposto;
            custoImplantadoTotal += custoImplantado;
            
            funcionariosDTO.add(new FuncionarioEconomiaDTO(
                f.getFuncionarioId(),
                f.getNomeFuncionario(),
                custoAtual,
                custoProposto,
                custoImplantado
            ));
        }

        // Calcular economias
        double economiaPrevista = custoAtualTotal - custoPropostoTotal;
        double economiaReal = custoAtualTotal - custoImplantadoTotal;
        
        // Se temos dados mensais específicos, o valor já é mensal (não dividir)
        // Se não temos mês/ano, assumir que é anual e dividir por 12
        double economiaPrevistaMensal;
        double economiaRealMensal;
        
        if (mesReferencia != null && anoReferencia != null) {
            // Dados mensais: valor já é do mês, não precisa dividir
            economiaPrevistaMensal = economiaPrevista;
            economiaRealMensal = economiaReal;
        } else {
            // Dados anuais (compatibilidade): dividir por 12 meses
            economiaPrevistaMensal = economiaPrevista / 12.0;
            economiaRealMensal = economiaReal / 12.0;
        }
        
        // Diferença percentual
        double diferenca = economiaPrevista > 0 
            ? ((economiaReal - economiaPrevista) / economiaPrevista) * 100.0
            : 0.0;

        // Total acumulado anual
        double totalAcumulado = economiaReal;

        System.out.println("DEBUG EconomiaService - Resultado para Auditoria " + auditoriaId + 
                " (Mês: " + mesReferencia + ", Ano: " + anoReferencia + "):");
        System.out.println("  - Economia Prevista: " + economiaPrevista);
        System.out.println("  - Economia Real: " + economiaReal);
        System.out.println("  - Economia Prevista Mensal: " + economiaPrevistaMensal);
        System.out.println("  - Economia Real Mensal: " + economiaRealMensal);
        System.out.println("  - Total de funcionários usados: " + funcionarios.size());

        return new EconomiaDTO(
            auditoria.getCliente() != null ? auditoria.getCliente().getIdCliente() : null,
            auditoria.getCliente() != null ? auditoria.getCliente().getNomeEmpresa() : null,
            auditoria.getId(),
            auditoria.getNome(),
            economiaPrevista,
            economiaReal,
            economiaPrevistaMensal,
            economiaRealMensal,
            diferenca,
            totalAcumulado,
            funcionariosDTO,
            mesReferencia != null ? mesReferencia : (funcionarios.isEmpty() ? null : funcionarios.get(0).getMesReferencia()),
            anoReferencia != null ? anoReferencia : (funcionarios.isEmpty() ? null : funcionarios.get(0).getAnoReferencia())
        );
    }

    public List<EconomiaDTO> calcularEconomiaPorCliente(Long clienteId) {
        List<AuditoriaModel> auditorias = auditoriaRepository.findByClienteIdCliente(clienteId);
        return auditorias.stream()
                .map(a -> calcularEconomiaPorAuditoria(a.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Calcula economia agregada por cliente (soma de todas as auditorias do cliente)
     */
    public EconomiaDTO calcularEconomiaAgregadaPorCliente(Long clienteId, Integer mesReferencia, Integer anoReferencia) {
        List<AuditoriaModel> auditorias = auditoriaRepository.findByClienteIdCliente(clienteId);
        
        if (auditorias.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado ou não possui auditorias");
        }
        
        // Buscar o cliente para obter dados
        ClienteModel cliente = auditorias.get(0).getCliente();
        
        // Buscar todos os funcionários de todas as auditorias deste cliente
        List<FuncionarioModel> todosFuncionarios = new java.util.ArrayList<>();
        for (AuditoriaModel auditoria : auditorias) {
            List<FuncionarioModel> funcionarios;
            if (mesReferencia != null && anoReferencia != null) {
                funcionarios = funcionarioRepository.findByAuditoriaIdAndMesReferenciaAndAnoReferencia(
                    auditoria.getId(), mesReferencia, anoReferencia);
            } else {
                funcionarios = funcionarioRepository.findByAuditoriaId(auditoria.getId());
            }
            todosFuncionarios.addAll(funcionarios);
        }
        
        // Calcular totais
        double custoAtualTotal = 0.0;
        double custoPropostoTotal = 0.0;
        double custoImplantadoTotal = 0.0;
        
        List<FuncionarioEconomiaDTO> funcionariosDTO = new java.util.ArrayList<>();
        
        for (FuncionarioModel f : todosFuncionarios) {
            double custoAtual = f.getCustoAtual() != null ? f.getCustoAtual() : 0.0;
            double custoProposto = f.getCustoProposto() != null ? f.getCustoProposto() : 0.0;
            double custoImplantado = f.getValorCustoImplantado() != null ? f.getValorCustoImplantado() : 
                (f.getCustoProposto() != null ? f.getCustoProposto() : 0.0);
            
            custoAtualTotal += custoAtual;
            custoPropostoTotal += custoProposto;
            custoImplantadoTotal += custoImplantado;
            
            funcionariosDTO.add(new FuncionarioEconomiaDTO(
                f.getFuncionarioId(),
                f.getNomeFuncionario(),
                custoAtual,
                custoProposto,
                custoImplantado
            ));
        }
        
        // Calcular economias
        double economiaPrevista = custoAtualTotal - custoPropostoTotal;
        double economiaReal = custoAtualTotal - custoImplantadoTotal;
        
        // Calcular mensal
        double economiaPrevistaMensal;
        double economiaRealMensal;
        
        if (mesReferencia != null && anoReferencia != null) {
            economiaPrevistaMensal = economiaPrevista;
            economiaRealMensal = economiaReal;
        } else {
            economiaPrevistaMensal = economiaPrevista / 12.0;
            economiaRealMensal = economiaReal / 12.0;
        }
        
        // Diferença percentual
        double diferenca = economiaPrevista > 0 
            ? ((economiaReal - economiaPrevista) / economiaPrevista) * 100.0
            : 0.0;
        
        // Total acumulado
        double totalAcumulado = economiaReal;
        
        return new EconomiaDTO(
            cliente.getIdCliente(),
            cliente.getNomeEmpresa(),
            null, // Sem ID de auditoria específica (agregado)
            cliente.getNomeEmpresa(), // Nome da auditoria = nome da empresa
            economiaPrevista,
            economiaReal,
            economiaPrevistaMensal,
            economiaRealMensal,
            diferenca,
            totalAcumulado,
            funcionariosDTO,
            mesReferencia,
            anoReferencia
        );
    }

    public List<EconomiaDTO> listarTodasEconomias() {
        List<AuditoriaModel> auditorias = auditoriaRepository.findAll();
        return auditorias.stream()
                .map(a -> calcularEconomiaPorAuditoria(a.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Calcula economia mensal para todos os meses disponíveis de uma auditoria
     */
    public List<EconomiaDTO> calcularEconomiaMensalPorAuditoria(Long auditoriaId, Integer ano) {
        // Verificar se auditoria existe
        auditoriaRepository.findById(auditoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Auditoria não encontrada"));

        // Buscar todos os funcionários da auditoria
        List<FuncionarioModel> todosFuncionarios = funcionarioRepository.findByAuditoriaId(auditoriaId);
        
        // Debug: contar funcionários com e sem mês/ano
        long comMesAno = todosFuncionarios.stream()
                .filter(f -> f.getMesReferencia() != null && f.getAnoReferencia() != null)
                .count();
        long semMesAno = todosFuncionarios.size() - comMesAno;
        
        System.out.println("DEBUG EconomiaService - Auditoria " + auditoriaId + 
                ": Total funcionários=" + todosFuncionarios.size() + 
                ", Com mês/ano=" + comMesAno + 
                ", Sem mês/ano=" + semMesAno);
        
        // Agrupar por mês/ano
        java.util.Map<String, List<FuncionarioModel>> funcionariosPorMesAno = todosFuncionarios.stream()
                .filter(f -> f.getMesReferencia() != null && f.getAnoReferencia() != null)
                .filter(f -> ano == null || f.getAnoReferencia().equals(ano))
                .collect(Collectors.groupingBy(f -> 
                    f.getAnoReferencia() + "-" + String.format("%02d", f.getMesReferencia())));

        System.out.println("DEBUG EconomiaService - Meses encontrados: " + funcionariosPorMesAno.keySet());
        System.out.println("DEBUG EconomiaService - Quantidade de grupos (meses distintos): " + funcionariosPorMesAno.size());
        
        // Detalhar cada grupo encontrado
        for (java.util.Map.Entry<String, List<FuncionarioModel>> entry : funcionariosPorMesAno.entrySet()) {
            System.out.println("DEBUG EconomiaService - Grupo " + entry.getKey() + ": " + entry.getValue().size() + " funcionários");
        }

        // Se não tem dados mensais, mas tem funcionários sem mês/ano, criar um grupo usando mês/ano da auditoria ou data atual
        if (funcionariosPorMesAno.isEmpty() && semMesAno > 0) {
            System.out.println("DEBUG EconomiaService - Nenhum dado mensal encontrado, mas há " + semMesAno + " funcionários sem mês/ano.");
            System.out.println("DEBUG EconomiaService - Tentando usar mês/ano da auditoria ou data atual.");
            
            // Buscar a auditoria para pegar mês/ano dela
            AuditoriaModel auditoria = auditoriaRepository.findById(auditoriaId)
                    .orElse(null);
            
            Integer mesParaUsar = null;
            Integer anoParaUsar = null;
            
            if (auditoria != null && auditoria.getMesReferencia() != null && auditoria.getAnoReferencia() != null) {
                mesParaUsar = auditoria.getMesReferencia();
                anoParaUsar = auditoria.getAnoReferencia();
                System.out.println("DEBUG EconomiaService - Usando mês/ano da auditoria: " + mesParaUsar + "/" + anoParaUsar);
            } else {
                // Usar data atual
                java.time.LocalDate hoje = java.time.LocalDate.now();
                mesParaUsar = hoje.getMonthValue();
                anoParaUsar = hoje.getYear();
                System.out.println("DEBUG EconomiaService - Usando data atual: " + mesParaUsar + "/" + anoParaUsar);
            }
            
            // Se o filtro de ano foi passado, verificar se bate
            if (ano != null && !ano.equals(anoParaUsar)) {
                System.out.println("DEBUG EconomiaService - Filtro de ano (" + ano + ") não corresponde ao ano dos dados (" + anoParaUsar + "). Retornando lista vazia.");
                return List.of();
            }
            
            // Agrupar todos os funcionários sem mês/ano como um único mês
            List<FuncionarioModel> funcionariosSemMesAno = todosFuncionarios.stream()
                    .filter(f -> f.getMesReferencia() == null || f.getAnoReferencia() == null)
                    .collect(Collectors.toList());
            
            if (!funcionariosSemMesAno.isEmpty()) {
                String chave = anoParaUsar + "-" + String.format("%02d", mesParaUsar);
                funcionariosPorMesAno.put(chave, funcionariosSemMesAno);
                System.out.println("DEBUG EconomiaService - Criado grupo " + chave + " com " + funcionariosSemMesAno.size() + " funcionários (dados antigos sem mês/ano)");
            }
        }
        
        // Se ainda está vazio, retorna lista vazia
        if (funcionariosPorMesAno.isEmpty()) {
            System.out.println("DEBUG EconomiaService - Nenhum dado mensal encontrado. Retornando lista vazia.");
            System.out.println("DEBUG EconomiaService - Possíveis causas:");
            System.out.println("  1. Funcionários não têm mesReferencia e/ou anoReferencia preenchidos");
            System.out.println("  2. Filtro de ano está eliminando todos os dados (ano solicitado: " + ano + ")");
            System.out.println("  3. Auditoria não tem funcionários associados");
            return List.of();
        }

        // Calcular economia para cada mês
        List<EconomiaDTO> economiasMensais = new java.util.ArrayList<>();
        for (java.util.Map.Entry<String, List<FuncionarioModel>> entry : funcionariosPorMesAno.entrySet()) {
            List<FuncionarioModel> funcionarios = entry.getValue();
            
            if (funcionarios.isEmpty()) {
                System.out.println("DEBUG EconomiaService - Grupo " + entry.getKey() + " está vazio, pulando...");
                continue;
            }
            
            // Pegar mês/ano do primeiro funcionário OU extrair da chave se funcionários não têm
            Integer mesRef = funcionarios.get(0).getMesReferencia();
            Integer anoRef = funcionarios.get(0).getAnoReferencia();
            
            // Se não tem mês/ano nos funcionários, extrair da chave do grupo (formato: "2025-11")
            if (mesRef == null || anoRef == null) {
                String[] partes = entry.getKey().split("-");
                if (partes.length >= 2) {
                    try {
                        anoRef = Integer.parseInt(partes[0]);
                        mesRef = Integer.parseInt(partes[1]);
                        System.out.println("DEBUG EconomiaService - Extraído mês/ano da chave: " + mesRef + "/" + anoRef);
                    } catch (NumberFormatException e) {
                        System.err.println("ERROR EconomiaService - Não foi possível extrair mês/ano da chave: " + entry.getKey());
                        continue;
                    }
                } else {
                    System.err.println("ERROR EconomiaService - Chave inválida: " + entry.getKey());
                    continue;
                }
            }
            
            try {
                System.out.println("DEBUG EconomiaService - Calculando economia para mês " + mesRef + "/" + anoRef + " (grupo: " + entry.getKey() + ", " + funcionarios.size() + " funcionários)");
                
                // Se os funcionários não têm mês/ano (dados antigos), calcular diretamente com a lista fornecida
                boolean funcionariosSemMesAno = funcionarios.stream()
                        .anyMatch(f -> f.getMesReferencia() == null || f.getAnoReferencia() == null);
                
                EconomiaDTO economia;
                if (funcionariosSemMesAno) {
                    // Calcular economia diretamente com os funcionários fornecidos
                    System.out.println("DEBUG EconomiaService - Funcionários sem mês/ano, calculando diretamente...");
                    economia = calcularEconomiaComFuncionarios(funcionarios, auditoriaId, mesRef, anoRef);
                } else {
                    // Usar método normal que busca funcionários por mês/ano
                    economia = calcularEconomiaPorAuditoria(auditoriaId, mesRef, anoRef);
                }
                
                economiasMensais.add(economia);
                System.out.println("DEBUG EconomiaService - Economia calculada com sucesso para " + mesRef + "/" + anoRef);
            } catch (Exception e) {
                System.err.println("ERROR EconomiaService - Erro ao calcular economia para mês " + mesRef + "/" + anoRef);
                System.err.println("  - Exceção: " + e.getClass().getName());
                System.err.println("  - Mensagem: " + e.getMessage());
                e.printStackTrace();
                // Continuar processando outros meses mesmo se este falhar
            }
        }
        
        System.out.println("DEBUG EconomiaService - Total de meses calculados com sucesso: " + economiasMensais.size());

        // Ordenar por ano e mês
        economiasMensais.sort((a, b) -> {
            // Ordenar por ano primeiro, depois por mês
            if (a.anoReferencia() == null && b.anoReferencia() == null) return 0;
            if (a.anoReferencia() == null) return 1;
            if (b.anoReferencia() == null) return -1;
            
            int cmpAno = a.anoReferencia().compareTo(b.anoReferencia());
            if (cmpAno != 0) return cmpAno;
            
            // Mesmo ano, ordenar por mês
            if (a.mesReferencia() == null && b.mesReferencia() == null) return 0;
            if (a.mesReferencia() == null) return 1;
            if (b.mesReferencia() == null) return -1;
            
            return a.mesReferencia().compareTo(b.mesReferencia());
        });

        return economiasMensais;
    }

    /**
     * Calcula economia diretamente com lista de funcionários para um cliente (usado para mensal)
     */
    private EconomiaDTO calcularEconomiaComFuncionariosPorCliente(List<FuncionarioModel> funcionarios, ClienteModel cliente, Integer mesReferencia, Integer anoReferencia) {
        System.out.println("DEBUG EconomiaService.calcularEconomiaComFuncionariosPorCliente - Cliente " + cliente.getIdCliente() + 
                ", Mês: " + mesReferencia + ", Ano: " + anoReferencia + ", Funcionários: " + funcionarios.size());

        // Calcular totais
        double custoAtualTotal = 0.0;
        double custoPropostoTotal = 0.0;
        double custoImplantadoTotal = 0.0;
        
        List<FuncionarioEconomiaDTO> funcionariosDTO = new java.util.ArrayList<>();
        
        for (FuncionarioModel f : funcionarios) {
            double custoAtual = f.getCustoAtual() != null ? f.getCustoAtual() : 0.0;
            double custoProposto = f.getCustoProposto() != null ? f.getCustoProposto() : 0.0;
            double custoImplantado = f.getValorCustoImplantado() != null ? f.getValorCustoImplantado() : 
                (f.getCustoProposto() != null ? f.getCustoProposto() : 0.0);
            
            custoAtualTotal += custoAtual;
            custoPropostoTotal += custoProposto;
            custoImplantadoTotal += custoImplantado;
            
            funcionariosDTO.add(new FuncionarioEconomiaDTO(
                f.getFuncionarioId(),
                f.getNomeFuncionario(),
                custoAtual,
                custoProposto,
                custoImplantado
            ));
        }
        
        // Calcular economias
        double economiaPrevista = custoAtualTotal - custoPropostoTotal;
        double economiaReal = custoAtualTotal - custoImplantadoTotal;
        
        // Dados mensais já são mensais (não dividir)
        double economiaPrevistaMensal = economiaPrevista;
        double economiaRealMensal = economiaReal;
        
        // Diferença percentual
        double diferenca = economiaPrevista > 0 
            ? ((economiaReal - economiaPrevista) / economiaPrevista) * 100.0
            : 0.0;
        
        // Total acumulado
        double totalAcumulado = economiaReal;
        
        return new EconomiaDTO(
            cliente.getIdCliente(),
            cliente.getNomeEmpresa(),
            null, // Sem ID de auditoria específica (agregado)
            cliente.getNomeEmpresa(),
            economiaPrevista,
            economiaReal,
            economiaPrevistaMensal,
            economiaRealMensal,
            diferenca,
            totalAcumulado,
            funcionariosDTO,
            mesReferencia,
            anoReferencia
        );
    }

    /**
     * Calcula economia mensal para todos os meses disponíveis de um cliente (todas as auditorias)
     */
    public List<EconomiaDTO> calcularEconomiaMensalPorCliente(Long clienteId, Integer ano) {
        List<AuditoriaModel> auditorias = auditoriaRepository.findByClienteIdCliente(clienteId);
        
        if (auditorias.isEmpty()) {
            System.out.println("DEBUG EconomiaService - Cliente " + clienteId + " não encontrado ou não possui auditorias");
            return List.of();
        }
        
        ClienteModel cliente = auditorias.get(0).getCliente();
        
        // Buscar todos os funcionários de todas as auditorias do cliente
        List<FuncionarioModel> todosFuncionarios = new java.util.ArrayList<>();
        for (AuditoriaModel auditoria : auditorias) {
            todosFuncionarios.addAll(funcionarioRepository.findByAuditoriaId(auditoria.getId()));
        }
        
        // Debug: contar funcionários com e sem mês/ano
        long comMesAno = todosFuncionarios.stream()
                .filter(f -> f.getMesReferencia() != null && f.getAnoReferencia() != null)
                .count();
        long semMesAno = todosFuncionarios.size() - comMesAno;
        
        System.out.println("DEBUG EconomiaService - Cliente " + clienteId + 
                ": Total funcionários=" + todosFuncionarios.size() + 
                ", Com mês/ano=" + comMesAno + 
                ", Sem mês/ano=" + semMesAno);
        
        // Agrupar por mês/ano
        java.util.Map<String, List<FuncionarioModel>> funcionariosPorMesAno = todosFuncionarios.stream()
                .filter(f -> f.getMesReferencia() != null && f.getAnoReferencia() != null)
                .filter(f -> ano == null || f.getAnoReferencia().equals(ano))
                .collect(Collectors.groupingBy(f -> 
                    f.getAnoReferencia() + "-" + String.format("%02d", f.getMesReferencia())));
        
        System.out.println("DEBUG EconomiaService - Meses encontrados: " + funcionariosPorMesAno.keySet());
        
        // Se não tem dados mensais, mas tem funcionários sem mês/ano
        if (funcionariosPorMesAno.isEmpty() && semMesAno > 0) {
            // Usar data atual
            java.time.LocalDate hoje = java.time.LocalDate.now();
            Integer mesParaUsar = hoje.getMonthValue();
            Integer anoParaUsar = hoje.getYear();
            
            if (ano != null && !ano.equals(anoParaUsar)) {
                return List.of();
            }
            
            List<FuncionarioModel> funcionariosSemMesAno = todosFuncionarios.stream()
                    .filter(f -> f.getMesReferencia() == null || f.getAnoReferencia() == null)
                    .collect(Collectors.toList());
            
            if (!funcionariosSemMesAno.isEmpty()) {
                String chave = anoParaUsar + "-" + String.format("%02d", mesParaUsar);
                funcionariosPorMesAno.put(chave, funcionariosSemMesAno);
            }
        }
        
        if (funcionariosPorMesAno.isEmpty()) {
            return List.of();
        }
        
        // Calcular economia para cada mês usando os funcionários já agrupados
        List<EconomiaDTO> economiasMensais = new java.util.ArrayList<>();
        
        for (java.util.Map.Entry<String, List<FuncionarioModel>> entry : funcionariosPorMesAno.entrySet()) {
            List<FuncionarioModel> funcionarios = entry.getValue();
            
            if (funcionarios.isEmpty()) continue;
            
            Integer mesRef = funcionarios.get(0).getMesReferencia();
            Integer anoRef = funcionarios.get(0).getAnoReferencia();
            
            if (mesRef == null || anoRef == null) {
                String[] partes = entry.getKey().split("-");
                if (partes.length >= 2) {
                    try {
                        anoRef = Integer.parseInt(partes[0]);
                        mesRef = Integer.parseInt(partes[1]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                } else {
                    continue;
                }
            }
            
            try {
                EconomiaDTO economia = calcularEconomiaComFuncionariosPorCliente(funcionarios, cliente, mesRef, anoRef);
                economiasMensais.add(economia);
            } catch (Exception e) {
                System.err.println("ERROR EconomiaService - Erro ao calcular economia para cliente " + clienteId + " mês " + mesRef + "/" + anoRef);
                e.printStackTrace();
            }
        }
        
        // Ordenar por ano e mês
        economiasMensais.sort((a, b) -> {
            if (a.anoReferencia() == null && b.anoReferencia() == null) return 0;
            if (a.anoReferencia() == null) return 1;
            if (b.anoReferencia() == null) return -1;
            
            int cmpAno = a.anoReferencia().compareTo(b.anoReferencia());
            if (cmpAno != 0) return cmpAno;
            
            if (a.mesReferencia() == null && b.mesReferencia() == null) return 0;
            if (a.mesReferencia() == null) return 1;
            if (b.mesReferencia() == null) return -1;
            
            return a.mesReferencia().compareTo(b.mesReferencia());
        });
        
        return economiasMensais;
    }
}

