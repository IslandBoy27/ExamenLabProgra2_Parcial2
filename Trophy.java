public enum Trophy {
    PLATINO(5),
    ORO(3),
    PLATA(2),
    BRONCE(1);

    public final int puntos;

    Trophy(int puntos) {
        this.puntos = puntos;
    }

    public int getPuntos() {
        return puntos;
    }
}
