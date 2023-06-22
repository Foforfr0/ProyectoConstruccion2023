package POJO;

public class Anteproyecto {
    private int idAnteproyecto;
    private String nombre;
    private int estado;
    private String lineaInvestigacion;
    private int idLGAC;
    private int idDirector;
    private int idCoDirector;
    private String nombreDirector;
    private String nombreCoDirector;
    private byte[] archivo;
    private String nombreArchivo;

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public Anteproyecto(){
    }

    public int getIdAnteproyecto() {
        return idAnteproyecto;
    }

    public void setIdAnteproyecto(int idAnteproyecto) {
        this.idAnteproyecto = idAnteproyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getLineaInvestigacion() {
        return lineaInvestigacion;
    }

    public void setLineaInvestigacion(String lineaInvestigacion) {
        this.lineaInvestigacion = lineaInvestigacion;
    }

    public int getIdLGAC() {
        return idLGAC;
    }

    public void setIdLGAC(int idLGAC) {
        this.idLGAC = idLGAC;
    }

    public int getIdDirector() {
        return idDirector;
    }

    public void setIdDirector(int idDirector) {
        this.idDirector = idDirector;
    }

    public int getIdCoDirector() {
        return idCoDirector;
    }

    public void setIdCoDirector(int idCoDirector) {
        this.idCoDirector = idCoDirector;
    }

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public String getNombreCoDirector() {
        return nombreCoDirector;
    }

    public void setNombreCoDirector(String nombreCoDirector) {
        this.nombreCoDirector = nombreCoDirector;
    }
    
    public byte[] getArchivo(){
        return archivo;
    }
    
    public void setArchivo(byte[] archivo){
        this.archivo = archivo;
    }
}
