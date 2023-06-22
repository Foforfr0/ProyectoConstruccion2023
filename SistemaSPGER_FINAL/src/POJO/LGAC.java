/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

/**
 *
 * @author Carmona
 */
public class LGAC {
    private int idLGAC;
    private String nombre;
    private String descripcion;
    
    public LGAC(){
        
    }
    
    public int getIdLGAC(){
        return idLGAC;
    }
    public void setIdLGAC(int idLGAC){
        this.idLGAC = idLGAC;
    }
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    @Override
    public String toString() {
        return nombre;
    }  
}
