package parcial2.Model.Implementacion2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FIFO {

    //Metodo que tiene como atributos la secuencia de paginas a procesar y la cantidad de marcos
    //para realizar la simulaccion con el algoritmo FIFO

    public Resultados simular(List<Integer> paginas, int numeroMarcos) {

        List<Integer> marcos = new ArrayList<>();   // lista que representa que paginas estan cargadas y cuantos marcos se encuentran libres
        Queue<Integer> colaFIFO = new LinkedList<>(); // lista que guarda el orden de llegada de las paginas
        List<DatosSimulacion> puntoGuardado = new ArrayList<>(); // lista para graficar
        int fallos = 0; //Representa la cantidad de fallos de la simulacion


        //Bucle que recorre todas las paginas de la lista
        for (int pagina : paginas) {

            if (marcos.contains(pagina)) {
                // la pagina ya estaba en memoria, no hay fallo no realiza nada se mantiene en el marco donde esta y
                // se gurdan los datos de donde se encuentra para poder mostrar en interfaz
                puntoGuardado.add(new DatosSimulacion(pagina, new ArrayList<>(marcos), false));
                continue;
            }

            fallos++; // la pagina no estaba en memoria o no esta cargda: se genera un fallo de pagina

            // si todavia todavia hay espacio libre o marcos sin utilizarr, no hace falta sacar a nadie solo se agrega la pagina
            if (marcos.size() < numeroMarcos) {
                marcos.add(pagina);
                colaFIFO.add(pagina);
            }
            // si no hay espacio: sacamos de la cola al que llego primero (FIFO) y ahora si agregamos la nueva pagina
            else {
                int paginaASacar = colaFIFO.poll();
                marcos.remove(Integer.valueOf(paginaASacar));
                marcos.add(pagina);
                colaFIFO.add(pagina);
            }
            // se gurdan los datos de donde se encuentra para poder mostrar en interfaz
            puntoGuardado.add(new DatosSimulacion(pagina, new ArrayList<>(marcos), true));
        }

        return new Resultados(fallos, puntoGuardado);
    }
}
