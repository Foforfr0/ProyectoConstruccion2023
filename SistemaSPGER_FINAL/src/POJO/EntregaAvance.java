package POJO;

import java.sql.Date;

public class EntregaAvance {
    private int idEntregaAvance;
    private int idAvance;
    private String descripEntrega;
    private int idEstudiante;
    private String nombreEstudiante;
    private Date fechaEntrega;
    private int numAvance;
    private String descripAvance;
    private int calificacion;
    private String nombreArchivo;
    private byte[] archivo;
    
    public EntregaAvance(){}

    public EntregaAvance(int idEntregaAvance, int idAvance, String descripcion, int idEstudiante, String nombreEstudiante, Date fechaEntrega, int numAvance, String descripAvance, int calificacion, String nombreArchivo, byte[] archivo) {
        this.idEntregaAvance = idEntregaAvance;
        this.idAvance = idAvance;
        this.descripEntrega = descripcion;
        this.idEstudiante = idEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.fechaEntrega = fechaEntrega;
        this.numAvance = numAvance;
        this.descripAvance = descripAvance;
        this.calificacion = calificacion;
        this.nombreArchivo = nombreArchivo;
        this.archivo = archivo;
    }

    public int getIdEntregaAvance() {
        return idEntregaAvance;
    }

    public void setIdEntregaAvance(int idEntregaAvance) {
        this.idEntregaAvance = idEntregaAvance;
    }

    public int getIdAvance() {
        return idAvance;
    }

    public void setIdAvance(int idAvance) {
        this.idAvance = idAvance;
    }

    public String getDescripEntrega() {
        return descripEntrega;
    }

    public void setDescripEntrega(String descripEntrega) {
        this.descripEntrega = descripEntrega;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public int getNumAvance() {
        return numAvance;
    }

    public void setNumAvance(int numAvance) {
        this.numAvance = numAvance;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }
    
    public void setCalificacion(int calificacion){
        this.calificacion = calificacion;
    }
    
    public int getCalificacion(){
        return this.calificacion;
    }
    
    public void setDescripAvance(String descripAvance) {
        this.descripAvance = descripAvance;
    }
    
}
