module sio.plant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens sio.plant to javafx.fxml;
    exports sio.plant;
}