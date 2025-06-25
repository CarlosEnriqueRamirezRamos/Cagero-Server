package com.example.Cagero_Automatico.Service;

import com.example.Cagero_Automatico.DAO.UsuarioRepository;
import com.example.Cagero_Automatico.JPA.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service // Indica que esta clase es un servicio y debe ser manejada por Spring
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired // Inyectamos el repositorio para poder buscar dentro de la base de datos
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Busca en la base de datos por el nombre del usuario escrito
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        String rolNombre = usuario.Rol.getNombre();
        // Creamos y devolvemos un objeto User que Spring Security entiende
        return new org.springframework.security.core.userdetails.User(
                usuario.getUserName(),              // El nombre de usuario
                usuario.getPassword(),              // La contraseña (debe estar cifrada con BCrypt)
                Collections.singletonList(new SimpleGrantedAuthority(rolNombre) // Lista de permisos o roles (aquí solo "USER")
                )
        );
    }
}