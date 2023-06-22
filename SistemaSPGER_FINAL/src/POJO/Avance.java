package POJO;

import java.sql.Date;

public class Avance {
    private int idAvance;
    private int numAvance;
    private String descripcion;
    private Date fechaCreacion; 
    private Date fechaInicio;
    private Date fechaCierre;
    private int curso;
    private String nombreCurso;
    private String nombreArchivo;
    private byte[] Archivo;

    public Avance(){}

    public Avance(int idAvance, int numAvance, String descripcion, Date fechaCreacion, Date fechaInicio, Date fechaCierre, int curso, String nombreCurso, String nombreArchivo, byte[] Archivo) {
        this.idAvance = idAvance;
        this.numAvance = numAvance;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
        this.curso = curso;
        this.nombreCurso = nombreCurso;
        this.nombreArchivo = nombreArchivo;
        this.Archivo = Archivo;
    }

    public int getIdAvance() {
        return idAvance;
    }

    public void setIdAvance(int idAvance) {
        this.idAvance = idAvance;
    }

    public int getNumAvance() {
        return numAvance;
    }

    public void setNumAvance(int numAvance) {
        this.numAvance = numAvance;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public byte[] getArchivo() {
        return Archivo;
    }

    public void setArchivo(byte[] Archivo) {
        this.Archivo = Archivo;
    }
    
    @Override
    public String toString(){
        return String.valueOf(this.numAvance);
    }
}
