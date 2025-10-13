package com.puc.realconsult.controller;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.puc.realconsult.model.FuncionarioModel;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpStatus;
import com.puc.realconsult.service.FuncionarioService;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

@AllArgsConstructor
@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    private final FuncionarioService service;

    @PostMapping("/getdados")
    public ResponseEntity getPlanilha(@RequestParam("file") MultipartFile file){
        try (InputStream is = file.getInputStream()){
             service.importarDados(is);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
        return ResponseEntity.ok().body(200);
    }
}
