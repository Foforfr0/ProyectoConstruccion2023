package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import POJO.Anteproyecto;
import ConexionBD.ResultadoOperacion;
import ConexionBD.ConexionBD;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author lecap
 */
public class AnteproyectoDAO {
    
    public static ResultadoOperacion crearAnteproyecto(Anteproyecto nuevoAnteproyecto) throws SQLException{
        ResultadoOperacion response = new ResultadoOperacion();
        response.setError(true);
        response.setFilasAfectadas(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "INSERT INTO anteproyecto(nombre, estado, lineaInvestigacion, "
                        + "idLGAC, archivo, idDirector, idCodirector,"
                        + " nombreArchivo)"
                        + " VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement setAnteproyecto = conexionBD.prepareStatement(sqlQuery);
                setAnteproyecto.setString(1, nuevoAnteproyecto.getNombre());
                setAnteproyecto.setInt(2, nuevoAnteproyecto.getEstado());
                setAnteproyecto.setString(3, nuevoAnteproyecto.getLineaInvestigacion());
                setAnteproyecto.setInt(4, nuevoAnteproyecto.getIdLGAC()); 
                setAnteproyecto.setBytes(5, nuevoAnteproyecto.getArchivo());
                setAnteproyecto.setInt(6, nuevoAnteproyecto.getIdDirector());
                setAnteproyecto.setInt(7, nuevoAnteproyecto.getIdCoDirector());
                setAnteproyecto.setString(8, nuevoAnteproyecto.getNombreArchivo());
                int filasAfectadas = setAnteproyecto.executeUpdate();
                if(filasAfectadas > 0){
                    response.setError(false);
                    response.setMensaje("Anteproyecto creado");
                    response.setFilasAfectadas(filasAfectadas);
                }else{
                    response.setMensaje("Error al crear el anteproyecto");
                }
            } catch (NullPointerException ex) {
                //throw ex;
                System.out.println(ex.getMessage());
            } finally{
                conexionBD.close();
            }
        }else{
            response.setMensaje("Sin conexión con la base de datos");
        }
        return response;
    }
    
    public static ArrayList<Anteproyecto> obtenerAnteproyectos(int idUsuario) throws SQLException{
        ArrayList<Anteproyecto> anteproyectos = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idanteproyecto, nombre, estado, lineaInvestigacion,"
                        + "idLGAC, archivo, idDirector, idCodirector,nombreArchivo FROM anteproyecto WHERE idDirector = ?";
                PreparedStatement prepararaConsulta = conexionBD.prepareStatement(consulta);
                prepararaConsulta.setInt(1, idUsuario);
                ResultSet resultado = prepararaConsulta.executeQuery();
                anteproyectos = new ArrayList<>();
                while (resultado.next()) {                    
                    Anteproyecto anteproyecto = new Anteproyecto();
                    anteproyecto.setIdAnteproyecto(resultado.getInt("idanteproyecto"));
                    anteproyecto.setNombre(resultado.getString("nombre"));
                    anteproyecto.setEstado(resultado.getInt("estado"));
                    anteproyecto.setLineaInvestigacion(resultado.getString("lineaInvestigacion"));
                    anteproyecto.setIdLGAC(resultado.getInt("idLGAC"));
                    anteproyecto.setArchivo(resultado.getBytes("archivo"));
                    anteproyecto.setIdDirector(resultado.getInt("idDirector"));
                    anteproyecto.setIdCoDirector(resultado.getInt("idCodirector"));
                    anteproyecto.setNombreArchivo(resultado.getString("nombreArchivo"));
                    anteproyectos.add(anteproyecto);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return anteproyectos;
    }
    
        public static ResultadoOperacion postularAnteproyecto(Anteproyecto anteproyecto) throws SQLException{
        ResultadoOperacion response = new ResultadoOperacion(true,
                "Error", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "UPDATE anteproyecto set estado = 2 WHERE idanteproyecto = ?";
                PreparedStatement setEstado = conexionBD.prepareStatement(query);
                setEstado.setInt(1, anteproyecto.getIdAnteproyecto());
                int filasAfectadas = setEstado.executeUpdate();
                if (filasAfectadas > 0) {
                    response.setError(false);
                    response.setMensaje("Anteproyecto postulado");
                    response.setFilasAfectadas(filasAfectadas);
                } else {
                    response.setMensaje("ERROR");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return response;
    }

    public static ResultadoOperacion actualizarAnteproyecto(Anteproyecto anteproyecto) throws SQLException {
        ResultadoOperacion response = new ResultadoOperacion(true,
                "Error", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "UPDATE anteproyecto SET nombre = ?, estado = 1, lineaInvestigacion = ?, idLGAC = ?, archivo = ?, \n" +
                    "idDirector=?, idCodirector = ?,nombreArchivo = ? \n" +
                    " WHERE idAnteproyecto = ?; ";
                PreparedStatement setEstado = conexionBD.prepareStatement(query);
                setEstado.setString(1, anteproyecto.getNombre());
                setEstado.setString(2, anteproyecto.getLineaInvestigacion());
                setEstado.setInt(3, anteproyecto.getIdLGAC());
                setEstado.setBytes(4,anteproyecto.getArchivo());
                setEstado.setInt(5, anteproyecto.getIdDirector());
                setEstado.setInt(6, anteproyecto.getIdCoDirector());
                setEstado.setString(7, anteproyecto.getNombreArchivo());
                setEstado.setInt(8, anteproyecto.getIdAnteproyecto());
                int filasAfectadas = setEstado.executeUpdate();
                if (filasAfectadas > 0) {
                    response.setError(false);
                    response.setMensaje("Anteproyecto postulado");
                    response.setFilasAfectadas(filasAfectadas);
                } else {
                    response.setMensaje("ERROR");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return response;  
    }
}
