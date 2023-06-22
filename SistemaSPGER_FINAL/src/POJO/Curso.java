/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

/**
 *
 * @author Carmona
 */
public class Curso {
    private int idcurso;
    private String periodo;
    private String nombre;
    private int experienciaEducativa;
    private int idProfesor;

    public int getIdcurso() {
        return idcurso;
    }

    public void setIdcurso(int idcurso) {
        this.idcurso = idcurso;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getExperienciaEducativa() {
        return experienciaEducativa;
    }

    public void setExperienciaEducativa(int experienciaEducativa) {
        this.experienciaEducativa = experienciaEducativa;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }
    
    @Override
    public String toString(){
        return this.nombre;
    }
}
