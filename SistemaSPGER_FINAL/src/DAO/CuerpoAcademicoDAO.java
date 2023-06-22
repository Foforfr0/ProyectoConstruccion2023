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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Map;


/**
 *
 * @author Carmona
 */
public class CuerpoAcademicoDAO {
    public static ArrayList<CuerpoAcademico> obtenerCuerposAcademicos() throws SQLException{
        ArrayList<CuerpoAcademico> CA = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idcuerpoAcademico, clave, gradoConsodilacion,"
                        + "fechaRegistro, desAdscripcion, nombre FROM cuerpoAcademico";
                PreparedStatement prepararaConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararaConsulta.executeQuery(consulta);
                CA = new ArrayList<>();
                while (resultado.next()) {                    
                    CuerpoAcademico CuerpoAcademico = new CuerpoAcademico();
                    CuerpoAcademico.setIdcuerpoAcademico(resultado.getInt("idcuerpoAcademico"));
                    CuerpoAcademico.setClave(resultado.getString("clave"));
                    CuerpoAcademico.setGradoConsodilacion(resultado.getString("gradoConsodilacion"));
                    CuerpoAcademico.setFechaRegistro(resultado.getDate("fechaRegistro"));
                    CuerpoAcademico.setDesAdscripcion(resultado.getString("desAdscripcion"));
                    CuerpoAcademico.setNombre(resultado.getString("nombre"));
                    CA.add(CuerpoAcademico);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return CA;
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
                //get id from Responsable, and get ids from integrantes and insert rows
                ResultSet rs = setCA.getGeneratedKeys();
                int idCA=-1;
                while (rs.next()){
                    //idCA = rs.getInt("idacademicoCuerpoAcademico");
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
            //resultado.setMensaje("Sin conexión con la base de datos");
            throw new SQLException("Sin conexión con la base de datos");
        }
        return resultado;
    }
    
    public static ResultadoOperacion actualizarCA(CuerpoAcademico cuerpoAcademico, Map<Integer,String> usuarios) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion(true,
                "Error SQL", -1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                // 1.- Update cuerpoAcademico
                String sqlQueryCA = "update cuerpoAcademico set clave=?, gradoConsodilacion=?, fechaRegistro=?, "
                        + " desAdscripcion=?, nombre=? where idcuerpoAcademico = ?";
                PreparedStatement setCA = conexionBD.prepareStatement(sqlQueryCA);
                setCA.setString(1, cuerpoAcademico.getClave());
                setCA.setString(2, cuerpoAcademico.getGradoConsodilacion());
                setCA.setDate(3, cuerpoAcademico.getFechaRegistro());
                setCA.setString(4, cuerpoAcademico.getDesAdscripcion());
                setCA.setString(5, cuerpoAcademico.getNombre());
                setCA.setInt(6, cuerpoAcademico.getIdcuerpoAcademico());
                
                int caAffectedRows = setCA.executeUpdate();
                // 2.- Eliminar registros existentes de academicoCuerpoAcademico
                String sqlQueryEliminarACA = "delete from academicoCuerpoAcademico where idcuerpoAcademico = ?";
                PreparedStatement setEliminarACA = conexionBD.prepareStatement(sqlQueryEliminarACA,0);
                    setEliminarACA.setInt(1, cuerpoAcademico.getIdcuerpoAcademico());
                int resultDelete = setEliminarACA.executeUpdate();
                //Elimar al menos 1 Responsable y 1 Integrante
                if(resultDelete < 2) throw new SQLException("Academicos del Cuerpo Academico no pudieron ser eliminados");
                
                int acaAffectedRows = 0;
                String sqlQueryACA = "INSERT INTO academicoCuerpoAcademico(idusuario, idcuerpoAcademico , tipoIntegrante) "
                        + "VALUES (?,?,?)";
                for (Map.Entry<Integer,String> entry : usuarios.entrySet()){
                    //Obtener valores para ACA
                    AcademicoCuerpoAcademico academicoCuerpoAcademico = new AcademicoCuerpoAcademico();
                    academicoCuerpoAcademico.setIdusuario(entry.getKey());
                    academicoCuerpoAcademico.setIdcuerpoAcademico(cuerpoAcademico.getIdcuerpoAcademico());
                    academicoCuerpoAcademico.setTipoIntegrante(entry.getValue());
                    //PreparedStatement setACA = conexionBD.prepareStatement(sqlQueryACA);
                    PreparedStatement setACA = conexionBD.prepareStatement(sqlQueryACA,0);
                    setACA.setInt(1, academicoCuerpoAcademico.getIdusuario());
                    setACA.setInt(2, academicoCuerpoAcademico.getIdcuerpoAcademico());
                    setACA.setString(3, academicoCuerpoAcademico.getTipoIntegrante());
                    int resultcount = setACA.executeUpdate();
                    acaAffectedRows = acaAffectedRows + resultcount;
                }
                if(caAffectedRows > 0){
                    resultado.setError(false);
                    resultado.setMensaje("Cuerpo Academico actualizado correctamente.");
                    resultado.setFilasAfectadas(caAffectedRows + acaAffectedRows);
                }else{
                    resultado.setMensaje("No se pudo actualizar la información del Cuerpo Academico.");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                e.printStackTrace();
                throw new SQLException("El Cuerpo Academico no fue actualizado correctamente");
            }finally{
                conexionBD.close();
            }
        }else{
            
            throw new SQLException("Sin conexión con la base de datos");
        }
        return resultado;
    }
}
