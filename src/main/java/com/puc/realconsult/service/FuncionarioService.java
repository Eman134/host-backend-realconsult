package com.puc.realconsult.service;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import java.io.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import com.puc.realconsult.model.FuncionarioModel;
import com.puc.realconsult.repository.FuncionarioRepository;

@Service
@AllArgsConstructor
public class FuncionarioService{

    private final FuncionarioRepository repository;

    public void importarDados(InputStream is) throws IOException{
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        List<FuncionarioModel> funcionarios = new ArrayList<>();
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
                funcionario.setNomeFuncionario(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : " ");
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
                funcionarios.add(funcionario);
            }
            repository.saveAll(funcionarios);
            workbook.close();

    }

    public List<FuncionarioModel> listarTodosFuncionarios() {
        return repository.findAll();
    }

}