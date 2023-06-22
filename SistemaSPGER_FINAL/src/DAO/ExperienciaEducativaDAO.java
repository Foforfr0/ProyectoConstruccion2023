/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConexionBD.ConexionBD;
import POJO.ExperienciaEducativa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Carmona
 */
public class ExperienciaEducativaDAO {
    public static ArrayList<ExperienciaEducativa> obtenerExperienciasEducativas() throws SQLException{
        ArrayList<ExperienciaEducativa> experiencias = null;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idexperienciaEducativa, NRC, nombre,"
                        + " creditos FROM experienciaeducativa";
                PreparedStatement prepararConsulta = conexionBD.prepareStatement(consulta);
                ResultSet resultadoConsulta = prepararConsulta.executeQuery();
                experiencias = new ArrayList<>();
                while(resultadoConsulta.next()){
                    ExperienciaEducativa experiencia = new ExperienciaEducativa();
                    experiencia.setIdExperienciaEducativa(resultadoConsulta.getInt("idexperienciaEducativa"));
                    experiencia.setNRC(resultadoConsulta.getInt("NRC"));
                    experiencia.setNombre(resultadoConsulta.getString("nombre"));
                    experiencia.setCreditos(resultadoConsulta.getInt("creditos"));
                    experiencias.add(experiencia);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }
        return experiencias;
    }  
}
