package com.example.Cagero_Automatico.RestControler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Cagero_Automatico.DAO.EfectivoDAOImplemetation;
import com.example.Cagero_Automatico.JPA.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/Efectivoapi")
public class EfectivoController {
    @Autowired
    private EfectivoDAOImplemetation efectivoDAOImplemetation;
    
    @GetMapping
    public ResponseEntity GetAll(){
        Result result = new Result();
        return ResponseEntity.ok(efectivoDAOImplemetation.GetAll());
    }
}
