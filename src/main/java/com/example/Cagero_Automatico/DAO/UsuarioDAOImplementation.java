package com.example.Cagero_Automatico.DAO;

import com.example.Cagero_Automatico.JPA.Result;
import com.example.Cagero_Automatico.JPA.Rol;
import com.example.Cagero_Automatico.JPA.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAOImplementation implements IUsuarioDAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAll() {
        Result result = new Result();
        try {
            TypedQuery<Usuario> queryUsuario = entityManager.createQuery("FROM Usuario", Usuario.class);
            List<Usuario> usuarios = queryUsuario.getResultList();
            result.object = usuarios;
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result Status(int idUsuario) {
        Result result = new Result();
        return result;
    }

    @Override
    public Result Login(String username, String password) {
        Result result = new Result();
        try {
            var storedProcedure = entityManager.createStoredProcedureQuery("LOGIN_USUARIO");

            // Paramtros Entrada
            storedProcedure.registerStoredProcedureParameter("P_USERNAME", String.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("P_PASSWORD", String.class, ParameterMode.IN);

            // Parámetros Salida
            storedProcedure.registerStoredProcedureParameter("P_IDUSUARIO", Integer.class, ParameterMode.OUT);
            storedProcedure.registerStoredProcedureParameter("P_NOMBRE", String.class, ParameterMode.OUT);
            storedProcedure.registerStoredProcedureParameter("P_APELLIDOPATERNO", String.class, ParameterMode.OUT);
            storedProcedure.registerStoredProcedureParameter("P_APELLIDOMATERNO", String.class, ParameterMode.OUT);
            storedProcedure.registerStoredProcedureParameter("P_IDROLL", Integer.class, ParameterMode.OUT);
            storedProcedure.registerStoredProcedureParameter("P_NOMBREROL", String.class, ParameterMode.OUT);
            storedProcedure.registerStoredProcedureParameter("P_RESULTADO", Integer.class, ParameterMode.OUT);

            storedProcedure.setParameter("P_USERNAME", username);
            storedProcedure.setParameter("P_PASSWORD", password);

            // Asignar valores de entrada
            storedProcedure.setParameter("P_USERNAME", username);
            storedProcedure.setParameter("P_PASSWORD", password);

            // Ejecutar
            storedProcedure.execute();

            Integer loginResult = (Integer) storedProcedure.getOutputParameterValue("P_RESULTADO");

            // Validacion de que el usuario este activo
            if (loginResult != null && loginResult == 1) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario((Integer) storedProcedure.getOutputParameterValue("P_IDUSUARIO"));
                usuario.setNombre((String) storedProcedure.getOutputParameterValue("P_NOMBRE"));
                usuario.setApellidoPaterno((String) storedProcedure.getOutputParameterValue("P_APELLIDOPATERNO"));
                usuario.setApellidoMaterno((String) storedProcedure.getOutputParameterValue("P_APELLIDOMATERNO"));
                Rol rol = new Rol();
                rol.setIdRol((Integer) storedProcedure.getOutputParameterValue("P_IDROLL"));
                rol.setNombre((String) storedProcedure.getOutputParameterValue("P_NOMBREROL"));

                usuario.setUserName(username);
                usuario.setRol(rol);

                result.correct = true;
                result.object = usuario;
                result.message = "Login exitoso";
            } else {
                result.correct = false;
                result.message = "Usuario o contraseña incorrectos";
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }
        return result;
    }
}
