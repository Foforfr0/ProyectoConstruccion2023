package POJO;

public class RolAcademico {
   private int idRolAcademico;
   private String rolAcademico;
   
   public RolAcademico(){
       
   }
   
   public int getidRolAacademico(){
       return idRolAcademico;
   }
   
   public void setIdRolAacademico(int idRolAcademico){
       this.idRolAcademico = idRolAcademico;
   }
   
   public String getRolAacademico(){
       return rolAcademico;
   }
   
   public void setRolAacademico(String rolAcademico){
       this.rolAcademico = rolAcademico;
   }
   
   @Override
    public String toString() {
        return rolAcademico;
    }
}
