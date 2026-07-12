package parcial2.Model.Implementacion2;

import java.util.List;

/**
 * Clase simple que guarda el resultado de correr un algoritmo de
 * reemplazo de paginas: cuantos fallos hubo y como quedaron los marcos
 * en cada paso (para poder explicarlo visualmente en el video).
 */
public class Resultados {

    //Atributo que gurada el total de fallos de pagina durante la simulacion
    private final int totalFallos;
    //Atributo ppara mostrar el paso a paso de la simulacion
    private final List<DatosSimulacion> pasos;

    //Constructor
    public Resultados(int totalFallos, List<DatosSimulacion> pasos) {
        this.totalFallos = totalFallos;
        this.pasos = pasos;
    }

    //Getters
    public int getTotalFallos() {
        return totalFallos;
    }

    public List<DatosSimulacion> getpasos() {
        return pasos;
    }
}
