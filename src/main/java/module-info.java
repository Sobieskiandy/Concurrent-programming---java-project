module org.example.pw_projekt_gui {
    requires javafx.controls;
    requires javafx.fxml;
    opens org.example.pw_projekt_gui to javafx.fxml;
    exports org.example.pw_projekt_gui;
}