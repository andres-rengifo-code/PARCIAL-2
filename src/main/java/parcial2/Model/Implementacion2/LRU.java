package parcial2.Model.Implementacion2;

import java.util.ArrayList;
import java.util.List;

public class LRU {

    public Resultados simular(List<Integer> paginas, int numeroMarcos) {
        List<Integer> marcos = new ArrayList<>(); //  lista que representa que paginas estan cargadas y cuantos marcos se encuentran libres
        List<DatosSimulacion> puntoGuardado = new ArrayList<>(); // lista para graficar
        int fallos = 0; // cantidad de fallos

        for (int i = 0; i < paginas.size(); i++) {
            int pagina = paginas.get(i); //extraemos la pagina en la que estamos

            //si la pagina ya esta en el marco no ay fallos y guardamos los datos
            //para graficar
            if (marcos.contains(pagina)) {
                puntoGuardado.add(new DatosSimulacion(pagina, new ArrayList<>(marcos), false));
                continue;
            }

            //Aumenntamos los fallos
            fallos++; 

            // si ay espacios libres lo agrgamos al marco libre
            if (marcos.size() < numeroMarcos) {
                marcos.add(pagina);
            }
            //Si no hay espacios libres sacamos la pagina que ah estdao mas tiempo en su marco y agregamos la nueva pagina
            else {
                int paginaASacar = buscarMenosRecienteUsada(marcos, paginas, i);
                marcos.remove(Integer.valueOf(paginaASacar));
                marcos.add(pagina);
            }
            //Guardamos los datos de esa pagina para la graficacion
            puntoGuardado.add(new DatosSimulacion(pagina, new ArrayList<>(marcos), true));
        }
        
        return new Resultados(fallos, puntoGuardado);
    }

    /**
     * Recorre el historial hacia atras (antes de la posicion actual) para
     * encontrar, de las paginas que estan en memoria, cual fue usada hace
     * mas tiempo. Esa es la que se debe sacar.
     */
    private int buscarMenosRecienteUsada(List<Integer> marcos, List<Integer> paginas, int posicionActual) {
        int paginaElegida = -1;
        int posicionMasAntigua = Integer.MAX_VALUE;

        //Recorre cada pagina que este en memoria
        for (int paginaEnMarco : marcos) {

            int ultimoUso = -1; 

            // Buscamos hacia atras la ultima vez que se uso la pagina que estamos evaluando
            for (int j = posicionActual - 1; j >= 0; j--) {
                if (paginas.get(j) == paginaEnMarco) {
                    ultimoUso = j;
                    break;
                }
            }

            // Compara si la pagina que estamos revisado se uso ase mas tiempo que la que tenemos como candidata de sacar
            // esta se convierte en la nueva candidata a sacar asi recorriendo el ciclo
            if (ultimoUso < posicionMasAntigua) {
                posicionMasAntigua = ultimoUso;
                paginaElegida = paginaEnMarco;
            }
        }

        //retorna la pagina elegida como candidata a sacar
        return paginaElegida;
    }

}
