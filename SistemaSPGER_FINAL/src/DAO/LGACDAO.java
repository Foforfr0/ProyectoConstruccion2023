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
import POJO.LGAC;
import ConexionBD.ConexionBD;

/**
 *
 * @author Carmona
 */
public class LGACDAO {
    public static ArrayList<LGAC> obtenerLGAC() throws SQLException{
        ArrayList<LGAC> LGAC = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idLGAC, nombre, descripcion FROM lgac";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultadoConsulta = prepararConsulta.executeQuery();
                LGAC = new ArrayList<>();
                while (resultadoConsulta.next()) {                    
                    LGAC lgac = new LGAC();
                    lgac.setIdLGAC(resultadoConsulta.getInt("idLGAC"));
                    lgac.setNombre(resultadoConsulta.getString("nombre"));
                    lgac.setDescripcion(resultadoConsulta.getString("descripcion"));
                    LGAC.add(lgac);
                }
            } catch (SQLException s) {
                s.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }
        return LGAC;
    }
    
    
}
