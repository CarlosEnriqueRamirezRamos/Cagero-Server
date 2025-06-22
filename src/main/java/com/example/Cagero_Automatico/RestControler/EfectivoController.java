package com.example.Cagero_Automatico.RestControler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Cagero_Automatico.DAO.EfectivoDAOImplemetation;
import com.example.Cagero_Automatico.JPA.Result;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "*")         //para permitir que tu frontend (en localhost:8080) haga peticiones a tu backend (en localhost:8081)
@RestController
@RequestMapping("/Efectivoapi")
public class EfectivoController {

    @Autowired
    private EfectivoDAOImplemetation efectivoDAOImplemetation;
    @Autowired
    private JdbcTemplate jdbcTemplate;      //Permite ejecutar consultas SQL directamente desde el controlador

    @GetMapping
    public ResponseEntity GetAll() {
        Result result = new Result();
        return ResponseEntity.ok(efectivoDAOImplemetation.GetAll());
    }

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> getSaldoTotal() {
        String sql = "SELECT SUM(cantidad * denominacion) FROM efectivo";
        BigDecimal total = jdbcTemplate.queryForObject(sql, BigDecimal.class);
        return ResponseEntity.ok(total);
    }

}
