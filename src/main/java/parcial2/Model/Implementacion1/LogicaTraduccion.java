package parcial2.Model.Implementacion1;

/**
 * Simula la Unidad de Administracion de Memoria (MMU).
 * Responsabilidad unica: recibir una direccion virtual y devolver la
 * direccion fisica correspondiente, apoyandose en la TablaPaginas para
 * saber en que marco quedo (o queda) cada pagina.
 */
public class LogicaTraduccion {

    //Creamos una tabla de paginas
    private final TablaPaginas tablaPaginas;
    //creamos el tamanio de paginas
    private final int tamanoPagina;

    public LogicaTraduccion(int tamanoPagina) {
        // validamos aqui porque sin un tamano de pagina valido no se puede
        // calcular nada el tamanio de pagina esta deefinido enn bytes por lo
        // que este no puede ser menor a cero y ademas debe de ser una potencia de dos
        if (tamanoPagina <= 0) {
            throw new IllegalArgumentException("El tamaño de página debe ser positivo.");
        }
        if (!tamanioPaginaEsPotenciaDeDos(tamanoPagina)) {
            throw new IllegalArgumentException("El tamaño de página debe ser potencia de 2 (ej. 4096).");
        }
        this.tamanoPagina = tamanoPagina;
        this.tablaPaginas = new TablaPaginas();
    }


    // Metodo encargado de traducir una direccion virtual a su direccion fisica
    // ase los calculos correspondientes
    public ResultadoTraduccion traducirDirecciones(int direccionVirtual) {
        //verifica que la direccion fisica sea un direccion correcta es decir que sea un numero positivo
        if (direccionVirtual < 0) {
            throw new IllegalArgumentException("La dirección virtual no puede ser negativa.");
        }

        // Paraca el calculo del numero de pagina lo que se ase es dividir la direccion virtual sobre el tamanio de pagina
        // que da como resulttado un entero
        int numeroPagina = direccionVirtual / tamanoPagina;

        // el desplazamiento se calcula como el residuo de la divicion de la direccion virtual sobre el tamanio de pagina
        // que da como resulttado un entero
        int desplazamiento = direccionVirtual % tamanoPagina;

        // si la pagina no existia en la tabla, esto cuenta como fallo de pagina
        // al inicio siempre va adar fallo de pagina ppoe que el mapa esta vacio o no hemos inicializado nada aun
        boolean huboFallo = !tablaPaginas.existePagina(numeroPagina);

        // utilizado para asignar el marco de pagina
        int marco = tablaPaginas.obtenerMarco(numeroPagina);

        // La dirección física se calcula multiplicando el marco por el tamaño de página
        // eso corre los bits del marco hacia la izquierda, dejando espacio vacío
        //  y luego sumando el desplazamiento que llena esos espacios vacios
        int direccionFisica = (marco * tamanoPagina) + desplazamiento;

        // Crea un objeto o encapsulamiento de los resultados del calculo de la traduciion fisica
        // para desppues ser mas facil mostrarlso en pantalla
        return new ResultadoTraduccion(direccionVirtual, numeroPagina, desplazamiento,
                marco, direccionFisica, huboFallo);
    }

    //Metodo para verificar que el tamanio de pagina introducida sea una potencia de dos
    private boolean tamanioPaginaEsPotenciaDeDos(int numero) {
        // calculo para verificar eso
        return (numero & (numero - 1)) == 0;
    }
}

