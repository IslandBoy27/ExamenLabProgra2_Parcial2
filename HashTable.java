public class HashTable {
    private Entry cabeza;  // Apunta al primer elemento de la lista

    public HashTable() {
        this.cabeza = null;  // Inicialmente, la lista está vacía
    }

    // Función para agregar un nuevo elemento al final de la lista
    public void agregar(String nombreUsuario, long posicion) {
        Entry nuevoEntry = new Entry(nombreUsuario, posicion);

        // Si la lista está vacía, el nuevo elemento se convierte en el primero (cabeza)
        if (cabeza == null) {
            cabeza = nuevoEntry;
        } else {
            // Recorremos la lista hasta encontrar el último elemento
            Entry actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            // Agregamos el nuevo elemento al final
            actual.setSiguiente(nuevoEntry);
        }
    }

    // Función para remover un elemento que concuerde con el nombreUsuario
    public boolean remover(String nombreUsuario) {
        if (cabeza == null) {
            return false;  // La lista está vacía, no hay nada que eliminar
        }

        // Si el primer elemento (cabeza) es el que queremos eliminar
        if (cabeza.getNombreUsuario().equals(nombreUsuario)) {
            cabeza = cabeza.getSiguiente();  // Reasignamos cabeza al siguiente elemento
            return true;
        }

        // Recorremos la lista para encontrar el elemento a eliminar
        Entry actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getNombreUsuario().equals(nombreUsuario)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());  // Eliminamos el elemento
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;  // No se encontró el elemento
    }

    // Función para buscar un elemento por nombreUsuario y retornar su posición
    public long buscar(String nombreUsuario) {
        Entry actual = cabeza;

        // Recorremos la lista buscando el elemento con el nombre de usuario
        while (actual != null) {
            if (actual.getNombreUsuario().equals(nombreUsuario)) {
                return actual.getPosicion();  // Retornamos la posición si lo encontramos
            }
            actual = actual.getSiguiente();
        }

        return -1;  // Si no se encuentra, retornamos -1
    }
}
