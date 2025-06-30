package com.example.Cagero_Automatico.DAO;

import com.example.Cagero_Automatico.JPA.Efectivo;
import com.example.Cagero_Automatico.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
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
    @Transactional // Marca el método como transaccional, es decir, todas las operaciones de base de datos dentro del método se ejecutan como una sola transacción.
    public Result Retiro(double monto) {
        Result result = new Result(); // Se crea un objeto para almacenar el resultado del retiro (éxito o error).
        try {
            // Consulta la tabla 'Efectivo' y obtiene todas las denominaciones ordenadas de mayor a menor.
            TypedQuery<Efectivo> query = entityManager.createQuery(
                    "FROM Efectivo ORDER BY denominacion DESC", Efectivo.class);
            List<Efectivo> efectivoDisponible = query.getResultList();

            // Mapa que almacenará la cantidad a retirar por cada denominación.
            Map<Double, Integer> retiro = new LinkedHashMap<>();

            // Recorre las denominaciones de efectivo disponibles.
            for (Efectivo ef : efectivoDisponible) {
                if (monto <= 0) {
                    break; // Si ya se cubrió el monto total a retirar, se termina el ciclo.
                }

                double denominacion = ef.getDenominacion(); // Ej: 500, 200, 100, etc.
                int disponible = ef.getCantidad(); // Cantidad disponible de esa denominación.

                // Calcula cuántos billetes/monedas se podrían usar para el monto restante.
                int maxRetirables = (int) (monto / denominacion);

                // Se usa la menor cantidad entre los disponibles y los necesarios.
                int cantidad_a_Retirar = Math.min(maxRetirables, disponible);

                if (cantidad_a_Retirar > 0) {
                    // Se guarda en el mapa el número de billetes/monedas que se van a retirar de esa denominación.
                    retiro.put(denominacion, cantidad_a_Retirar);

                    // Se actualiza el monto restante por retirar.
                    monto -= cantidad_a_Retirar * denominacion;

                    // Redondeo a 2 decimales para evitar errores por precisión de punto flotante.
                    monto = Math.round(monto * 100.0) / 100.0;
                }
            }

            // Si se logró cubrir el monto exacto
            if (monto == 0) {
                // Se actualiza la base de datos restando la cantidad retirada de cada denominación
                for (Map.Entry<Double, Integer> entry : retiro.entrySet()) {
                    double denom = entry.getKey();
                    int cantidad_a_Retirar = entry.getValue();

                    // Se consulta el objeto Efectivo correspondiente a esa denominación
                    TypedQuery<Efectivo> queryEfectivo = entityManager.createQuery(
                            "FROM Efectivo WHERE denominacion = :denom", Efectivo.class);
                    queryEfectivo.setParameter("denom", denom);
                    Efectivo efectivo = queryEfectivo.getSingleResult();

                    // Se actualiza la cantidad disponible en la base de datos
                    efectivo.setCantidad(efectivo.getCantidad() - cantidad_a_Retirar);
                    entityManager.merge(efectivo); // Se guarda el cambio
                }

                // Se marca el resultado como exitoso
                result.correct = true;
                result.object = retiro; // Se devuelve el detalle de lo retirado
            } else {
                // Si no se pudo cubrir el monto exacto, se marca como error
                result.correct = false;
                result.errorMessage = "El cajero no cuenta con el dinero suficiente para la transacción";
            }
        } catch (Exception ex) {
            // Manejo de errores inesperados
            result.correct = false;
            result.errorMessage = ex.getMessage(); // Mensaje de error
            result.ex = ex; // Se guarda la excepción completa (por si se quiere registrar o mostrar)
        }

        return result; // Se devuelve el resultado final
    }

    @Override
    public Result Rellenar() {
        Result result = new Result();
        try {
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("EfectivoReestablecer");
            query.execute();
            result.correct = true;
            result.message = "El efectivo se restablecio correctamente";
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = "Error al restablecer el Efectivo" + ex.getMessage();
            result.ex = ex;
        }
        return result;
    }
}
