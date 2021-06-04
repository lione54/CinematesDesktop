package Adapter;

import Controller.Dettagli_Controller;
import Controller.Home_Controller;
import ModelData.DBModelSegnalazioni;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

public class DBModelSegnalazioniListViewCell extends ListCell<DBModelSegnalazioni> {
          private FXMLLoader mLLoader;
    @FXML public Label NomeUtenteSegnalatore, NomeUtenteSegnalato, Motivazioni;
    @FXML public ImageView FotoProfilo;
    @FXML public AnchorPane ItemSegnalazioni;
    @FXML public Button VaiAiDettagli;

    private static String Motivazione;
    private static String UtenteSegnalato;
    private static String UtenteSegnalatore;
    private static String TestoRecensione;
    private static String TitoloFilm;
    private static Integer IdSegnalazione;

    public static String getMotivazione() {
        return Motivazione;
    }

    public void setMotivazione(String myVariable) {
        this.Motivazione = myVariable;
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

    public static String getTestoRecensione() {
        return TestoRecensione;
    }

    public void setTestoRecensione(String myVariable) {
        this.TestoRecensione = myVariable;
    }

    public static String getTitoloFilm() {
        return TitoloFilm;
    }

    public void setTitoloFilm(String myVariable) {
        this.TitoloFilm = myVariable;
    }

    public static Integer getIdSegnalazione() {
        return IdSegnalazione;
    }

    public void setIdSegnalazione(Integer myVariable) {
        this.IdSegnalazione = myVariable;
    }

    @Override
    protected void updateItem(DBModelSegnalazioni dbModelSegnalazioni, boolean empty) {
        super.updateItem(dbModelSegnalazioni, empty);
        if(empty || dbModelSegnalazioni == null){
            setText(null);
            setGraphic(null);
        }else {
            if(mLLoader == null ){
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/layout_segnalazioni.fxml"));
                mLLoader.setController(this);
                try{
                    mLLoader.load();
                }catch (IOException e){
                    e.printStackTrace();
                }
                NomeUtenteSegnalatore.setText(dbModelSegnalazioni.getUserSegnalatore());
                NomeUtenteSegnalato.setText(dbModelSegnalazioni.getUserSegnalato());
                String Titolo = dbModelSegnalazioni.getTitolo_Film_Recensito().replaceAll("/","'");
                Motivazioni.setText(Titolo);
                FotoProfilo.setImage(new Image("http://192.168.178.48/cinematesdb/" + dbModelSegnalazioni.getFotoProfilo()));
                VaiAiDettagli.setOnAction(event -> VaiAidettagliClicked(getItem().getTitolo_Film_Recensito(),getItem().getUserSegnalato(),getItem().getUserSegnalatore(), getItem().getMotivazione(), getItem().getTesto_Recensione(), getItem().getId_Segnalazione(),event));
                setText(null);
                setGraphic(ItemSegnalazioni);
            }
        }
    }
    private void VaiAidettagliClicked(String titolo_film_recensito, String userSegnalato, String userSegnalatore, String motivazione, String testo_recensione, Integer id_segnalazione, ActionEvent event) {
        setMotivazione(motivazione);
        setUtenteSegnalato(userSegnalato);
        setUtenteSegnalatore(userSegnalatore);
        setTestoRecensione(testo_recensione);
        setTitoloFilm(titolo_film_recensito);
        setIdSegnalazione(id_segnalazione);
        Stage stage = (Stage)  VaiAiDettagli.getScene().getWindow();
        stage.close();
        Node source = (Node)  event.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();;
        CaricaIDettagli(primaryStage);
    }

    private void CaricaIDettagli(Stage primaryStage) {
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
}
