package upla.pe.model;

public class Reporte {
    String nombre;
    String tipo;
    String data;

    public Reporte(String nombre,String tipo, String data){
        this.nombre = nombre;
        this.tipo =tipo;
        this.data = data;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}
