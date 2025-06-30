package com.example.Cagero_Automatico.RestControler;

import com.example.Cagero_Automatico.DAO.RolDAOImplementation;
import com.example.Cagero_Automatico.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Rolapi")
public class RolRestController {
    
    @Autowired
    private RolDAOImplementation rolDAOImplementation;
    
    @GetMapping
    public ResponseEntity GetAll(){
        Result result = new Result();
        return ResponseEntity.ok(rolDAOImplementation.GetAll());
    }
}
