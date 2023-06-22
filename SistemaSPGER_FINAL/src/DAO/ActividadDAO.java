/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConexionBD.ConexionBD;
import ConexionBD.ResultadoOperacion;
import POJO.Actividad;
import POJO.Curso;
import Utils.ShowMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author Carmona
 */
public class ActividadDAO {
    public static Curso cursoActual = null;
    public static Actividad actividadActual = null;
    public static ArrayList<Actividad> listaActividades = null;
    
    public static ArrayList<Actividad> obtenerActividades(Curso curso) throws SQLException{
        ArrayList<Actividad> actividades = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idactividad, nombre, descripcion, valor, "
                        + "fechaInicio, fechaCierre, curso, archivo, nombreArchivo FROM actividad WHERE curso = ?";
                PreparedStatement prepararaConsulta = conexionBD.prepareStatement(consulta);
                prepararaConsulta.setInt(1,curso.getIdcurso());
                ResultSet resultado = prepararaConsulta.executeQuery();
                actividades = new ArrayList<>();
                while (resultado.next()) {                    
                    Actividad actividad = new Actividad();
                    actividad.setIdActividad(resultado.getInt("idactividad"));
                    actividad.setNombre(resultado.getString("nombre"));
                    actividad.setDescripcion(resultado.getString("descripcion"));
                    actividad.setValor(resultado.getInt("valor"));
                    actividad.setFechaInicio(resultado.getDate("fechaInicio"));
                    actividad.setFechaCierre(resultado.getDate("fechaCierre"));
                    actividad.setCurso(resultado.getInt("curso"));
                    actividad.setArchivo(resultado.getBytes("archivo"));
                    actividad.setNombreArchivo(resultado.getString("nombreArchivo"));
                    actividades.add(actividad);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        listaActividades = actividades;
        return actividades;
    }
    
    public static ResultadoOperacion crearActividad(Actividad actividad) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "ERROR", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "INSERT INTO actividad(nombre, descripcion, valor, fechaInicio,"
                        + " fechaCierre, curso, archivo, nombreArchivo) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement setActividad = conexionBD.prepareStatement(query);
                setActividad.setString(1, actividad.getNombre());
                setActividad.setString(2, actividad.getDescripcion());
                setActividad.setInt(3, actividad.getValor());
                setActividad.setDate(4, actividad.getFechaInicio());
                setActividad.setDate(5, actividad.getFechaCierre());
                setActividad.setInt(6, actividad.getCurso());
                setActividad.setBytes(7, actividad.getArchivo());
                setActividad.setString(8, actividad.getNombreArchivo());
                int filasAfectadas = setActividad.executeUpdate();
                System.out.println(filasAfectadas);
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Actividad creada");
                    resultado.setFilasAfectadas(filasAfectadas);
                } else {
                    resultado.setMensaje("ERROR");
                }
            } catch (SQLException e) {
                resultado.setMensaje(e.getMessage());
                e.printStackTrace();
                
            } finally {
                conexionBD.close();
            }
        } else {
            ShowMessage.showAlertSimple("ERROR",
                    "Son conexión con la BD",
                    Alert.AlertType.ERROR);
        }
        return resultado;
    }
    
    public static ResultadoOperacion actualizarActividad (Actividad actividad) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "ERROR DE CONEXIÓN",
                -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "UPDATE actividad SET nombre = ?, descripcion = ?, "
                        + "valor = ?, fechaInicio = ?, fechaCierre = ?, archivo = ?, nombreArchivo = ? "
                        + "WHERE idactividad = ?";
                PreparedStatement setActividad = conexionBD.prepareStatement(query);
                setActividad.setString(1, actividad.getNombre());
                setActividad.setString(2, actividad.getDescripcion());
                setActividad.setInt(3, actividad.getValor());
                setActividad.setDate(4, actividad.getFechaInicio());
                setActividad.setDate(5, actividad.getFechaCierre());
                setActividad.setBytes(6, actividad.getArchivo());
                setActividad.setString(7, actividad.getNombreArchivo());
                setActividad.setInt(8, actividad.getIdActividad());
                int filasAfectadas = setActividad.executeUpdate();
                if (actividad.getArchivo() != null) {
                    System.out.println("Hay un archivo");
                }else {
                    System.out.println("No hay un archivo");
                }
                System.out.println("Nombre de la actividad: " + actividad.getNombre());
                System.out.println("ID de la actividad: " + actividad.getIdActividad());
                System.out.println(filasAfectadas);
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Actividad modificada");
                    resultado.setFilasAfectadas(filasAfectadas);
                } else {
                    resultado.setMensaje(resultado.getMensaje());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return resultado;
    }
}
