package parcial2.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import parcial2.Model.Implementacion1.ResultadoTraduccion;
import parcial2.Model.Implementacion1.LogicaTraduccion;
import parcial2.Model.Implementacion2.*;

import java.util.ArrayList;
import java.util.List;


public class MainController {
    @FXML
    private TextField campoDireccionVirtual;

    @FXML
    private TextField campoTamanoPagina;

    @FXML
    private TextField campoNumeroMarcos;

    @FXML
    private TextField campoListaPaginas;

    @FXML
    private ComboBox<String> comboAlgoritmo;

    @FXML
    private TextArea areaResultados;

    @FXML
    private GridPane gridReemplazo;

    // guardamos el resultado de cada algoritmo por separado, asi cuando se
    // cambia de seleccion en el ComboBox no hay que volver a simular
    // solo redibujamos lo que ya calculamos
    private Resultados ultimoFIFO;
    private Resultados ultimoLRU;
    private Resultados ultimoOPT;
    private int ultimoNumeroMarcos = -1;

    // se guarda la LogicaTraduccion como atributo para que la
    // tabla de paginas persista entre traducciones, asi se pueden pedir varias
    // direcciones seguidas y ver como se va llenando la tabla
    private LogicaTraduccion mmu;
    private int ultimoTamanoPagina = -1;

    @FXML
    public void initialize() {
        comboAlgoritmo.getItems().addAll("FIFO", "LRU", "Óptimo (OPT)");
        comboAlgoritmo.setValue("FIFO");

        // cada vez que el usuario cambia la seleccion del combo, se vuelve a
        // dibujar la tabla, pero SIN recalcular nada
        comboAlgoritmo.valueProperty().addListener((obs, oldValue, newValue) -> {
            redibujarTablaSegunSeleccion();
        });
    }

    // Revisa cual algoritmo esta seleccionado en el ComboBox y dibuja la
    // tabla correspondiente

    private void redibujarTablaSegunSeleccion() {
        if (ultimoNumeroMarcos <= 0) return;

        String seleccion = comboAlgoritmo.getValue();
        if ("FIFO".equals(seleccion) && ultimoFIFO != null) {
            dibujarTablaDeReemplazo(ultimoFIFO, ultimoNumeroMarcos);
        } else if ("LRU".equals(seleccion) && ultimoLRU != null) {
            dibujarTablaDeReemplazo(ultimoLRU, ultimoNumeroMarcos);
        } else if ("Óptimo (OPT)".equals(seleccion) && ultimoOPT != null) {
            dibujarTablaDeReemplazo(ultimoOPT, ultimoNumeroMarcos);
        }
    }

    // Funcion que llama a la logica de la Implementacion 1 y muestra el resultado.
    @FXML
    private void calcularDireccionFisica() {
        try {
            areaResultados.setText(ejecutarTraduccionDeDirecciones());
        } catch (IllegalArgumentException e) {
            areaResultados.setText("=== IMPLEMENTACIÓN 1: Traductor de direcciones ===\n"
                    + "Error: " + e.getMessage());
        }
    }

    // Corre los 3 algoritmos con la misma secuencia de paginas, guarda sus
    // resultados y dibuja la tabla del algoritmo que este seleccionado.

    @FXML
    private void simularReemplazoDePaginas() {
        try {
            int numeroMarcos = Integer.parseInt(campoNumeroMarcos.getText().trim());
            if (numeroMarcos <= 0) {
                throw new IllegalArgumentException("El número de marcos debe ser mayor que 0.");
            }

            List<Integer> paginas = convertirTextoAListaDePaginas(campoListaPaginas.getText());

            ultimoFIFO = new FIFO().simular(paginas, numeroMarcos);
            ultimoLRU = new LRU().simular(paginas, numeroMarcos);
            ultimoOPT = new OPTR().simular(paginas, numeroMarcos);
            ultimoNumeroMarcos = numeroMarcos;

            StringBuilder texto = new StringBuilder();
            texto.append("=== IMPLEMENTACIÓN 2: Reemplazo de páginas ===\n");
            texto.append("FIFO -> ").append(ultimoFIFO.getTotalFallos()).append(" fallos de página\n");
            texto.append("LRU  -> ").append(ultimoLRU.getTotalFallos()).append(" fallos de página\n");
            texto.append("OPT  -> ").append(ultimoOPT.getTotalFallos()).append(" fallos de página\n");
            areaResultados.setText(texto.toString());

            redibujarTablaSegunSeleccion();

        } catch (IllegalArgumentException e) {
            areaResultados.setText("=== IMPLEMENTACIÓN 2: Reemplazo de páginas ===\n"
                    + "Error: " + e.getMessage());
        }
    }

