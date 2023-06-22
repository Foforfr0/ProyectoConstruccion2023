package DAO;

import ConexionBD.ConexionBD;
import ConexionBD.ResultadoOperacion;
import POJO.Avance;
import POJO.Curso;
import POJO.EntregaAvance;
import POJO.Usuario;
import Utils.ShowMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import javafx.scene.control.Alert;


public class EntregaAvanceDAO {
    
    public static Curso cursoActual = null;
    public static Avance avanceActual = null;
    public static ArrayList<Usuario> listaAlumnosEntregas = null;
    public static EntregaAvance entregaActual = null;
    
    public static ResultadoOperacion realizarEntrega(EntregaAvance nuevaEntrega) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true, "ERROR", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String query = "INSERT INTO entregaavance (idavance, descripcion, idusuario, " +
                               "fechaEntrega, calificacion, nombreArchivo, archivo) " +
                               "VALUES (?,?,?,?,NULL,?,?); ";
                PreparedStatement setEntrega = conexionBD.prepareStatement(query);
                    setEntrega.setInt(1, nuevaEntrega.getIdAvance());
                    setEntrega.setString(2, nuevaEntrega.getDescripEntrega());
                    setEntrega.setInt(3, nuevaEntrega.getIdEstudiante());
                    setEntrega.setDate(4, nuevaEntrega.getFechaEntrega());
                    //setEntrega.setInt(4, nuevaEntrega.getCalificacion());
                    setEntrega.setString(5, nuevaEntrega.getNombreArchivo());
                    setEntrega.setBytes(6, nuevaEntrega.getArchivo());
                    
