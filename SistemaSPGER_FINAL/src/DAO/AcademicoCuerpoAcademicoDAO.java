/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import POJO.CuerpoAcademico;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import ConexionBD.ConexionBD;
import ConexionBD.ResultadoOperacion;
import POJO.AcademicoCuerpoAcademico;
import POJO.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Map;

/**
 *
 * @author Carmona
 */
public class AcademicoCuerpoAcademicoDAO {
    
    public static ArrayList<Usuario> obtenerAcademicosCuerpoAcademicoResponsable(Integer idcuerpoAcademico) throws SQLException{
        ArrayList<Usuario> usuariosBD = null;
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT u.idusuario, u.nombre, u.matricula, u.apellidoPaterno, u.apellidoMaterno, r.rolSistema "
                        + "AS tipoUsuario, u.rolSistema FROM usuario u INNER JOIN rolsistema r ON u.rolSistema = "
                        + "r.idrolSistema WHERE u.idusuario in ("
                        + "SELECT idusuario FROM academicoCuerpoAcademico where idcuerpoAcademico = ? and tipoIntegrante = 'Responsable')";
                PreparedStatement getACA = conexionBD.prepareStatement(sqlQuery);
                getACA.setInt(1, idcuerpoAcademico);
                ResultSet result = getACA.executeQuery();
                usuariosBD = new ArrayList<>();
                while(result.next()){
                    Usuario newUsuario = new Usuario();
                       newUsuario.setIdUsuario(result.getInt("u.idusuario"));
                       newUsuario.setMatricula(result.getInt("u.matricula"));
                       newUsuario.setNombre(result.getString("u.nombre"));
                       newUsuario.setApellidoPaterno(result.getString("u.apellidoPaterno"));
                       newUsuario.setApellidoMaterno(result.getString("u.apellidoMaterno"));
                       newUsuario.setPrueba(result.getString("tipoUsuario"));
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
    
    public static ArrayList<Usuario> obtenerAcademicosCuerpoAcademicoIntegrante(Integer idcuerpoAcademico) throws SQLException{
        ArrayList<Usuario> usuariosBD = null;
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT u.idusuario, u.nombre, u.matricula, u.apellidoPaterno, u.apellidoMaterno, r.rolSistema "
                        + "AS tipoUsuario, u.rolSistema FROM usuario u INNER JOIN rolsistema r ON u.rolSistema = "
                        + "r.idrolSistema WHERE u.idusuario in ("
                        + "SELECT idusuario FROM academicoCuerpoAcademico where idcuerpoAcademico = ? and tipoIntegrante = 'Integrante')";
                PreparedStatement getACA = conexionBD.prepareStatement(sqlQuery);
                getACA.setInt(1, idcuerpoAcademico);
                ResultSet result = getACA.executeQuery();
                usuariosBD = new ArrayList<>();
                while(result.next()){
                    Usuario newUsuario = new Usuario();
                       newUsuario.setIdUsuario(result.getInt("u.idusuario"));
                       newUsuario.setMatricula(result.getInt("u.matricula"));
                       newUsuario.setNombre(result.getString("u.nombre"));
                       newUsuario.setApellidoPaterno(result.getString("u.apellidoPaterno"));
                       newUsuario.setApellidoMaterno(result.getString("u.apellidoMaterno"));
                       newUsuario.setPrueba(result.getString("tipoUsuario"));
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
    
    public static ArrayList<Usuario> obtenerAcademicosCuerpoAcademico(Integer idcuerpoAcademico) throws SQLException{
        ArrayList<Usuario> usuariosBD = null;
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT u.idusuario, u.nombre, u.matricula, u.apellidoPaterno, u.apellidoMaterno, r.rolSistema "
                        + "AS tipoUsuario, u.rolSistema FROM usuario u INNER JOIN rolsistema r ON u.rolSistema = "
                        + "r.idrolSistema WHERE u.idusuario in ("
                        + "SELECT idusuario FROM academicoCuerpoAcademico where idcuerpoAcademico = ?";
                PreparedStatement getACA = conexionBD.prepareStatement(sqlQuery);
                getACA.setInt(1, idcuerpoAcademico);
                ResultSet result = getACA.executeQuery();
                usuariosBD = new ArrayList<>();
                while(result.next()){
                    Usuario newUsuario = new Usuario();
                       newUsuario.setIdUsuario(result.getInt("u.idusuario"));
                       newUsuario.setMatricula(result.getInt("u.matricula"));
                       newUsuario.setNombre(result.getString("u.nombre"));
                       newUsuario.setApellidoPaterno(result.getString("u.apellidoPaterno"));
                       newUsuario.setApellidoMaterno(result.getString("u.apellidoMaterno"));
                       newUsuario.setPrueba(result.getString("tipoUsuario"));
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
    
    public static ResultadoOperacion registrarCA(CuerpoAcademico cuerpoAcademico, Map<Integer,String> usuarios) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                // 1.- Update cuerpoAcademico
                String sqlQueryCA = "INSERT INTO cuerpoAcademico(clave, gradoConsodilacion , fechaRegistro , "
                        + " desAdscripcion, nombre) VALUES (?,?,?,?,?)";
                PreparedStatement setCA = conexionBD.prepareStatement(sqlQueryCA, Statement.RETURN_GENERATED_KEYS);
                setCA.setString(1, cuerpoAcademico.getClave());
                setCA.setString(2, cuerpoAcademico.getGradoConsodilacion());
                setCA.setDate(3, cuerpoAcademico.getFechaRegistro());
                setCA.setString(4, cuerpoAcademico.getDesAdscripcion());
                setCA.setString(5, cuerpoAcademico.getNombre());
                int caAffectedRows = setCA.executeUpdate();
                // 2.- Update academicoCuerpoAcademico
                ResultSet rs = setCA.getGeneratedKeys();
                int idCA=-1;
                while (rs.next()){
                    idCA = rs.getInt(1);
                    
                }
                if (idCA < 0) throw new SQLException("Error al leer valor autoincremental del registro insertado");
                int acaAffectedRows = 0;
                String sqlQueryACA = "INSERT INTO academicoCuerpoAcademico(idusuario, idcuerpoAcademico , tipoIntegrante) "
                        + "VALUES (?,?,?)";
                for (Map.Entry<Integer,String> entry : usuarios.entrySet()){
                    //Obtener valores para ACA
                    AcademicoCuerpoAcademico academicoCuerpoAcademico = new AcademicoCuerpoAcademico();
                    academicoCuerpoAcademico.setIdusuario(entry.getKey());
                    academicoCuerpoAcademico.setIdcuerpoAcademico(idCA);
                    academicoCuerpoAcademico.setTipoIntegrante(entry.getValue());
                    PreparedStatement setACA = conexionBD.prepareStatement(sqlQueryACA,0);
                    setACA.setInt(1, academicoCuerpoAcademico.getIdusuario());
                    setACA.setInt(2, academicoCuerpoAcademico.getIdcuerpoAcademico());
                    setACA.setString(3, academicoCuerpoAcademico.getTipoIntegrante());
                    int resultcount = setACA.executeUpdate();
                    acaAffectedRows = acaAffectedRows + resultcount;
                }
                if(caAffectedRows > 0){
                    resultado.setError(false);
                    resultado.setMensaje("Cuerpo Academico agregado correctamente.");
                    resultado.setFilasAfectadas(caAffectedRows + acaAffectedRows);
                }else{
                    resultado.setMensaje("No se pudo registrar la información del Cuerpo Academico.");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                e.printStackTrace();
                throw new SQLException("El Cuerpo Academico ya existe");
            }finally{
                conexionBD.close();
            }
        }else{
            throw new SQLException("Sin conexión con la base de datos");
        }
        return resultado;
    }
}
