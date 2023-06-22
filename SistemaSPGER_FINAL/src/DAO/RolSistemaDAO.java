/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import POJO.RolSistema;
import ConexionBD.ConexionBD;


/**
 *
 * @author lecap
 */
public class RolSistemaDAO {
    
    public static ArrayList<RolSistema> obtenerRolesSistema() throws SQLException{
        ArrayList<RolSistema> rolSistema = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idrolsistema, rolsistema FROM rolsistema";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultadoConsulta = prepararConsulta.executeQuery();
                rolSistema = new ArrayList<>();
                while(resultadoConsulta.next()){
                    RolSistema rol = new RolSistema();
                    rol.setIdRolSistema(resultadoConsulta.getInt("idrolsistema"));
                    rol.setRolSistema(resultadoConsulta.getString("rolsistema"));
                    rolSistema.add(rol);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }
        return rolSistema;
    }
    
}
