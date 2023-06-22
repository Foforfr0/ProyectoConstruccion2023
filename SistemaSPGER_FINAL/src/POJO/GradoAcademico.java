/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package POJO;

/**
 *
 * @author lecap
 */
public class GradoAcademico {
    private int idGradoAcademico;
    private String gradoAcademico;
    
    public GradoAcademico(){
        
    }
    
    public int getIdGradoAcademico(){
        return idGradoAcademico;
    }
    
    public void setIdGradoAcademico(int IdGradoAcademico){
        this.idGradoAcademico = IdGradoAcademico;
    }
    
    public String getGradoAcademico(){
        return gradoAcademico;
    }
    
    public void setGradoAcademico(String gradoAcademico){
        this.gradoAcademico = gradoAcademico;
    }
    
    @Override
    public String toString() {
        return gradoAcademico;
    }
    
}
