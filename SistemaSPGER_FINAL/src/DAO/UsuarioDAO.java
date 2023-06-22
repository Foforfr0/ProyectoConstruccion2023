package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import POJO.Usuario;
import ConexionBD.ConexionBD;
import ConexionBD.ResultadoOperacion;
import POJO.Anteproyecto;
import Utils.ShowMessage;
import java.sql.SQLIntegrityConstraintViolationException;
import javafx.scene.control.Alert;

public class UsuarioDAO {
    public static ResultadoOperacion registrarUsuario(Usuario newUsuario) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "INSERT INTO usuario(matricula, nombre , apellidoPaterno , "
                        + " apellidoMaterno, username, password, rolSistema) "
                        + "VALUES (?,?,?,?,?,?,?)";
                PreparedStatement setUsuario = conexionBD.prepareStatement(sqlQuery);
                    setUsuario.setInt(1, newUsuario.getMatricula());
                    setUsuario.setString(2, newUsuario.getNombre());
                    setUsuario.setString(3, newUsuario.getApellidoPaterno());
                    setUsuario.setString(4, newUsuario.getApellidoMaterno());
                    setUsuario.setString(5, newUsuario.getNombreUsuario());
                    setUsuario.setString(6, newUsuario.getPassword());
                    setUsuario.setInt(7, newUsuario.getIdRolSistema());
                int affectedRows = setUsuario.executeUpdate();
                if(affectedRows > 0){
                    resultado.setError(false);
                    resultado.setMensaje("Usuario registrado");
                    resultado.setFilasAfectadas(affectedRows);
                }else{
                    resultado.setMensaje("No se pudo registrar el usuario");
                } 
            } catch (SQLIntegrityConstraintViolationException ex){
                throw ex;
            } catch (SQLException ex) {
                throw ex;
            } finally{
                conexionBD.close();
            }
        }else{
            resultado.setMensaje("Sin conexión con la base de datos");
        }
        return resultado;
    }

    public static Usuario getUsuario(int idUsuario) throws SQLException{
        Usuario usuarioBD = null;
        ResultadoOperacion response = new ResultadoOperacion();
        response.setError(true);
        response.setFilasAfectadas(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "";
                PreparedStatement getUsuario = conexionBD.prepareStatement(sqlQuery);
                ResultSet resultSet = getUsuario.executeQuery();
                getUsuario.setInt(1, idUsuario);   
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            response.setMensaje("Por el momento no hay conexión con la base de datos...");
        }
        return usuarioBD;
    }
    
    public static ArrayList<Usuario> obtenerUsuarios() throws SQLException{
        ArrayList<Usuario> usuariosBD = null;
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT u.idusuario, u.matricula, u.nombre, u.apellidoPaterno, u.apellidoMaterno, "
                        + "r.rolSistema AS tipoUsuario "
                        + "FROM usuario u INNER JOIN rolsistema r ON u.rolSistema = r.idrolSistema";
                PreparedStatement getAllUsers = conexionBD.prepareStatement(sqlQuery);
                ResultSet result = getAllUsers.executeQuery();
                usuariosBD = new ArrayList<>();
                while(result.next()){
                    Usuario newUsuario = new Usuario();
                       newUsuario.setIdUsuario(result.getInt("u.idUsuario"));
                       newUsuario.setNombre(result.getString("u.nombre"));
                       newUsuario.setMatricula(result.getInt("u.matricula"));
                       newUsuario.setApellidoPaterno(result.getString("u.apellidoPaterno"));
                       newUsuario.setApellidoMaterno(result.getString("u.apellidoMaterno"));
                       newUsuario.setPrueba(result.getString("tipoUsuario"));
                    usuariosBD.add(newUsuario);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            resultado.setMensaje("Sin conexión con la BD");
        }
        return usuariosBD;
    }
    
    public static ArrayList<Usuario> obtenerUsuariosAdministradores() throws SQLException{
        ArrayList<Usuario> usuariosBD = null;
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT u.idusuario, u.nombre, u.matricula,"
                        + " u.apellidoPaterno, u.apellidoMaterno, u.password, u.username, r.rolSistema "
                        + "AS tipoUsuario, u.rolSistema FROM usuario u INNER JOIN rolsistema r ON u.rolSistema = "
                        + "r.idrolSistema WHERE u.rolSistema = 1";
                PreparedStatement getAllUsers = conexionBD.prepareStatement(sqlQuery);
                ResultSet result = getAllUsers.executeQuery();
                usuariosBD = new ArrayList<>();
                while(result.next()){
                    Usuario newUsuario = new Usuario();
                       newUsuario.setIdUsuario(result.getInt("u.idusuario"));
                       newUsuario.setMatricula(result.getInt("u.matricula"));
                       newUsuario.setNombre(result.getString("u.nombre"));
                       newUsuario.setApellidoPaterno(result.getString("u.apellidoPaterno"));
                       newUsuario.setApellidoMaterno(result.getString("u.apellidoMaterno"));
                       newUsuario.setPrueba(result.getString("tipoUsuario"));
                       newUsuario.setNombreUsuario(result.getString("u.username"));
                       newUsuario.setPassword(result.getString("u.password"));
                       newUsuario.setIdRolSistema(result.getInt("u.rolSistema"));
                    usuariosBD.add(newUsuario);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            resultado.setMensaje("Sin conexión con la base de datos");
        }
        return usuariosBD;
    }
    
    public static ArrayList<Usuario> obtenerUsuariosAcademicos() throws SQLException{
        ArrayList<Usuario> usuariosBD = null;
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT u.idusuario, u.nombre, u.matricula,"
                        + " u.apellidoPaterno, u.apellidoMaterno, u.username, u.password, r.rolSistema "
                        + "AS tipoUsuario, u.rolSistema FROM usuario u INNER JOIN rolsistema r ON u.rolSistema = "
                        + "r.idrolSistema WHERE u.rolSistema = 2";
                PreparedStatement getAllUsers = conexionBD.prepareStatement(sqlQuery);
                ResultSet result = getAllUsers.executeQuery();
                usuariosBD = new ArrayList<>();
                while(result.next()){
                    Usuario newUsuario = new Usuario();
                       newUsuario.setIdUsuario(result.getInt("u.idusuario"));
                       newUsuario.setMatricula(result.getInt("u.matricula"));
                       newUsuario.setNombre(result.getString("u.nombre"));
                       newUsuario.setApellidoPaterno(result.getString("u.apellidoPaterno"));
                       newUsuario.setApellidoMaterno(result.getString("u.apellidoMaterno"));
                       newUsuario.setPrueba(result.getString("tipoUsuario"));
                       newUsuario.setNombreUsuario(result.getString("u.username"));
                       newUsuario.setPassword(result.getString("u.password"));
                       newUsuario.setIdRolSistema(result.getInt("u.rolSistema"));
                    usuariosBD.add(newUsuario);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            resultado.setMensaje("Sin conexión con la BD");
        }
        return usuariosBD;
    }
    
    public static ArrayList<Usuario> obtenerUsuariosEstudiantes() throws SQLException{
        ArrayList<Usuario> usuariosBD = null;
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT u.idusuario, u.nombre, u.matricula,"
                        + " u.apellidoPaterno, u.apellidoMaterno, u.username, u.password, r.rolSistema "
                        + "AS tipoUsuario, u.rolSistema FROM usuario u INNER JOIN rolsistema r ON u.rolSistema = "
                        + "r.idrolSistema WHERE u.rolSistema = 3";
                PreparedStatement getAllUsers = conexionBD.prepareStatement(sqlQuery);
                ResultSet result = getAllUsers.executeQuery();
                usuariosBD = new ArrayList<>();
                while(result.next()){
                    Usuario newUsuario = new Usuario();
                       newUsuario.setIdUsuario(result.getInt("u.idusuario"));
                       newUsuario.setMatricula(result.getInt("u.matricula"));
                       newUsuario.setNombre(result.getString("u.nombre"));
                       newUsuario.setApellidoPaterno(result.getString("u.apellidoPaterno"));
                       newUsuario.setApellidoMaterno(result.getString("u.apellidoMaterno"));
                       newUsuario.setPrueba(result.getString("tipoUsuario"));
                       newUsuario.setNombreUsuario(result.getString("u.username"));
                       newUsuario.setPassword(result.getString("u.password"));
                       newUsuario.setIdRolSistema(result.getInt("u.rolSistema"));
                    usuariosBD.add(newUsuario);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            resultado.setMensaje("Sin conexión con la BD");
        }
        return usuariosBD;
    }
    
    public static ResultadoOperacion eliminarUsuario(int idUsuario) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "DELETE FROM usuario WHERE idUsuario = ?";                
                PreparedStatement delUsuario = conexionBD.prepareStatement(sqlQuery);
                    delUsuario.setInt(1, idUsuario);
                int rowsAffected = delUsuario.executeUpdate();
                if(rowsAffected > 0){
                    resultado.setError(false);
                    resultado.setMensaje("Usuario eliminado correctamente.");
                    resultado.setFilasAfectadas(rowsAffected);
                }else{
                    resultado.setMensaje("No se pudo eliminar el usuario");
                }
            } catch (SQLException e) {
                resultado.setMensaje(e.getMessage());
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            resultado.setMensaje("Sin conexión con la DB");
        }
        return resultado;
    }
    
    private static Usuario usuarioActual;
    
    public static Usuario verificarUsuario(String usuario, String password) throws SQLException{
        Usuario sesion = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();

        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT idusuario, matricula, nombre, apellidoPaterno, apellidoMaterno, " +
                                  "username, password, rolSistema " +
                                  "FROM usuario WHERE username = ? AND password = ?; ";
                PreparedStatement consultLogin = conexionBD.prepareStatement(sqlQuery);
                    consultLogin.setString(1, usuario);
                    consultLogin.setString(2, password);
                ResultSet getUsuario = consultLogin.executeQuery();

                if(getUsuario.next()){
                    sesion = new Usuario();
                        sesion.setIdUsuario( getUsuario.getInt("idusuario"));
                        sesion.setMatricula(getUsuario.getInt("matricula"));
                        sesion.setNombre(getUsuario.getString("nombre"));
                        sesion.setApellidoPaterno(getUsuario.getString("apellidoPaterno"));
                        sesion.setApellidoMaterno(getUsuario.getString("apellidoMaterno"));
                        sesion.setNombreUsuario(getUsuario.getString("username"));
                        sesion.setPassword(getUsuario.getString("password"));
                        sesion.setIdRolSistema(getUsuario.getInt("rolsistema"));                    
                }
            } catch (SQLException s) {
                s.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            ShowMessage.showAlertSimple(
                "Error de conexión", 
                "No se pudo establecer conexión con la base de datos.", 
                Alert.AlertType.ERROR
            );
            sesion = new Usuario();
        }
        usuarioActual = sesion;
        return sesion;
    }
    
    public static Usuario getUsuarioActual(){
        return usuarioActual;
    }
    
    public static int obtenerTipoUsuario(int idUsuario)throws SQLException{
        Connection conexionDB = ConexionBD.abrirConexionBD();
        int tipoUsuario = 0;
        if (conexionDB != null) {
            try {
                String sqlQuery = "SELECT rolsistema FROM usuario WHERE idusuario = ?";
                PreparedStatement consulta = conexionDB.prepareStatement(sqlQuery);
                consulta.setInt(1, idUsuario);
                ResultSet resultado = consulta.executeQuery();
                if (resultado.next()) {
                    tipoUsuario = resultado.getInt("rolsistema");
                }
                resultado.close();
                consulta.close();
            } catch (Exception e) {
            e.printStackTrace();
            } finally {
            conexionDB.close();
            }
        }
            return tipoUsuario -1; 
        }
    
    public static ResultadoOperacion guardarEdicion(Usuario usuario) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "UPDATE usuario SET matricula = ?, nombre = ?,"
                        + "apellidoPaterno = ?, apellidoMaterno = ?, username = ?,"
                        + "password = ? WHERE idusuario = ?"; 
                PreparedStatement editar = conexionBD.prepareStatement(query);
                editar.setInt(1, usuario.getMatricula());
                editar.setString(2, usuario.getNombre());
                editar.setString(3, usuario.getApellidoPaterno());
                editar.setString(4, usuario.getApellidoMaterno());
                editar.setString(5, usuario.getNombreUsuario());
                editar.setString(6, usuario.getPassword());
                editar.setInt(7, usuario.getIdUsuario());
                int filasAfectadas = editar.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Usuario modificado");
                    resultado.setFilasAfectadas(filasAfectadas);
                }else{
                    resultado.setMensaje("Error al editar");
                }
            } catch (SQLIntegrityConstraintViolationException ex) {
                throw ex;
            } catch (SQLException ex){
                throw ex;
            } finally {
                conexionBD.close();
            }
        } else {
            ShowMessage.showAlertSimple("Error",
                    "Sin conexión con la BD",
                    Alert.AlertType.ERROR);
        }
        return resultado;
    }
    
    public static ArrayList<Usuario> obtenerEstudiantesAnteproyecto(Anteproyecto anteproyecto) throws SQLException{
        ArrayList<Usuario> estudiantesAnteproyecto = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT idusuario, matricula, nombre, apellidoPaterno, apellidoMaterno"
                        + " FROM usuario"
                        + " WHERE idAnteproyecto = ?";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(sqlQuery);
                prepararConsulta.setInt(1, anteproyecto.getIdAnteproyecto());
                ResultSet result = prepararConsulta.executeQuery();
                estudiantesAnteproyecto = new ArrayList<>();
                while(result.next()){
                    Usuario newUsuario = new Usuario();
                       newUsuario.setIdUsuario(result.getInt("idusuario"));
                       newUsuario.setMatricula(result.getInt("matricula"));
                       newUsuario.setNombre(result.getString("nombre"));
                       newUsuario.setApellidoPaterno(result.getString("apellidoPaterno"));
                       newUsuario.setApellidoMaterno(result.getString("apellidoMaterno"));
                    estudiantesAnteproyecto.add(newUsuario);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            ShowMessage.showAlertSimple("ERROR",
                    "No hay conexión con la BD",
                    Alert.AlertType.ERROR);
        }
        return estudiantesAnteproyecto;
    }
    
    public static ResultadoOperacion eliminarEstudianteAnteproyecto(Usuario usuario) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "ERROR", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "UPDATE usuario set idAnteproyecto = NULL where idusuario = ?";
                PreparedStatement eliminar = conexionBD.prepareStatement(query);
                eliminar.setInt(1, usuario.getIdUsuario());
                int filasAfectadas = eliminar.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Estudiante eliminado");
                    resultado.setFilasAfectadas(filasAfectadas);
                } else {
                    resultado.setMensaje("ERROR");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return  resultado;
    }
    
    public static ArrayList<Usuario> consultarEstudiantesDisponibles() throws SQLException{
        ArrayList<Usuario> estudiantesDisponibles = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "SELECT idusuario, matricula, nombre, apellidoPaterno, apellidoMaterno"
                        + " FROM usuario"
                        + " WHERE idAnteproyecto IS NULL AND rolSistema = 3";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(query);
                ResultSet resultado = prepararConsulta.executeQuery();
                estudiantesDisponibles = new ArrayList<>();
                while (resultado.next()) {                    
                    Usuario estudiante = new Usuario();
                    estudiante.setIdUsuario(resultado.getInt("idusuario"));
                    estudiante.setMatricula(resultado.getInt("matricula"));
                    estudiante.setNombre(resultado.getString("nombre"));
                    estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    estudiantesDisponibles.add(estudiante);
                }
            } catch (SQLException e) {
                ShowMessage.showAlertSimple("ERROR",
                        "Sin coneión con la BD",
                        Alert.AlertType.ERROR);
            } finally {
                conexionBD.close();
            }
        }
        return estudiantesDisponibles;
    }
    
    public static ResultadoOperacion asignarAnteproyecto(Usuario usuario,Anteproyecto anteproyecto) throws SQLException{
        ResultadoOperacion resultadoOperacion = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "UPDATE usuario SET idAnteproyecto = ? WHERE idUsuario = ?";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(sqlQuery);
                prepararConsulta.setInt(1, anteproyecto.getIdAnteproyecto());
                prepararConsulta.setInt(2, usuario.getIdUsuario());           
                int filasAfectadas = prepararConsulta.executeUpdate();
                resultadoOperacion.setFilasAfectadas(filasAfectadas);
                resultadoOperacion.setError(false);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            ShowMessage.showAlertSimple("ERROR",
                    "No hay conexión con la BD",
                    Alert.AlertType.ERROR);
            resultadoOperacion.setError(true);
        }
        return resultadoOperacion;
    }
}