                int filasAfectadas = setEntrega.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Curso creado");
                    resultado.setFilasAfectadas(filasAfectadas);
                } else {
                    resultado.setMensaje("Error al crear el curso");
                }
            } catch (SQLException e) {
                ShowMessage.showAlertSimple(
                    "ERROR",
                    "Error en la base de datos",
                    Alert.AlertType.ERROR
                );
                e.printStackTrace();
            } finally {
                conexionBD.close();;
            }
        } else {
            ShowMessage.showAlertSimple(
                "Sin conexión",
                "Sin conexión con la BD",
                Alert.AlertType.ERROR
            );
        }
        return resultado;
    }
    
    public static ResultadoOperacion modificarEntrega(EntregaAvance nuevaEntrega) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true, "ERROR", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String query = "UPDATE entregaavance SET descripcion = ?, fechaEntrega = ?, nombreArchivo = ?, archivo = ? " +
                               "WHERE idavance = ? AND idusuario = ?; ";
                PreparedStatement setEntrega = conexionBD.prepareStatement(query);
                    setEntrega.setString(1, nuevaEntrega.getDescripEntrega());
                    setEntrega.setDate(2, nuevaEntrega.getFechaEntrega());
                    setEntrega.setString(3, nuevaEntrega.getNombreArchivo());
                    setEntrega.setBytes(4, nuevaEntrega.getArchivo());
                    setEntrega.setInt(5, nuevaEntrega.getIdAvance());
                    setEntrega.setInt(6, nuevaEntrega.getIdEstudiante());
                    
                int filasAfectadas = setEntrega.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Curso creado");
                    resultado.setFilasAfectadas(filasAfectadas);
                } else {
                    resultado.setMensaje("Error al crear el curso");
                }
            } catch (SQLException e) {
                ShowMessage.showAlertSimple(
                    "ERROR",
                    "Error en la base de datos",
                    Alert.AlertType.ERROR
                );
                e.printStackTrace();
            } finally {
                conexionBD.close();;
            }
        } else {
            ShowMessage.showAlertSimple(
                "Sin conexión",
                "Sin conexión con la BD",
                Alert.AlertType.ERROR
            );
        }
        return resultado;
    }
    
    public static boolean verificarExistenciaEntrega(EntregaAvance nuevaEntrega) throws SQLException{
        //TRUE SI EXISTE UNA O MAS ENTREGAS AL MISMO AVANCE
        ArrayList<EntregaAvance> listaEntregas = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String query = "SELECT * FROM entregaavance INNER JOIN usuario ON " +
                               "entregaavance.idUsuario = usuario.idusuario " +
                               "INNER JOIN avance ON entregaavance.idavance = avance.idavance " +
                               "WHERE usuario.idUsuario = ? AND avance.idavance = ?; ";
                PreparedStatement setEntrega = conexionBD.prepareStatement(query);
                    setEntrega.setInt(1, nuevaEntrega.getIdEstudiante());
                    setEntrega.setInt(2, nuevaEntrega.getIdAvance());
                ResultSet result = setEntrega.executeQuery();

                listaEntregas = new ArrayList<>();
                while(result.next()){
                    EntregaAvance entrega = new EntregaAvance();
                        entrega.setIdEntregaAvance(result.getInt("identregaAvance"));
                    listaEntregas.add(entrega);
                }
            } catch (SQLException e) {
                ShowMessage.showAlertSimple(
                    "ERROR",
                    "Error en la base de datos",
                    Alert.AlertType.ERROR
                );
                e.printStackTrace();
            } finally {
                conexionBD.close();;
            }
        } else {
            ShowMessage.showAlertSimple(
                "Sin conexión",
                "Sin conexión con la BD",
                Alert.AlertType.ERROR
            );
        }
        if(listaEntregas.size() > 0){
            return true;
        }else if(listaEntregas == null || listaEntregas.size()<=0){
            return false;
        }
        return true;
    }
    
    public static ArrayList<EntregaAvance> getEntregasAvance(int idAvance) throws SQLException{
        ArrayList<EntregaAvance> entregasAvance = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT * FROM entregaavance LEFT JOIN avance ON " +
                                  "entregaavance.idavance = avance.idavance " +
                                  "WHERE avance.idavance = ?; ";
                PreparedStatement getEntregas = conexionBD.prepareStatement(sqlQuery);
                getEntregas.setInt(1, idAvance);
                ResultSet result = getEntregas.executeQuery();

                entregasAvance = new ArrayList<>();
                while(result.next()){
                    EntregaAvance entrega = new EntregaAvance();
                       entrega.setIdEntregaAvance(result.getInt("identregaAvance"));
                       entrega.setIdAvance(result.getInt("idavance"));
                       entrega.setDescripEntrega(result.getString("descripcion"));
                       entrega.setIdEstudiante(result.getInt("idUsuario"));
                       entrega.setFechaEntrega(result.getDate("fechaEntrega"));
                       entrega.setCalificacion(result.getInt("calificacion"));
                       entrega.setNombreArchivo(result.getString("nombreArchivo"));
                       entrega.setArchivo(result.getBytes("archivo"));
                    entregasAvance.add(entrega);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            
        }
        return entregasAvance;
    }
    
    public static ArrayList<Usuario> getAlumnos(int idAvance) throws SQLException{
        ArrayList<Usuario> estudiantes = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT * FROM entregaavance LEFT JOIN avance ON entregaavance.idavance = avance.idavance " +
                                  "LEFT JOIN usuario ON entregaavance.idusuario = usuario.idusuario " +
                                  "WHERE avance.idavance = ?; ";
                PreparedStatement getEntregas = conexionBD.prepareStatement(sqlQuery);
                getEntregas.setInt(1, idAvance);
                ResultSet result = getEntregas.executeQuery();

                estudiantes = new ArrayList<>();
                while(result.next()){
                    Usuario alumno = new Usuario();
                       alumno.setIdUsuario(result.getInt("idusuario"));
                       alumno.setMatricula(result.getInt("matricula"));
                       alumno.setNombre(result.getString("nombre"));
                       alumno.setApellidoPaterno(result.getString("apellidoPaterno"));
                       alumno.setApellidoMaterno(result.getString("apellidoPaterno"));
                    estudiantes.add(alumno);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            
        }
        listaAlumnosEntregas = estudiantes;
        return estudiantes;
    }
    
    public static EntregaAvance getEntregaEstudiante(int idAlumno, int idAvance) throws SQLException{
        EntregaAvance entregaAlumno = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT * FROM entregaavance " +
                                  "INNER JOIN usuario ON entregaavance.idUsuario = usuario.idusuario " +
                                  "INNER JOIN avance ON entregaavance.idavance = avance.idavance " +
                                  "WHERE usuario.idUsuario = ? AND avance.idavance = ?;";
                PreparedStatement getAllUsers = conexionBD.prepareStatement(sqlQuery);
                getAllUsers.setInt(1, idAlumno);
                getAllUsers.setInt(2, idAvance);
                ResultSet result = getAllUsers.executeQuery();

                entregaAlumno = new EntregaAvance();
                if(result.next()){
                    entregaAlumno.setIdEntregaAvance(result.getInt("identregaAvance"));
                    entregaAlumno.setIdAvance(result.getInt("idavance"));
                    entregaAlumno.setDescripEntrega(result.getString("descripcion"));
                    entregaAlumno.setIdEstudiante(result.getInt("idusuario"));
                    entregaAlumno.setNombreEstudiante(result.getString("nombre"));
                    entregaAlumno.setFechaEntrega(result.getDate("fechaEntrega"));
                    entregaAlumno.setCalificacion(result.getInt("calificacion"));
                    entregaAlumno.setNombreArchivo(result.getString("nombreArchivo"));
                    entregaAlumno.setArchivo(result.getBytes("archivo"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }
        entregaActual = entregaAlumno;
        return entregaAlumno;
    }
    
    public static ArrayList<EntregaAvance> getEntregasAlumno(int idAlumno) throws SQLException{
        ArrayList<EntregaAvance> entregasEstudiantes = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT * FROM entregaavance INNER JOIN usuario ON "+
                                  "entregaavance.idUsuario = usuario.idusuario " + 
                                  "WHERE usuario.idUsuario = ?; ";
                PreparedStatement getAllUsers = conexionBD.prepareStatement(sqlQuery);
                getAllUsers.setInt(1, idAlumno);
                ResultSet result = getAllUsers.executeQuery();

                entregasEstudiantes = new ArrayList<>();
                while(result.next()){
                    EntregaAvance entrega = new EntregaAvance();
                       entrega.setIdEntregaAvance(result.getInt("identregaAvance"));
                       entrega.setIdAvance(result.getInt("idAvance"));
                       entrega.setDescripEntrega(result.getString("descripcion"));
                       entrega.setIdEstudiante(idAlumno);
                       entrega.setCalificacion(result.getInt("calificacion"));
                       entrega.setNombreEstudiante(result.getString("nombre"));
                    entregasEstudiantes.add(entrega);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }
        return entregasEstudiantes;
    }
    
    public static ResultadoOperacion calificarAvance(int idEntregaAvance, int calificacion) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,"Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String query = "UPDATE entregaavance SET calificacion = ? WHERE entregaavance.identregaAvance = ?; "; 
                PreparedStatement editar = conexionBD.prepareStatement(query);
                    editar.setInt(1, calificacion);
                    editar.setInt(2, idEntregaAvance);
                    
                int filasAfectadas = editar.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Calificacion completa completa");
                    resultado.setFilasAfectadas(filasAfectadas);
                }else{
                    resultado.setMensaje("Error al editar");
                }
            } catch (SQLIntegrityConstraintViolationException v) {
                ShowMessage.showAlertSimple(
                    "Error",
                    "Faltan datos",
                    Alert.AlertType.WARNING
                );
            }catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        } else {
            ShowMessage.showAlertSimple(
                "Error",
                "Sin conexión con la BD",
                Alert.AlertType.WARNING
            );
        }
        return resultado;
    }
}
