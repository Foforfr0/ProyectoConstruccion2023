package POJO;

public class RolSistema {
    private int idRolSistema;
    private String rolSistema;
    
    public RolSistema(){
        
    };
    
    public RolSistema(String rolSistema){
        this.rolSistema = rolSistema;
    }
    
    public int getIdRolSistema(){
        return idRolSistema;
    }
    
    public void setIdRolSistema(int idRolSistema){
        this.idRolSistema = idRolSistema;
    }
    
    public String getRolSistema(){
        return rolSistema;
    }
    
    public void setRolSistema(String rolSistema){
        this.rolSistema = rolSistema;
    }
    
    @Override
    public String toString() {
        return rolSistema;
    }  
}
