module parcial2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens parcial2 to javafx.fxml;
    opens parcial2.Controller to javafx.fxml;

    exports parcial2;
    exports parcial2.Controller;
}