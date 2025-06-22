package com.example.Cagero_Automatico.DAO;

import com.example.Cagero_Automatico.JPA.Efectivo;
import com.example.Cagero_Automatico.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EfectivoDAOImplemetation implements IEfectivoDAO{
    
    @Autowired
    private EntityManager entityManager;
    
    @Override
    public Result GetAll() {
        Result result = new Result();
        try {
            TypedQuery<Efectivo> queryEfectivo = entityManager.createQuery("FROM Efectivo", Efectivo.class);
            List<Efectivo> dineros = queryEfectivo.getResultList();
            result.object = dineros;
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }
}
