package com.puc.realconsult.service;

import com.puc.realconsult.model.FuncionarioModel;
import com.puc.realconsult.model.AuditoriaModel;
import com.puc.realconsult.repository.FuncionarioRepository;
import com.puc.realconsult.repository.AuditoriaRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final AuditoriaRepository auditoriaRepository;

    @Transactional(readOnly = true)
    public List<FuncionarioModel> listarPorAuditoria(Long auditoriaId, String busca) {
        if (busca == null || busca.isBlank()) {
            return funcionarioRepository.findByAuditoriaId(auditoriaId);
        }
        return funcionarioRepository.findByAuditoriaIdAndNomeFuncionarioContainingIgnoreCase(auditoriaId, busca.trim());
    }

    @Transactional
    public DadosImportacao importarDados(InputStream is, Long auditoriaId) throws IOException {

        AuditoriaModel auditoria = auditoriaRepository.findById(auditoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Auditoria não encontrada"));

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        List<FuncionarioModel> funcionarios = new ArrayList<>();

        int funcionariosAcrescentados = 0;
        int funcionariosSubstituidos = 0;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            boolean linhaVazia = true;
            for (Cell cell : row) {
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    linhaVazia = false;
                    break;
                }
            }
            if (linhaVazia) continue;
            if (row.getCell(0) == null) continue;

            FuncionarioModel funcionario = new FuncionarioModel();
            funcionario.setNomeFuncionario(row.getCell(0).getStringCellValue());
            funcionario.setMatricula((long) row.getCell(1).getNumericCellValue());
            funcionario.setUnidade(row.getCell(2).getStringCellValue());
            funcionario.setSituacao(row.getCell(3).getStringCellValue());
            funcionario.setCustoAtual(row.getCell(4).getNumericCellValue());
            funcionario.setCustoProposto(row.getCell(5).getNumericCellValue());
            funcionario.setEconomiaValor(row.getCell(6) != null ? row.getCell(6).getNumericCellValue() : 0);
            funcionario.setEconomiaPercentual(row.getCell(7).getNumericCellValue());
            funcionario.setTipoDia(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : " - ");
            funcionario.setDiasMes(row.getCell(9) != null ? (long) row.getCell(9).getNumericCellValue() : 0);
            funcionario.setValorDiario(row.getCell(10) != null ? row.getCell(10).getNumericCellValue() : 0);
            funcionario.setValorCustoImplantado(row.getCell(11) != null ? row.getCell(11).getNumericCellValue() : 0);
            funcionario.setValorEconomiaImplantada(row.getCell(12) != null ? row.getCell(12).getNumericCellValue() : 0);
            funcionario.setTarifaLinhaUmIda(row.getCell(13) != null ? row.getCell(13).getNumericCellValue() : 0);
            funcionario.setTarifaLinhaDoisIda(row.getCell(14) != null ? row.getCell(14).getNumericCellValue() : 0);
            funcionario.setTarifaLinhaTresIda(row.getCell(15) != null ? row.getCell(15).getNumericCellValue() : 0);
            funcionario.setTarifaLinhaQuatroIda(row.getCell(16) != null ? row.getCell(16).getNumericCellValue() : 0);
            funcionario.setTarifaLinhaUmVolta(row.getCell(17) != null ? row.getCell(17).getNumericCellValue() : 0);
            funcionario.setTarifaLinhaDoisVolta(row.getCell(18) != null ? row.getCell(18).getNumericCellValue() : 0);
            funcionario.setTarifaLinhaTresVolta(row.getCell(19) != null ? row.getCell(19).getNumericCellValue() : 0);
            funcionario.setTarifaLinhaQuatroVolta(row.getCell(20) != null ? row.getCell(20).getNumericCellValue() : 0);
            funcionario.setOperadoraIda(String.valueOf(row.getCell(21) != null ? row.getCell(21).getNumericCellValue() : 0));
            funcionario.setOperadoraVolta(String.valueOf(row.getCell(22) != null ? row.getCell(22).getNumericCellValue() : 0));

            FuncionarioModel funcionarioExistente = funcionarioRepository.findByAuditoriaIdAndMatricula(auditoriaId, funcionario.getMatricula());

            if (funcionarioExistente != null) {
                // Atualizar todos os dados do funcionário existente
                funcionarioExistente.setNomeFuncionario(funcionario.getNomeFuncionario());
                funcionarioExistente.setUnidade(funcionario.getUnidade());
                funcionarioExistente.setSituacao(funcionario.getSituacao());
                funcionarioExistente.setCustoAtual(funcionario.getCustoAtual());
                funcionarioExistente.setCustoProposto(funcionario.getCustoProposto());
                funcionarioExistente.setEconomiaValor(funcionario.getEconomiaValor());
                funcionarioExistente.setEconomiaPercentual(funcionario.getEconomiaPercentual());
                funcionarioExistente.setTipoDia(funcionario.getTipoDia());
                funcionarioExistente.setDiasMes(funcionario.getDiasMes());
                funcionarioExistente.setValorDiario(funcionario.getValorDiario());
                funcionarioExistente.setValorCustoImplantado(funcionario.getValorCustoImplantado());
                funcionarioExistente.setValorEconomiaImplantada(funcionario.getValorEconomiaImplantada());
                funcionarioExistente.setTarifaLinhaUmIda(funcionario.getTarifaLinhaUmIda());
                funcionarioExistente.setTarifaLinhaDoisIda(funcionario.getTarifaLinhaDoisIda());
                funcionarioExistente.setTarifaLinhaTresIda(funcionario.getTarifaLinhaTresIda());
                funcionarioExistente.setTarifaLinhaQuatroIda(funcionario.getTarifaLinhaQuatroIda());
                funcionarioExistente.setTarifaLinhaUmVolta(funcionario.getTarifaLinhaUmVolta());
                funcionarioExistente.setTarifaLinhaDoisVolta(funcionario.getTarifaLinhaDoisVolta());
                funcionarioExistente.setTarifaLinhaTresVolta(funcionario.getTarifaLinhaTresVolta());
                funcionarioExistente.setTarifaLinhaQuatroVolta(funcionario.getTarifaLinhaQuatroVolta());
                funcionarioExistente.setOperadoraIda(funcionario.getOperadoraIda());
                funcionarioExistente.setOperadoraVolta(funcionario.getOperadoraVolta());

                funcionarioRepository.save(funcionarioExistente);
                funcionariosSubstituidos++;
            } else {
                funcionario.setAuditoria(auditoria);
                funcionarios.add(funcionario);
                funcionariosAcrescentados++;
            }
        }

        funcionarioRepository.saveAll(funcionarios);
        workbook.close();

        return new DadosImportacao(funcionariosAcrescentados, funcionariosSubstituidos);
    }

    public List<FuncionarioModel> listarTodosFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public static class DadosImportacao {
        private int funcionariosAcrescentados;
        private int funcionariosSubstituidos;

        public DadosImportacao(int funcionariosAcrescentados, int funcionariosSubstituidos) {
            this.funcionariosAcrescentados = funcionariosAcrescentados;
            this.funcionariosSubstituidos = funcionariosSubstituidos;
        }

        public int getFuncionariosAcrescentados() {
            return funcionariosAcrescentados;
        }

        public int getFuncionariosSubstituidos() {
            return funcionariosSubstituidos;
        }
    }
}
