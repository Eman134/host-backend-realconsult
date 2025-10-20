package com.puc.realconsult.service;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import java.io.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import com.puc.realconsult.model.AuditoriaModel;
import com.puc.realconsult.repository.AuditoriaRepository;

@Service
@AllArgsConstructor
public class AuditoriaService{

    private final AuditoriaRepository repository;

    public List<AuditoriaModel> listarTodasAuditorias() {
        return repository.findAll();
    }

    public AuditoriaModel cadastrar(AuditoriaModel auditoria) {
        return repository.save(auditoria);
    }

    public List<AuditoriaModel> listar() {
        List<AuditoriaModel> auditorias = repository.findAll();
        if (auditorias.isEmpty()) {
            return Collections.emptyList();
        }
        return auditorias;
    }

}