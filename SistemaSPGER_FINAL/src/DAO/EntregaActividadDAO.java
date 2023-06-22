/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConexionBD.ConexionBD;
import ConexionBD.ResultadoOperacion;
import POJO.Actividad;
import POJO.EntregaActividad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;


/**
 *
 * @author Carmona
 */
public class EntregaActividadDAO {
    public static ResultadoOperacion crearEntregaActividad(EntregaActividad entregaActividad) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error al crear la entrega", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "INSERT INTO entregaactividad(idactividad, descripcion, idusuario,"
                        + " fechaEntrega, archivo, nombreArchivo) VALUES (?,?,?,?,?,?)";
                PreparedStatement setEntrega = conexionBD.prepareCall(query);
                setEntrega.setInt(1, entregaActividad.getIdActividad());
                setEntrega.setString(2, entregaActividad.getDescripcion());
                setEntrega.setInt(3, entregaActividad.getIdUsuario());
                LocalDate localDate = entregaActividad.getFechaEntrega();
                ZoneId zoneId = ZoneId.systemDefault();
                Date fechaEntrega = Date.valueOf(localDate);
                setEntrega.setDate(4, fechaEntrega);
                setEntrega.setBytes(5, entregaActividad.getArchivo());
                setEntrega.setString(6, entregaActividad.getNombreArchivo());
                int filasAfectadas = setEntrega.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Entrega realizada");
                    resultado.setFilasAfectadas(filasAfectadas);
                } else {
                    resultado.setMensaje("ERROR");
                }
            } catch (SQLException e) {
                resultado.setMensaje(resultado.getMensaje());
            } finally {
                conexionBD.close();
            }
        }
        return resultado;
    }
    
    public static EntregaActividad obtenerEntrega(Actividad actividad) throws SQLException {
        EntregaActividad entrega = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "SELECT identregaactividad, idactividad, descripcion, "
                        + "idusuario, fechaEntrega, archivo, nombreArchivo, calificacion "
                        + "FROM entregaactividad WHERE idactividad = ?";
                PreparedStatement prepararaConsulta = conexionBD.prepareStatement(query);
                prepararaConsulta.setInt(1,actividad.getIdActividad());
                ResultSet resultado = prepararaConsulta.executeQuery();
                entrega = new EntregaActividad();
                while (resultado.next()) {                    
                    entrega.setIdEntregaActividad(resultado.getInt("identregaactividad"));
                    entrega.setIdActividad(resultado.getInt("idactividad"));
                    entrega.setDescripcion(resultado.getString("descripcion"));
                    entrega.setIdUsuario(resultado.getInt("idusuario"));
                    Date date = resultado.getDate("fechaEntrega");
                    Instant instant = Instant.ofEpochMilli(date.getTime());
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    LocalDate localDate = localDateTime.toLocalDate();
                    entrega.setFechaEntrega(localDate);
                    entrega.setArchivo(resultado.getBytes("archivo"));
                    entrega.setNombreArchivo(resultado.getString("nombreArchivo"));
                    entrega.setCalificacion(resultado.getInt("calificacion")); 
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return entrega;
    }
    
    public static ResultadoOperacion modificarEntrega(EntregaActividad entrega) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "ERROR DE CONEXIÓN",
                -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "UPDATE entregaactividad SET descripcion = ?, archivo = ?, "
                        + "nombreArchivo = ? WHERE identregaActividad = ?";
                PreparedStatement setEntrega = conexionBD.prepareStatement(query);
                setEntrega.setString(1, entrega.getDescripcion());
                setEntrega.setBytes(2, entrega.getArchivo());
                setEntrega.setString(3, entrega.getNombreArchivo());
                setEntrega.setInt(4, entrega.getIdEntregaActividad());
                int filasAfectadas = setEntrega.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Exito");
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
    
    public static ArrayList<EntregaActividad> obtenerEntregas(Actividad actividad) throws SQLException {
        ArrayList<EntregaActividad> entregas = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "SELECT identregaactividad, idactividad, descripcion, "
                        + "idusuario, fechaEntrega, archivo, nombreArchivo, calificacion "
                        + "FROM entregaactividad WHERE idactividad = ?";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(query);
                prepararConsulta.setInt(1, actividad.getIdActividad());
                ResultSet resultado = prepararConsulta.executeQuery();
                entregas = new ArrayList<>();
                while (resultado.next()) {                    
                    EntregaActividad entrega = new EntregaActividad();
                    entrega.setIdEntregaActividad(resultado.getInt("identregaactividad"));
                    entrega.setIdActividad(resultado.getInt("idactividad"));
                    entrega.setDescripcion(resultado.getString("descripcion"));
                    entrega.setIdUsuario(resultado.getInt("idusuario"));
                    Date date = resultado.getDate("fechaEntrega");
                    Instant instant = Instant.ofEpochMilli(date.getTime());
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    LocalDate localDate = localDateTime.toLocalDate();
                    entrega.setFechaEntrega(localDate);
                    entrega.setArchivo(resultado.getBytes("archivo"));
                    entrega.setNombreArchivo(resultado.getString("nombreArchivo"));
                    entrega.setCalificacion(resultado.getInt("calificacion"));
                    entregas.add(entrega);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return entregas;
    }
}
