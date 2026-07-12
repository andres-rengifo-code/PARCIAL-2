package parcial2.Model.Implementacion2;

import java.util.List;

public class DatosSimulacion {

    private final int paginaSolicitada;
    private final List<Integer> estadoDeMarcos;
    private final boolean huboFallo;

    public DatosSimulacion(int paginaSolicitada, List<Integer> estadoDeMarcos, boolean huboFallo) {
        this.paginaSolicitada = paginaSolicitada;
        this.estadoDeMarcos = estadoDeMarcos;
        this.huboFallo = huboFallo;
    }

    public int getPaginaSolicitada() {
        return paginaSolicitada;
    }

    public List<Integer> getEstadoDeMarcos() {
        return estadoDeMarcos;
    }

    public boolean isHuboFallo() {
        return huboFallo;
    }
}