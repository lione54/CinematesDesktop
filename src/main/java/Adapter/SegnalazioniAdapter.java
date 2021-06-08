package Adapter;

import Controller.Dettagli_Controller;
import Controller.Esito_Contoller;
import Controller.Home_Controller;
import Controller.LogIn_Controller;
import Model.ModelDBInterno.DBModelSegnalazioniResponce;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kairos.layouts.RecyclerView;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SegnalazioniAdapter extends RecyclerView.Adapter<SegnalazioniAdapter.DataHolder> {

    @FXML
    private static String UtenteSegnalato, UtenteSegnalatore, Motivazione, TitoloFilm, TestoRecensione, IdSegnalazione;

    @Override public SegnalazioniAdapter.DataHolder onCreateViewHolder(FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(Home_Controller.class.getResource("/fxml/layout_segnalazioni.fxml"));
        return new DataHolder(fxmlLoader);
    }

    @Override public void onBindViewHolder(SegnalazioniAdapter.DataHolder dataHolder, Object o) {
            DBModelSegnalazioniResponce dbModelSegnalazioniResponce = (DBModelSegnalazioniResponce) o;
            if(dbModelSegnalazioniResponce.getFotoProfilo() != null){
                dataHolder.FotoProfilo.setImage(new Image(dbModelSegnalazioniResponce.getFotoProfilo()));
            }else{
                dataHolder.FotoProfilo.setImage(new Image("/images/man-shape.png"));
            }
            dataHolder.NomeUtenteSegnalato.setText(dbModelSegnalazioniResponce.getUserSegnalato());
            dataHolder.TitoloFilm.setText(dbModelSegnalazioniResponce.getTitolo_Film_Recensito());
            dataHolder.NomeUtenteSegnalatore.setText(dbModelSegnalazioniResponce.getUserSegnalatore());
            dataHolder.VaiAiDettagli.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    setUtenteSegnalato(dbModelSegnalazioniResponce.getUserSegnalato());
                    setUtenteSegnalatore(dbModelSegnalazioniResponce.getUserSegnalatore());
                    setTitoloFilm(dbModelSegnalazioniResponce.getTitolo_Film_Recensito());
                    setMotivazione(dbModelSegnalazioniResponce.getMotivazione());
                    setTestoRecensione(dbModelSegnalazioniResponce.getTesto_Recensione());
                    setIdSegnalazione(String.valueOf(dbModelSegnalazioniResponce.getId_Segnalazione()));
                    Stage stage = (Stage)  dataHolder.VaiAiDettagli.getScene().getWindow();
                    stage.close();
                    Node source = (Node)  event.getSource();
                    Stage primaryStage  = (Stage) source.getScene().getWindow();
                    VaiAiDettagli(primaryStage);
                }
            });
    }

    private void VaiAiDettagli(Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(Dettagli_Controller.class.getResource("/fxml/DettagliController.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newPrimaryStage = new Stage();
            newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
            newPrimaryStage.setScene(scene);
            newPrimaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(Home_Controller.class.toString()).severe("main_view loading failed");
        }
    }


    public class DataHolder extends RecyclerView.ViewHolder{

        @FXML private ImageView FotoProfilo;
        @FXML private Label NomeUtenteSegnalato;
        @FXML private Label TitoloFilm;
        @FXML private Label NomeUtenteSegnalatore;
        @FXML private Button VaiAiDettagli;

        public DataHolder(FXMLLoader loader) {
            super(loader);
        }
    }

    public static String getUtenteSegnalato() {
        return UtenteSegnalato;
    }

    public void setUtenteSegnalato(String myVariable) {
        this.UtenteSegnalato = myVariable;
    }

    public static String getUtenteSegnalatore() {
        return UtenteSegnalatore;
    }

    public void setUtenteSegnalatore(String myVariable) {
        this.UtenteSegnalatore = myVariable;
    }

    public static String getMotivazione() {
        return Motivazione;
    }

    public void setMotivazione(String myVariable) {
        this.Motivazione = myVariable;
    }

    public static String getTitoloFilm() {
        return TitoloFilm;
    }

    public void setTitoloFilm(String myVariable) {
        this.TitoloFilm = myVariable;
    }

    public static String getTestoRecensione() {
        return TestoRecensione;
    }

    public void setTestoRecensione(String myVariable) {
        this.TestoRecensione = myVariable;
    }

    public static String getIdSegnalazione() {
        return IdSegnalazione;
    }

    public void setIdSegnalazione(String myVariable) {
        this.IdSegnalazione = myVariable;
    }
}
