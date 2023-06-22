/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConexionBD.ConexionBD;
import ConexionBD.ResultadoOperacion;
import POJO.Curso;
import POJO.Usuario;
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
public class CursoDAO {
    
    public static ResultadoOperacion crearCurso(Curso curso) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion(true, "ERROR", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "INSERT INTO curso(periodo, nombre, experienciaEducativa,"
                        + " idProfesor) VALUES (?,?,?,?)";
                PreparedStatement setCurso = conexionBD.prepareStatement(query);
                setCurso.setString(1, curso.getPeriodo());
                setCurso.setString(2, curso.getNombre());
                setCurso.setInt(3, curso.getExperienciaEducativa());
                setCurso.setInt(4, curso.getIdProfesor());
                int filasAfectadas = setCurso.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Curso creado");
                    resultado.setFilasAfectadas(filasAfectadas);
                } else {
                    resultado.setMensaje("Error al crear el curso");
                }
            } catch (SQLException e) {
                ShowMessage.showAlertSimple("ERROR",
                        "Error en la base de datos",
                        Alert.AlertType.ERROR);
                e.printStackTrace();
            } finally {
                conexionBD.close();;
            }
        } else {
            ShowMessage.showAlertSimple("Sin conexión",
                    "Sin conexión con la BD",
                    Alert.AlertType.ERROR);
        }
        return resultado;
    }
    
    public static ArrayList<Curso> obtenerCursos(int idUsuario) throws SQLException{
        ArrayList<Curso> cursos = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idcurso, periodo, nombre, experienciaEducativa, idProfesor"
                        + " FROM curso WHERE idProfesor = ?";
                PreparedStatement prepararaConsulta = conexionBD.prepareStatement(consulta);
                prepararaConsulta.setInt(1, idUsuario);
                ResultSet resultado = prepararaConsulta.executeQuery();
                cursos = new ArrayList<>();
                while (resultado.next()) {                    
                    Curso curso = new Curso();
                    curso.setIdcurso(resultado.getInt("idcurso"));
                    curso.setPeriodo(resultado.getString("periodo"));
                    curso.setNombre(resultado.getString("nombre"));
                    curso.setExperienciaEducativa(resultado.getInt("experienciaEducativa"));
                    curso.setIdProfesor(resultado.getInt("idProfesor"));
                    cursos.add(curso);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return cursos;
    }
    
    public static ArrayList<Curso> obtenerCursosEstudiante(Usuario usuario) throws SQLException{
        ArrayList<Curso> cursos = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT c.idcurso, c.periodo, c.nombre, c.experienciaEducativa "
                        + "FROM usuario u JOIN usuarioCurso uc ON u.idusuario = uc.idUsuario "
                        + "JOIN curso c ON uc.idCurso = c.idcurso WHERE u.idusuario = ?";
                PreparedStatement prepararaConsulta = conexionBD.prepareStatement(consulta);
                prepararaConsulta.setInt(1, usuario.getIdUsuario());
                ResultSet resultado = prepararaConsulta.executeQuery();
                cursos = new ArrayList<>();
                while (resultado.next()) {                    
                    Curso curso = new Curso();
                    curso.setIdcurso(resultado.getInt("idcurso"));
                    curso.setPeriodo(resultado.getString("periodo"));
                    curso.setNombre(resultado.getString("nombre"));
                    curso.setExperienciaEducativa(resultado.getInt("experienciaEducativa"));
                    //curso.setIdProfesor(resultado.getInt("idProfesor"));
                    cursos.add(curso);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return cursos;
    }
    
    public static ResultadoOperacion modificarCurso(Curso curso) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error al modificar el curso", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String query = "UPDATE curso SET periodo = ?, nombre = ?, "
                        + "experienciaEducativa = ? WHERE idcurso = ?";
                PreparedStatement modificar = conexionBD.prepareStatement(query);
                modificar.setString(1, curso.getPeriodo());
                modificar.setString(2, curso.getNombre());
                modificar.setInt(3, curso.getExperienciaEducativa());
                modificar.setInt(4, curso.getIdcurso());
                int filasAfectadas = modificar.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Curso modificado");
                    resultado.setFilasAfectadas(filasAfectadas);
                } else {
                    resultado.setMensaje("Error al modificar el curso");
                }
            } catch (SQLException ex) {
                //throw ex;
                ex.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                conexionBD.close();
            }
        } else {
            ShowMessage.showAlertSimple("Error",
                    "Sin conexión con la BD",
                    Alert.AlertType.ERROR);
        }
        return resultado;
    }
}
