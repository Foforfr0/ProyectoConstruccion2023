package ConexionBD;

public class ResultadoOperacion {
    private boolean error;
    private String mensaje;
    private int filasAfectadas;

    public ResultadoOperacion() {
    }

    public ResultadoOperacion(boolean error, String mensaje, int filasAfectadas) {
        this.error = error;
        this.mensaje = mensaje;
        this.filasAfectadas = filasAfectadas;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getFilasAfectadas() {
        return filasAfectadas;
    }

    public void setFilasAfectadas(int filasAfectadas) {
        this.filasAfectadas = filasAfectadas;
    }  
}
