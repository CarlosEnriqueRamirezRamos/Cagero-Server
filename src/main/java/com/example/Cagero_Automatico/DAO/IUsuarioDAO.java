package com.example.Cagero_Automatico.DAO;

import com.example.Cagero_Automatico.JPA.Result;

public interface IUsuarioDAO {
    Result GetAll();    
    Result Status(int idUsuario);    
}
