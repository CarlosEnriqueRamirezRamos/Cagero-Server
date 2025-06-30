package com.example.Cagero_Automatico.RestControler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Cagero_Automatico.DAO.EfectivoDAOImplemetation;
import com.example.Cagero_Automatico.JPA.Result;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/retiro")
    public ResponseEntity<?> retirarDinero(@RequestParam double monto) {
        Result result = efectivoDAOImplemetation.Retiro(monto);

        if (result.correct) {
            return ResponseEntity.ok(result.object); // JSON con el Map de retiro
        } else {
            return ResponseEntity.badRequest().body(result.errorMessage);
        }
    }

    @GetMapping("/rellenar")
    public ResponseEntity<Void> ejecutarReestablecer() {
        try {
            jdbcTemplate.execute("BEGIN EfectivoReestablecer; END;");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
