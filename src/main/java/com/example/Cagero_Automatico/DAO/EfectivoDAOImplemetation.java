package com.example.Cagero_Automatico.DAO;

import com.example.Cagero_Automatico.JPA.Efectivo;
import com.example.Cagero_Automatico.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EfectivoDAOImplemetation implements IEfectivoDAO {

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

    @Override
    @Transactional
    public Result Retiro(double monto) {
        Result result = new Result();
        try {
            TypedQuery<Efectivo> query = entityManager.createQuery(
                    "FROM Efectivo ORDER BY denominacion DESC", Efectivo.class);
            List<Efectivo> efectivoDisponible = query.getResultList();
            Map<Double, Integer> retiro = new LinkedHashMap<>();
            for (Efectivo ef : efectivoDisponible) {
                if (monto <= 0) {
                    break;
                }
                double denominacion = ef.getDenominacion();
                int disponible = ef.getCantidad();
                int maxRetirables = (int) (monto / denominacion);
                int cantidad_a_Retirar = Math.min(maxRetirables, disponible);

                if (cantidad_a_Retirar > 0) {
                    retiro.put(denominacion, cantidad_a_Retirar);
                    monto -= cantidad_a_Retirar * denominacion;
                    monto = Math.round(monto * 100.0) / 100.0;
                }
            }
            if (monto == 0) {
                //Actualizacion de la base de datos
                for (Map.Entry<Double, Integer> entry : retiro.entrySet()) {
                    double denom = entry.getKey();
                    int cantidad_a_Retirar = entry.getValue();

                    TypedQuery<Efectivo> queryEfectivo = entityManager.createQuery(
                            "FROM Efectivo WHERE denominacion = :denom", Efectivo.class);
                    queryEfectivo.setParameter("denom", denom);
                    Efectivo efectivo = queryEfectivo.getSingleResult();

                    efectivo.setCantidad(efectivo.getCantidad() - cantidad_a_Retirar);
                    entityManager.merge(efectivo);
                }
                result.correct = true;
                result.object = retiro;
            } else {
                result.correct = false;
                result.errorMessage = "Errror al retirar el monto: " + Retiro(monto) + " con la cantidad disponible en el cajero";
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }
        return result;
    }
}
