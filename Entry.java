public class Entry {
    private String nombreUsuario;
    private long posicion;
    private Entry siguiente;

    public Entry(String nombreUsuario, long posicion) {
        this.nombreUsuario = nombreUsuario;
        this.posicion = posicion;
        this.siguiente = null;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public long getPosicion() {
        return posicion;
    }

    public void setPosicion(long posicion) {
        this.posicion = posicion;
    }

    public Entry getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Entry siguiente) {
        this.siguiente = siguiente;
    }
}
