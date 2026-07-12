package parcial2.Model.Implementacion1;

import java.util.HashMap;
import java.util.Map;

/**
 * Simula la tabla de paginas de un proceso.
 * Responsabilidad unica: guardar la relacion "numero de pagina -> marco fisico"
 * y decidir que marco nuevo asignar cuando una pagina no ha sido cargada todavia.
 */
public class TablaPaginas {

    // mapea o direcciona el número de página (parte de la dirección virtual) con el
    // marco físico o numero de la ram donde quedó guardada esa página
    private final Map<Integer, Integer> paginaAMarco;

    // contador que simula el siguiente espacio libre en ram o memoria fisica
    private int siguienteMarcoLibre;

    public TablaPaginas() {
        //Inicializamos los atributos
        this.paginaAMarco = new HashMap<>();
        this.siguienteMarcoLibre = 0;
    }
     // Verifica si la pagina ya tiene un marco asignado es decir si ya fuue trraducida para no volver a aser el calculo
    public boolean existePagina(int numeroPagina) {
        return paginaAMarco.containsKey(numeroPagina);
    }


      //Este metodo si el numero de pagina ya habia sido aisgnado a una pagina devuelve el valor al cual
      //esta conectado es decir el marrco de pagina. Si la pagina nunca se habia
      //pedido, esto ES un fallo de pagina: se le asigna el siguiente marco
      //libre de forma automatica

    public int obtenerMarco(int numeroPagina) {
        if (existePagina(numeroPagina)) {
            // si la pagina existe me retorna el numero del marco asignado
            return paginaAMarco.get(numeroPagina);
        }

        // fallo de pagina: la pagina no estaba mapeada, se le da un marco nuevo el que este libre
        // y aumentamos el contador de marcos libres
        int marcoAsignado = siguienteMarcoLibre;
        paginaAMarco.put(numeroPagina, marcoAsignado);
        siguienteMarcoLibre++;
        return marcoAsignado;
    }

    public Map<Integer, Integer> getPaginaAMarco() {
        return paginaAMarco;
    }
}
