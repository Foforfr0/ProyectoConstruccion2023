/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

/**
 *
 * @author Andres Arellano Garcia
 */
public class AcademicoCuerpoAcademico {
    private int idacademicoCuerpoAcademico;
    private int idusuario;
    private int idcuerpoAcademico;
    private String tipoIntegrante;
    
    public AcademicoCuerpoAcademico(){
        
    }

    public int getIdacademicoCuerpoAcademico() {
        return idacademicoCuerpoAcademico;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public int getIdcuerpoAcademico() {
        return idcuerpoAcademico;
    }

    public String getTipoIntegrante() {
        return tipoIntegrante;
    }

    public void setIdacademicoCuerpoAcademico(int idacademicoCuerpoAcademico) {
        this.idacademicoCuerpoAcademico = idacademicoCuerpoAcademico;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public void setIdcuerpoAcademico(int idcuerpoAcademico) {
        this.idcuerpoAcademico = idcuerpoAcademico;
    }

    public void setTipoIntegrante(String tipoIntegrante) {
        this.tipoIntegrante = tipoIntegrante;
    }
}
