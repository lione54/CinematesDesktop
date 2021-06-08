package Controller;

import Adapter.SegnalazioniAdapter;
import Model.ModelDBInterno.DBModelResponseToInsert;
import Model.ModelDBInterno.DBModelVerifica;
import RetrofitClient.RetrofitClientDBInterno;
import RetrofitService.RetrofitServiceDBInterno;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.logging.Logger;

public class Dettagli_Controller extends Controller{

    @FXML private Button closeButton, DeclinaButton, minimizeButton, AccettaButton;
    @FXML private Label UtenteSegnalato, UtenteSegnalatore, Motivazione, TitoloFilm, TestoRecensione;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override public void initialize() {
        UtenteSegnalato.setText(SegnalazioniAdapter.getUtenteSegnalato());
        UtenteSegnalatore.setText(SegnalazioniAdapter.getUtenteSegnalatore());
        Motivazione.setText(SegnalazioniAdapter.getMotivazione().replaceAll("-", " "));
        TitoloFilm.setText(SegnalazioniAdapter.getTitoloFilm());
        TestoRecensione.setText(SegnalazioniAdapter.getTestoRecensione());
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        DeclinaButton.setOnMouseClicked(this::DeclinaButtonClicked);
        AccettaButton.setOnMouseClicked(this::AccettaButtonClicked);
    }

    private void AccettaButtonClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage)  AccettaButton.getScene().getWindow();
        Call<DBModelResponseToInsert> accettaCall = retrofitServiceDBInterno.AccettaSegnalazione(SegnalazioniAdapter.getUtenteSegnalato(), SegnalazioniAdapter.getUtenteSegnalatore(), SegnalazioniAdapter.getIdSegnalazione());
        accettaCall.enqueue(new Callback<DBModelResponseToInsert>() {
            @Override public void onResponse(@NotNull Call<DBModelResponseToInsert> call,@NotNull Response<DBModelResponseToInsert> response) {
                DBModelResponseToInsert dbModelResponseToInsert = response.body();
                if(dbModelResponseToInsert != null){
                    if(dbModelResponseToInsert.getStato().equals("Successfull")){
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                stage.close();
                                Node source = (Node)  mouseEvent.getSource();
                                Stage primaryStage  = (Stage) source.getScene().getWindow();
                                AzioneRiuscita(primaryStage);
                            }
                        });
                    }
                }
            }
            @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call,@NotNull Throwable t) {

            }
        });
    }
    private void DeclinaButtonClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage)  DeclinaButton.getScene().getWindow();
        Call<DBModelResponseToInsert> declinaCall = retrofitServiceDBInterno.DeclinaSengnalazione(SegnalazioniAdapter.getUtenteSegnalato(), SegnalazioniAdapter.getUtenteSegnalatore(), SegnalazioniAdapter.getIdSegnalazione());
        declinaCall.enqueue(new Callback<DBModelResponseToInsert>() {
            @Override public void onResponse(@NotNull Call<DBModelResponseToInsert> call,@NotNull Response<DBModelResponseToInsert> response) {
                DBModelResponseToInsert dbModelResponseToInsert = response.body();
                if(dbModelResponseToInsert != null){
                    if(dbModelResponseToInsert.getStato().equals("Successfull")){
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                stage.close();
                                Node source = (Node)  mouseEvent.getSource();
                                Stage primaryStage  = (Stage) source.getScene().getWindow();
                                AzioneRiuscita(primaryStage);
                            }
                        });
                    }
                }
            }
            @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call,@NotNull Throwable t) {

            }
        });
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
