package com.puc.realconsult.testeConexãoEndpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class testeController {

    @GetMapping("/teste")
    public ResponseEntity<String> testeConexão(){
        return ResponseEntity.ok().body("Conexão estável");
    }
}