    //Llena el GridPane con la tabla de tiempo/marcos/fallos de un resultado
    private void dibujarTablaDeReemplazo(Resultados resultado, int numeroMarcos) {
        gridReemplazo.getChildren().clear();

        List<DatosSimulacion> pasos = resultado.getpasos();

        gridReemplazo.add(new Label("Tiempo"), 0, 0);
        gridReemplazo.add(new Label("Solicitud"), 0, 1);
        for (int fila = 0; fila < numeroMarcos; fila++) {
            gridReemplazo.add(new Label("Marco " + fila), 0, 2 + fila);
        }
        gridReemplazo.add(new Label("Fallo"), 0, 2 + numeroMarcos);

        for (int columna = 0; columna < pasos.size(); columna++) {
            DatosSimulacion paso = pasos.get(columna);

            gridReemplazo.add(new Label("T" + columna), columna + 1, 0);
            gridReemplazo.add(new Label(String.valueOf(paso.getPaginaSolicitada())), columna + 1, 1);

            List<Integer> estadoDeMarcos = paso.getEstadoDeMarcos();
            for (int fila = 0; fila < numeroMarcos; fila++) {
                String textoMarco = fila < estadoDeMarcos.size()
                        ? String.valueOf(estadoDeMarcos.get(fila))
                        : "-";
                gridReemplazo.add(new Label(textoMarco), columna + 1, 2 + fila);
            }

            gridReemplazo.add(new Label(paso.isHuboFallo() ? "X" : ""), columna + 1, 2 + numeroMarcos);
        }
    }

    //Ejecuta la Implementacion 1: lee los campos, traduce la direccion
    //virtual y arma el texto con el resultado completo.
    private String ejecutarTraduccionDeDirecciones() {

        //Extrae el numero introducido en la casilla de Direccion virtual
        int direccionVirtual = Integer.parseInt(campoDireccionVirtual.getText().trim());
        //EXtrae el numero introducido en la casiila tamio de pagina
        int tamanoPagina = Integer.parseInt(campoTamanoPagina.getText().trim());

        // Si la MMU no ha sido creada, o el tamaño de página cambió, instanciamos una nueva
        if (mmu == null || tamanoPagina != ultimoTamanoPagina) {
            mmu = new LogicaTraduccion(tamanoPagina);
            ultimoTamanoPagina = tamanoPagina;
        }

        //Utilizamos el metodo donde esta la logica de traduccion para calcular los resultados
        ResultadoTraduccion resultado = mmu.traducirDirecciones(direccionVirtual);

        //Imprimimos los resultados calculados en el area de texto
        StringBuilder texto = new StringBuilder();
        texto.append("=== IMPLEMENTACIÓN 1: Traductor de direcciones (paginación) ===\n");
        texto.append("Dirección virtual: ").append(resultado.getDireccionVirtual()).append("\n");
        texto.append("Tamaño de página: ").append(tamanoPagina).append(" bytes\n");
        texto.append("Número de página: ").append(resultado.getNumeroPagina()).append("\n");
        texto.append("Desplazamiento (offset): ").append(resultado.getDesplazamiento()).append("\n");
        texto.append("Marco asignado: ").append(resultado.getMarco()).append("\n");
        texto.append("¿Fue fallo de página?: ").append(resultado.isHuboFallo() ? "SÍ" : "NO").append("\n");
        texto.append("Dirección física: ").append(resultado.getDireccionFisica()).append("\n\n");

        texto.append("=== TABLA DE PÁGINAS ACTUAL ===\n");
        texto.append("Página Virtual -> Marco Físico\n");
        mmu.getTablaPaginas().getPaginaAMarco().forEach((pag, marco) -> {
            texto.append("  Página ").append(pag).append("   ->   Marco ").append(marco).append("\n");
        });

        return texto.toString();
    }



    //Convierte el texto escrito por el usuario (ej. "7,0,1,2,0,3") en una lista de enteros
    private List<Integer> convertirTextoAListaDePaginas(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar al menos una página.");
        }

        List<Integer> paginas = new ArrayList<>();
        String[] partes = texto.split(",");

        for (String parte : partes) {
            String numeroLimpio = parte.trim();
            if (!numeroLimpio.isEmpty()) {
                paginas.add(Integer.parseInt(numeroLimpio));
            }
        }

        if (paginas.isEmpty()) {
            throw new IllegalArgumentException("La lista de páginas está vacía.");
        }

        return paginas;
    }

}


