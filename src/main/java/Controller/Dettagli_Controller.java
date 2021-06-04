package Controller;

import Adapter.DBModelSegnalazioniListViewCell;
import com.goebl.david.Webb;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

public class Dettagli_Controller extends Controller{

    @FXML private Button closeButton, DeclinaButton;
    @FXML private Button minimizeButton, AccettaButton;
    @FXML private Label UtenteSegnalato, UtenteSegnalatore, Motivazione, TitoloFilm, TestoRecensione;

    @Override public void initialize() {
        UtenteSegnalato.setText(DBModelSegnalazioniListViewCell.getUtenteSegnalato());
        UtenteSegnalatore.setText(DBModelSegnalazioniListViewCell.getUtenteSegnalatore());
        Motivazione.setText(DBModelSegnalazioniListViewCell.getMotivazione().replaceAll("-", " "));
        TitoloFilm.setText(DBModelSegnalazioniListViewCell.getTitoloFilm());
        TestoRecensione.setText(DBModelSegnalazioniListViewCell.getTestoRecensione());
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        DeclinaButton.setOnMouseClicked(this::DeclinaButtonClicked);
        AccettaButton.setOnMouseClicked(this::AccettaButtonClicked);
    }

    private void AccettaButtonClicked(MouseEvent mouseEvent) {
        Webb webb = Webb.create();
        webb.post("http://192.168.178.48/cinematesdb/AccettaSegnalazione.php").param("UtenteSegnalato", DBModelSegnalazioniListViewCell.getUtenteSegnalato()).param("UtenteSegnalatore", DBModelSegnalazioniListViewCell.getUtenteSegnalatore()).param("Id_Segnalazione", DBModelSegnalazioniListViewCell.getIdSegnalazione()).ensureSuccess().asVoid();
        Stage stage = (Stage)  AccettaButton.getScene().getWindow();
        stage.close();
        Node source = (Node)  mouseEvent.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        AzioneRiuscita(primaryStage);
    }
    private void DeclinaButtonClicked(MouseEvent mouseEvent) {
        Webb webb = Webb.create();
        webb.post("http://192.168.178.48/cinematesdb/DeclinaSengnalazione.php").param("UtenteSegnalato", DBModelSegnalazioniListViewCell.getUtenteSegnalato()).param("UtenteSegnalatore", DBModelSegnalazioniListViewCell.getUtenteSegnalatore()).param("Id_Segnalazione", DBModelSegnalazioniListViewCell.getIdSegnalazione()).ensureSuccess().asVoid();
        Stage stage = (Stage)  DeclinaButton.getScene().getWindow();
        stage.close();
        Node source = (Node)  mouseEvent.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        AzioneRiuscita(primaryStage);
    }

    private void AzioneRiuscita(Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(Esito_Contoller.class.getResource("/fxml/Esito.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newPrimaryStage = new Stage();
            newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
            newPrimaryStage.setScene(scene);
            newPrimaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(Dettagli_Controller.class.toString()).severe("main_view loading failed");
        }
    }


    @FXML private void ButtonCloseClicked(MouseEvent event) {
        Stage stage = (Stage)  closeButton.getScene().getWindow();
        stage.close();
        Node source = (Node)  event.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        VisualizzaSegnalazioni(primaryStage);
    }

    @FXML private void ButtonMinimizeClicked(MouseEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    private void  VisualizzaSegnalazioni(@NotNull Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(Home_Controller.class.getResource("/fxml/HomeVisualizzaSegnalazioni.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newPrimaryStage = new Stage();
            newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
            newPrimaryStage.setScene(scene);
            newPrimaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(Dettagli_Controller.class.toString()).severe("main_view loading failed");
        }
    }
}
