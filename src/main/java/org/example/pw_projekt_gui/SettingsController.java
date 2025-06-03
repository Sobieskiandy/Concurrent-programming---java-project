package org.example.pw_projekt_gui;
import javafx.stage.Modality;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class SettingsController {
    @FXML
    protected void onDefaultButtonClick() {
        //welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    public void onRandomButtonClick(){
        new Losuje().losuj(HelloApplication.iloscZasobow, HelloApplication.iloscLiniiProdukcyjnych, HelloApplication.iloscMagazynowNaZasoby, HelloApplication.listaZasobow, HelloApplication.listaMagazynowNaZasoby, HelloApplication.listaLiniiProdukcyjnych, (Pane)HelloApplication.root);
        /*try {
            welcomeText.setText("Welcome to Settings!");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Ustawienia");
            settingsStage.setScene(new Scene(root, 640, 480));
            settingsStage.initModality(Modality.APPLICATION_MODAL);

            // Możesz też ustawić właściciela, jeśli chcesz (opcjonalnie):
            // settingsStage.initOwner(welcomeText.getScene().getWindow());

            settingsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    @FXML
    public void onSaveButtonClick() {
// Pobierz scenę z dowolnego elementu UI (np. przycisku)
        // i zamknij powiązane okno (Stage)
        HelloApplication.settingsStage.close();
    }
}