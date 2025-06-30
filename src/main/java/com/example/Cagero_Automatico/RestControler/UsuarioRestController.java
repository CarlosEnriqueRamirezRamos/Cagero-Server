package com.example.Cagero_Automatico.RestControler;

import com.example.Cagero_Automatico.DAO.UsuarioDAOImplementation;
import com.example.Cagero_Automatico.JPA.Result;
import com.example.Cagero_Automatico.JPA.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Usuario")
public class UsuarioRestController {
    @Autowired
    private UsuarioDAOImplementation UsuarioDAOImplementation;
    @GetMapping
    public ResponseEntity GetAll(){
        Result result = new Result();
        return ResponseEntity.ok(UsuarioDAOImplementation.GetAll());
    }
    
    @GetMapping("/status")
    public ResponseEntity Status(int idUsuarios){
        Result result = new Result();
        return ResponseEntity.ok(UsuarioDAOImplementation.Status(idUsuarios));
    }
    
    @ResponseBody
    @PostMapping("/Login")
    public Result login(@RequestBody Usuario usuario){
        Result result = UsuarioDAOImplementation.Login(usuario.getUserName(), usuario.getPassword());
        return result;
    }
}
