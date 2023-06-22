package DAO;

import ConexionBD.ConexionBD;
import ConexionBD.ResultadoOperacion;
import POJO.Avance;
import POJO.Curso;
import Utils.ShowMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

public class AvancesDAO {
    
    public static Curso cursoActual = null;
    public static ArrayList<Avance> avancesConsultadas = null;
    
    public static ArrayList<Avance> obtenerAvances(Curso curso) throws SQLException{
        ArrayList<Avance> avances = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String SQLQuery = "SELECT idavance, numAvance, descripcion, fechaCreacion, fechaInicio, fechaCierre, " +
                                  "Avance.curso, Curso.nombre AS nombreCurso, nombreArchivo, archivo " +
                                  "FROM Avance INNER JOIN Curso ON Avance.curso = Curso.idCurso " +
                                  "WHERE Curso.idcurso = ?; ";
                PreparedStatement prepararaConsulta = conexionBD.prepareStatement(SQLQuery);
                prepararaConsulta.setInt(1,curso.getIdcurso());
                ResultSet resultado = prepararaConsulta.executeQuery();
                
                avances = new ArrayList<>();
                while (resultado.next()) {                    
                    Avance avanceConsultado = new Avance();
                        avanceConsultado.setIdAvance(resultado.getInt("idavance"));
                        avanceConsultado.setNumAvance(resultado.getInt("numAvance"));
                        avanceConsultado.setDescripcion(resultado.getString("descripcion"));
                        avanceConsultado.setFechaCreacion(resultado.getDate("fechaCreacion"));
                        avanceConsultado.setFechaInicio(resultado.getDate("fechaInicio"));
                        avanceConsultado.setFechaCierre(resultado.getDate("fechaCierre"));
                        avanceConsultado.setCurso(resultado.getInt("curso"));
                        avanceConsultado.setNombreCurso(resultado.getString("nombreCurso"));
                        avanceConsultado.setNombreArchivo(resultado.getString("nombreArchivo"));
                        avanceConsultado.setArchivo(resultado.getBytes("archivo"));
                    avances.add(avanceConsultado);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        avancesConsultadas = avances;
        return avances;
    }
    
    public static ResultadoOperacion createAvance(Avance avanceNew, Curso cursoAct) throws SQLException{
        ResultadoOperacion response = new ResultadoOperacion(true, "Mensaje de error", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if(conexionBD != null){
            try {
                String sqlQuery = "INSERT INTO Avance (numAvance, descripcion, fechaCreacion, fechaInicio, " +
                                  "fechaCierre, curso, nombreArchivo, archivo) " +
                                  "VALUES (?,?,?,?,?,?,?,?); ";
                PreparedStatement setAnteproyecto = conexionBD.prepareStatement(sqlQuery);
                setAnteproyecto.setInt(1, avanceNew.getNumAvance());
                setAnteproyecto.setString(2, avanceNew.getDescripcion());
                setAnteproyecto.setDate(3, avanceNew.getFechaCreacion());
                setAnteproyecto.setDate(4, avanceNew.getFechaInicio()); 
                setAnteproyecto.setDate(5, avanceNew.getFechaCierre());
                setAnteproyecto.setInt(6, avanceNew.getCurso());
                setAnteproyecto.setString(7, avanceNew.getNombreArchivo());
                setAnteproyecto.setBytes(8, avanceNew.getArchivo());
                
                int filasAfectadas = setAnteproyecto.executeUpdate();
                if(filasAfectadas > 0){
                    response.setError(false);
                    response.setMensaje("Avance creado");
                    response.setFilasAfectadas(filasAfectadas);
                }else{
                    response.setMensaje("No se pudo crear el avance");
                }
            } catch (SQLException e) {
                response.setMensaje(e.getMessage());
            }  finally{
                conexionBD.close();
            }
        }else{
            response.setMensaje("Sin conexión con la base de datos");
        }
        return response;
    }
    
    public static ResultadoOperacion updateAvance(Avance editAvance) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,"Mensaje de error", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String SQLQuery = "UPDATE avance SET numAvance = ?, descripcion = ?, fechaInicio = ?, " +
                                  "fechaCierre = ?, nombreArchivo = ?, archivo = ? " +
                                  "WHERE idavance = ?; "; 
                PreparedStatement editar = conexionBD.prepareStatement(SQLQuery);
                editar.setInt(1, editAvance.getNumAvance());
                editar.setString(2, editAvance.getDescripcion());
                editar.setDate(3, editAvance.getFechaInicio());
                editar.setDate(4, editAvance.getFechaCierre());
                editar.setString(5, editAvance.getNombreArchivo());
                editar.setBytes(6, editAvance.getArchivo());
                editar.setInt(7, editAvance.getIdAvance());
                
                int affectedRows = editar.executeUpdate();
                if (affectedRows > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Edicion completa");
                    resultado.setFilasAfectadas(affectedRows);
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
    
    public static ResultadoOperacion deleteAvance(int idAvance) throws SQLException{
        ResultadoOperacion respuesta = new ResultadoOperacion(true, "Mensaje de error", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if(conexionBD != null){
            try {
                String sentencia = "DELETE FROM avance WHERE idavance = ?; ";                
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idAvance);
                
                int affectedRows = prepararSentencia.executeUpdate();
                if(affectedRows > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Alumno eliminado correctamente.");
                    respuesta.setFilasAfectadas(affectedRows);
                }else{
                    respuesta.setMensaje("No se pudo eliminar el avance.");
                }
            } catch (SQLException e) {
                respuesta.setMensaje(e.getMessage());
            } finally{
                conexionBD.close();
            }
        }else{
            respuesta.setMensaje("Por el momento no hay conexión con la base de datos...");
        }
        return respuesta;
    }
    
    public static boolean validarNumAvance(int numAvance, Curso cursoAct) throws SQLException{
        ArrayList<Avance> avances = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String SQLQuery = "SELECT * FROM Avance INNER JOIN Curso ON Avance.curso = Curso.idCurso " +
                                  "WHERE Curso.idcurso = ? AND Avance.numAvance = ?; ";
                PreparedStatement prepararaConsulta = conexionBD.prepareStatement(SQLQuery);
                    prepararaConsulta.setInt(1,cursoAct.getIdcurso());
                    prepararaConsulta.setInt(2,numAvance);
                ResultSet resultado = prepararaConsulta.executeQuery();
                
                avances = new ArrayList<>();
                while (resultado.next()) {                    
                    Avance avanceConsultado = new Avance();
                        avanceConsultado.setIdAvance(resultado.getInt("idavance"));
                    avances.add(avanceConsultado);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        
        if(avances.isEmpty()){
            return true;        //NO HAY AVANCE CON EL MISMO numAvance
        }else{
            return false;
        }
    }
    
    public static void setCursoActual(Curso curso){
        cursoActual = curso;
    }
}
