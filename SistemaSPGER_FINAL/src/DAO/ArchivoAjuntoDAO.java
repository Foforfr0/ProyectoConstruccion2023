/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import POJO.Anteproyecto;
import POJO.ArchivoAdjunto;
import ConexionBD.ResultadoOperacion;
import ConexionBD.ConexionBD;
/**
 *
 * @author lecap
 */
public class ArchivoAjuntoDAO {
    public static ArrayList<ArchivoAdjunto> obtenerPDFsAnteproyectos() throws SQLException{
        ArrayList<ArchivoAdjunto> lista = null;
        ResultadoOperacion respuesta = new ResultadoOperacion();
        respuesta.setError(true);
        respuesta.setFilasAfectadas(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        return lista;    
        //TODO
    }
    
    public static ResultadoOperacion guardarPDF(ArchivoAdjunto PDFAdjunto, Anteproyecto anteproyecto) throws SQLException{
        ResultadoOperacion respuesta = new ResultadoOperacion();
        respuesta.setError(true);
        respuesta.setFilasAfectadas(-1);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if (conexionBD != null) {
            try {
                String sqlQuery = "INSERT INTO archivoadjunto(nombre, archivo, idanteproyecto, "
                        + "tipoArchivo) VALUES(?,?,?,?)";
                PreparedStatement guardarArchivo = conexionBD.prepareStatement(sqlQuery);
                guardarArchivo.setString(1, PDFAdjunto.getNombrePDF());
                
                //Leer el archivo y convertirlo en bits
                byte[] archivoBytes = Files.readAllBytes(PDFAdjunto.getArchivo().toPath());
                guardarArchivo.setBytes(2, archivoBytes);
                guardarArchivo.setInt(3, anteproyecto.getIdAnteproyecto());
                System.out.print(anteproyecto.getIdAnteproyecto());
                guardarArchivo.setInt(4, 1);
                int filasAfectadas = guardarArchivo.executeUpdate(sqlQuery);
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Archivo agregado correctamente");
                    respuesta.setFilasAfectadas(filasAfectadas);
                }else{
                    respuesta.setMensaje("No se agrego el archivo");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (SQLException s){
                s.printStackTrace();
            }catch(NullPointerException n){
                n.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return respuesta;
    }
}
