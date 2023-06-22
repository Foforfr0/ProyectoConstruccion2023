/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

import java.io.File;

/**
 *
 * @author lecap
 */
public class ArchivoAdjunto {
    private int idArchivoAdjunto;
    private String nombrePDF;
    private File archivo;
    private int idEntregaAvance;
    private int idEntregaActividad;
    private int idAnteproyecto;
    private int tipoArchivo;
    
    public ArchivoAdjunto(){
        
    }
    
    public int getIdArchivoAdjunto(){
        return idArchivoAdjunto;
    }
    
    public void setIdArchivoAdjunto(int idArchivoAdjunto){
        this.idArchivoAdjunto = idArchivoAdjunto;
    }
    
    public void setNombrePDF(String nombrePDF){
        this.nombrePDF = nombrePDF;
    }
    
    public String getNombrePDF(){
        return nombrePDF;
    }
    
    public File getArchivo(){
        return archivo;
    }
    
    public void setArchivo(File archivo){
        this.archivo = archivo;
    }
    
    public int getIdEntregaAvance(){
        return idEntregaAvance;
    }
    
    public void setIdEntregaAvance(int idEntregaAvance){
        this.idEntregaAvance = idEntregaAvance;
    }
    
    public int getIdEntregaActividad(){
        return idEntregaActividad;
    }
    
    public void setIdEntregaActividad(int idEntregaActividad){
        this.idEntregaActividad = idEntregaActividad;
    }
    
    public int getIdAnteproyecto(){
        return idAnteproyecto;
    }
    
    public void setIdAnteproyecto(int idAnteproyecto){
        this.idAnteproyecto = idAnteproyecto;
    }
    
    public int getTipoArchivo(){
        return tipoArchivo;
    }
    
    public void setTipoArchivo(int tipoArchivo){
        this.tipoArchivo = tipoArchivo;
    }
    
    @Override
    public String toString(){
        return this.nombrePDF;
    }
}
