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
import java.util.List;
import javafx.scene.control.Alert;
import ConexionBD.ResultadoOperacion;
import ConexionBD.ConexionBD;
import Utils.ShowMessage;

/**
 *
 * @author lecap
 */

//Ugual podria ser una lista de los roles de ese usuario

public class RolesAcademicosDAO {
    
    public static List<Integer> getRolesAcademicos(int idUsuario) throws SQLException{
        List<Integer> rolesAcademicos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if(conexionBD != null){
            try {
                String sqlQuery = "SELECT idrolAcademico FROM usuariorolacademico WHERE idusuario = ?";
                PreparedStatement getRoles = conexionBD.prepareStatement(sqlQuery);
                getRoles.setInt(1, idUsuario);
                ResultSet resultado = getRoles.executeQuery();
                
                while(resultado.next()){
                    int idRolAcademico = resultado.getInt("idrolAcademico");
                    rolesAcademicos.add(idRolAcademico);
                    
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally{
                conexionBD.close();
            }
        }else{
            ShowMessage.showAlertSimple("Error", 
                    "Sin conexión con la base de datos", 
                    Alert.AlertType.ERROR); 
        }
        return rolesAcademicos;               
    }   
}
