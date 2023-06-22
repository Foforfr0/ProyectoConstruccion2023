/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

import java.sql.Date;

/**
 *
 * @author Carmona
 */
public class CuerpoAcademico {
    private int idcuerpoAcademico;
    private String clave;
    private String gradoConsodilacion;
    private Date fechaRegistro;
    private String desAdscripcion;
    private String nombre;
    
    public CuerpoAcademico(){
        
    }

    public int getIdcuerpoAcademico() {
        return idcuerpoAcademico;
    }

    public void setIdcuerpoAcademico(int idCuerpoAca) {
        this.idcuerpoAcademico = idCuerpoAca;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getGradoConsodilacion() {
        return gradoConsodilacion;
    }

    public void setGradoConsodilacion(String gradoConsodilacion) {
        this.gradoConsodilacion = gradoConsodilacion;
    }

    public Date getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDesAdscripcion() {
        return desAdscripcion;
    }

    public void setDesAdscripcion(String desAdscripcion) {
        this.desAdscripcion = desAdscripcion;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
