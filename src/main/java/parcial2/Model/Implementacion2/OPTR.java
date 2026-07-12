package parcial2.Model.Implementacion2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OPTR {
    public Resultados simular(List<Integer> paginas, int numeroMarcos) {
        List<Integer> marcos = new ArrayList<>();      // Lista que representa que paginas estan cargadas y cuantos marcos se encuentran libres
        Queue<Integer> ordenDeEntrada = new LinkedList<>(); // solo se usa para desempatar (criterio FIFO)
        List<DatosSimulacion> puntoGuardado = new ArrayList<>(); //lista para graficar
        int fallos = 0; //cantidad de fallos del simulador

        for (int i = 0; i < paginas.size(); i++) {
            int pagina = paginas.get(i);

            // Si la pagina ya esta en un marco no ay fallos y se guarda el punto donde esta
            if (marcos.contains(pagina)) {
                puntoGuardado.add(new DatosSimulacion(pagina, new ArrayList<>(marcos), false));
                continue;
            }

            fallos++; // Aumenta fallo de pagina

            // Si todavia hay marcos disponibles se agrega la pagian aun marco
            if (marcos.size() < numeroMarcos) {
                marcos.add(pagina);
                ordenDeEntrada.add(pagina);
            }

            // Cuando no hay marcos libres
            // se llama al metodo que decide cual sacar segun cual se encuentra mas lejos del punto donde nos encontramos
            // se saca este y se agrega la nueva pagina
            else {
                int paginaASacar = buscarMasLejanaEnElFuturo(marcos, paginas, i, ordenDeEntrada);
                marcos.remove(Integer.valueOf(paginaASacar));
                ordenDeEntrada.remove(Integer.valueOf(paginaASacar));
                marcos.add(pagina);
                ordenDeEntrada.add(pagina);
            }

            // Se guarda el punto donde nos encontramos
            puntoGuardado.add(new DatosSimulacion(pagina, new ArrayList<>(marcos), true));
        }

        // Guardamos los resultados
        return new Resultados(fallos, puntoGuardado);
    }

    /**
     * Busca hacia adelante cual pagina (de las que estan en memoria) tarda
     * mas en volver a repetirse. Si nunca vuelve a aparecer, se considera
     * "infinito" y se saca de inmediato.
     */
    private int buscarMasLejanaEnElFuturo(List<Integer> marcos, List<Integer> paginas, int posicionActual, Queue<Integer> ordenDeEntrada) {
        int paginaElegida = -1;
        int distanciaMasLejana = -1;

        //revisamos la lista de paginas del marco mediante un bucle
        for (int paginaEnMarco : marcos) {
            int proximoUso = Integer.MAX_VALUE; // Si no vuelve a aparecer, queda como "mull" candidata para sacar

            //se busca en que punto se va a volver a pedir esa pagina
            for (int j = posicionActual + 1; j < paginas.size(); j++) {
                if (paginas.get(j) == paginaEnMarco) {
                    proximoUso = j;
                    break;
                }
            }
            //si la pagina que estamos revisando se tarda mas en volver a utilizarse que nuestro candidato actual a sacar este se convierte nuestro nuevo candidato
            if (proximoUso > distanciaMasLejana) {
                distanciaMasLejana = proximoUso;
                paginaElegida = paginaEnMarco;

            }
            //si ay empate entre dos candidato se elige el que llego primero (FIFO)
            else if (proximoUso == distanciaMasLejana) {
                if (entroAntes(paginaEnMarco, paginaElegida, ordenDeEntrada)) {
                    paginaElegida = paginaEnMarco;
                }
            }
        }

        //retornaos la pagina elegida
        return paginaElegida;
    }

    //cuadno se genera un emppate y no podemos decidir que proceso sacar usamos fifo
    //recorriendo la cola de entrada para saber cual de las dos paginas llego
    private boolean entroAntes(int paginaA, int paginaB, Queue<Integer> ordenDeEntrada) {
        for (int p : ordenDeEntrada) {
            if (p == paginaA) return true;
            if (p == paginaB) return false;
        }
        return false;
    }
}
