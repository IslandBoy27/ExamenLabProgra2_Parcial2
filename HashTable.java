public class HashTable {
    private Entry cabeza;  

    public HashTable() {
        this.cabeza = null;  
    }

    public void agregar(String nombreUsuario, long posicion) {
        Entry nuevoEntry = new Entry(nombreUsuario, posicion);
        if (cabeza == null) {
            cabeza = nuevoEntry;
        } else {
            
            Entry actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            
            actual.setSiguiente(nuevoEntry);
        }
    }

    public boolean remover(String nombreUsuario) {
        if (cabeza == null) {
            return false;  
        }

       
        if (cabeza.getNombreUsuario().equals(nombreUsuario)) {
            cabeza = cabeza.getSiguiente(); 
            return true;
        }

        Entry actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getNombreUsuario().equals(nombreUsuario)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente()); 
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;  
    }

    public long buscar(String nombreUsuario) {
        Entry actual = cabeza;

        while (actual != null) {
            if (actual.getNombreUsuario().equals(nombreUsuario)) {
                return actual.getPosicion();  
            }
            actual = actual.getSiguiente();
        }

        return -1; 
    }
}
