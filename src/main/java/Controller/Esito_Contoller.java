package Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;


public class Esito_Contoller extends Controller{

    @FXML private Button closeButton, minimizeButton, TornaAllaHome;

    @Override public void initialize() {
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        TornaAllaHome.setOnMouseClicked(this::TornaAllaHomeClicked);
    }

    private void TornaAllaHomeClicked(MouseEvent event) {
        Stage stage = (Stage)  TornaAllaHome.getScene().getWindow();
        stage.close();
        Node source = (Node)  event.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        VisualizzaSegnalazioni(primaryStage);
    }

    private void ButtonMinimizeClicked(MouseEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    private void ButtonCloseClicked(MouseEvent event) {
        Stage stage = (Stage)  closeButton.getScene().getWindow();
        stage.close();
        Node source = (Node)  event.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        VisualizzaSegnalazioni(primaryStage);
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
            Logger.getLogger(Esito_Contoller.class.toString()).severe("main_view loading failed");
        }
    }
}
