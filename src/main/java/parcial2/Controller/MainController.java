package parcial2.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import parcial2.Model.Implementacion1.ResultadoTraduccion;
import parcial2.Model.Implementacion1.LogicaTraduccion;

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
    private TextArea areaResultados;

    @FXML
    private void calcularDireccionFisica() {
        try {
            areaResultados.setText(ejecutarTraduccionDeDirecciones());
        } catch (IllegalArgumentException e) {
            areaResultados.setText("=== IMPLEMENTACIÓN 1: Traductor de direcciones ===\n"
                    + "Error: " + e.getMessage());
        }
    }

    @FXML
    private void simularReemplazoDePaginas() {
    }

    private String ejecutarTraduccionDeDirecciones() {

        //Extrae el numero introducido en la casilla de Direccion virtual
        int direccionVirtual = Integer.parseInt(campoDireccionVirtual.getText().trim());
        //EXtrae el numero introducido en la casiila tamio de pagina
        int tamanoPagina = Integer.parseInt(campoTamanoPagina.getText().trim());

        // Creamos un objeto de logica de traduccion
        LogicaTraduccion mmu = new LogicaTraduccion(tamanoPagina);

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
        texto.append("Dirección física: ").append(resultado.getDireccionFisica()).append("\n");

        return texto.toString();
    }

    public void ejecutarSimulacion(){

    }
}


