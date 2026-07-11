package parcial2.Model.Implementacion1;

/**
 * Clase encargada de guardar el resultado de traducir una
 * direccion virtual a fisica. Se usa para poder mostrar todos los datos
 * en la interfaz sin tener que recalcular nada.
 */
public class ResultadoTraduccion {

    private final int direccionVirtual;
    private final int numeroPagina;
    private final int desplazamiento;
    private final int marco;
    private final int direccionFisica;
    private final boolean huboFallo;

    public ResultadoTraduccion(int direccionVirtual, int numeroPagina, int desplazamiento,
                               int marco, int direccionFisica, boolean huboFallo) {
        this.direccionVirtual = direccionVirtual;
        this.numeroPagina = numeroPagina;
        this.desplazamiento = desplazamiento;
        this.marco = marco;
        this.direccionFisica = direccionFisica;
        this.huboFallo = huboFallo;
    }

    public int getDireccionVirtual() {
        return direccionVirtual;
    }

    public int getNumeroPagina() {
        return numeroPagina;
    }

    public int getDesplazamiento() {
        return desplazamiento;
    }

    public int getMarco() {
        return marco;
    }

    public int getDireccionFisica() {
        return direccionFisica;
    }

    public boolean isHuboFallo() {
        return huboFallo;
    }
}
