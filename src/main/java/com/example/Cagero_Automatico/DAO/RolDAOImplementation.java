package com.example.Cagero_Automatico.DAO;

import com.example.Cagero_Automatico.JPA.Result;
import com.example.Cagero_Automatico.JPA.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class RolDAOImplementation implements IRolDAO{
    
    @Autowired
    private EntityManager entityManager;
    
    @Override
    public Result GetAll() {
        Result result = new Result();
        try{
            TypedQuery<Rol> queryRol = entityManager.createQuery("FROM Rol",
                    Rol.class);
            result.object = queryRol.getResultList();
            result.correct = true;
        }catch (Exception ex){
            System.out.println("Error al traer el Rol");
        }
        return result;
    }
}
