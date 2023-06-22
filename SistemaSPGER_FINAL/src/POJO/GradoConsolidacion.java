package POJO;

public class GradoConsolidacion {
    private int idGradoConsodilacion;
    private String gradoConsodilacion;
    
    public GradoConsolidacion(){
       
    }
    public GradoConsolidacion(int id, String gradoConsolidacion){
        this.idGradoConsodilacion = id;
        this.gradoConsodilacion = gradoConsolidacion;
    }
    
    public int getIdGradoConsodilacion(){
        return idGradoConsodilacion;
    }
    
    public void setIdGradoConsodilacion(int IdGradoAcademico){
        this.idGradoConsodilacion = IdGradoAcademico;
    }
    
    public String getGradoConsodilacion(){
        return gradoConsodilacion;
    }
    
    public void setGradoConsodilacion(String gradoConsodilacion){
        this.gradoConsodilacion = gradoConsodilacion;
    }
    
    @Override
    public String toString() {
        return gradoConsodilacion;
    }
    
}
