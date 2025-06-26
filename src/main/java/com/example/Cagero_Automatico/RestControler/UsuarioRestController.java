package com.example.Cagero_Automatico.RestControler;

import com.example.Cagero_Automatico.DAO.UsuarioDAOImplementation;
import com.example.Cagero_Automatico.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Usuario")
public class UsuarioRestController {
    @Autowired
    private UsuarioDAOImplementation UsuarioDAOImplementation;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
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
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            Authentication auth = (Authentication) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPasword())
            );

            // Si llegó aquí, autenticación exitosa
            User user = (User) auth.getPrincipal();

            // Opcional: podrías devolver datos del usuario, roles, o un token JWT
            return ResponseEntity.ok("Login exitoso para " + user.getUsername());

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
        }
    }
}
