package Controller;

import Adapter.DBModelSegnalazioniListViewCell;
import ModelData.DBModelSegnalazioni;
import com.goebl.david.Webb;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.BitSet;
import java.util.logging.Logger;

public class Home_Controller extends Controller {

    @FXML private ListView<DBModelSegnalazioni> ListeSegnalazioni;
          private ObservableList<DBModelSegnalazioni> dbModelSegnalazioniObservableList = FXCollections.observableArrayList();
    @FXML private Button closeButton;
    @FXML private Button minimizeButton;
    @FXML private Hyperlink LogOutLink;
    @FXML private Hyperlink VisualizzaSegnalazioniLink;
    @FXML private Hyperlink CambiaPasswdLink;
    @FXML private Label NomeAdminLabel;
    @FXML private ImageView FotoProfiloAdmin;
          private String Image;
          private Webb webb = Webb.create();
          public static final String JSON_ARRAY = "dbdata";

    @Override public void initialize(){
        NomeAdminLabel.setText(NomeAdminLabel.getText() + LogIn_Controller.getNomeAdmin());
        try {
            JSONObject response = webb.post("http://192.168.178.48/cinematesdb/PrendiFotoProfiloAdimn.php").param("Email_Admin", NomeAdminLabel.getText().toString()).retry(1, false).asJsonObject().getBody();
            JSONArray array = response.getJSONArray(JSON_ARRAY);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Image = object.getString("Foto_Profilo");
            }
            if(!(Image.equals("null"))){
                FotoProfiloAdmin.setImage(new Image("http://192.168.178.48/cinematesdb/" +  Image));
            }else{
                FotoProfiloAdmin.setImage(new Image("/images/businessman.png"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Eventi();
        ListeSegnalazioni.setItems(CercaSegnalazioni());
        ListeSegnalazioni.setCellFactory(new Callback<ListView<DBModelSegnalazioni>, ListCell<DBModelSegnalazioni>>() {
            @Override public ListCell<DBModelSegnalazioni> call(ListView<DBModelSegnalazioni> param) {
                return new DBModelSegnalazioniListViewCell();
            }
        });
    }
    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        VisualizzaSegnalazioniLink.setOnMouseClicked(this::VisualizzaSegnalazioniLinkClicked);
        CambiaPasswdLink.setOnMouseClicked(this::CambiaPasswdLinkClicked);
        LogOutLink.setOnMouseClicked(this::LogOutLinkClicked);
        FotoProfiloAdmin.setOnMouseClicked(this::FotoProfiloClicked);

    }



    private void FotoProfiloClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage)  FotoProfiloAdmin.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File files = fileChooser.showOpenDialog(stage);
            try {
                if(files != null) {
                    URL url = files.toURI().toURL();
                    FotoProfiloAdmin.setImage(new Image(url.toExternalForm()));
                    byte[] fileContent = FileUtils.readFileToByteArray(files);
                    String encodedString = Base64.getEncoder().encodeToString(fileContent);
                    webb.post("http://192.168.178.48/cinematesdb/CambiaFotoProfiloAdimn.php").param("Email_Admin", NomeAdminLabel.getText().toString()).param("nome", encodedString).ensureSuccess().asVoid();
                    Node source = (Node) mouseEvent.getSource();
                    Stage primaryStage = (Stage) source.getScene().getWindow();
                    VisualizzaSegnalazioni(primaryStage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @FXML private void ButtonCloseClicked(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        Platform.setImplicitExit(true);
    }

    @FXML private void ButtonMinimizeClicked(MouseEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML private void VisualizzaSegnalazioniLinkClicked(MouseEvent event) {
        Stage stage = (Stage)  VisualizzaSegnalazioniLink.getScene().getWindow();
        stage.close();
        Node source = (Node)  event.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        VisualizzaSegnalazioni(primaryStage);
    }

    @FXML private void CambiaPasswdLinkClicked(MouseEvent event) {
        Stage stage = (Stage)  CambiaPasswdLink.getScene().getWindow();
        stage.close();
        Node source = (Node)  event.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        CambiaPasswd(primaryStage);
    }


    @FXML private void LogOutLinkClicked(MouseEvent event) {
        Stage stage = (Stage)  LogOutLink.getScene().getWindow();
        stage.close();
        Node source = (Node)  event.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        LogOut(primaryStage);
    }

    private void  VisualizzaSegnalazioni(@NotNull Stage primaryStage) {
        Parent root = null;
        dbModelSegnalazioniObservableList.clear();
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
            Logger.getLogger(Home_Controller.class.toString()).severe("main_view loading failed");
        }
    }



    private void  CambiaPasswd(@NotNull Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(CambiaPassword_Controller.class.getResource("/fxml/CambiaPasswd.fxml"));
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

    public ObservableList<DBModelSegnalazioni> CercaSegnalazioni() {
        Webb webb = Webb.create();
        JSONObject response = webb.post("http://192.168.178.48/cinematesdb/CercaSegnalazioni.php").retry(1, false).asJsonObject().getBody();
        try {
            JSONArray array = response.getJSONArray(JSON_ARRAY);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String str_id = object.getString("Id_Segnalazione");
                String UserSegnalato = object.getString("UserSegnalato");
                String UserSegnalatore = object.getString("UserSegnalatore");
                String Motivazione = object.getString("Motivazione");
                String TitoloFilm = object.getString("Titolo_Film");
                String TestoRecensione = object.getString("Testo_Recensione");
                String FotoProfilo = object.getString("Foto_Profilo");
                Integer Id_Segnalazione = Integer.valueOf(str_id);
                DBModelSegnalazioni dbModelSegnalazioni = new DBModelSegnalazioni(Id_Segnalazione, UserSegnalato, UserSegnalatore, Motivazione, TitoloFilm, TestoRecensione, FotoProfilo);
                dbModelSegnalazioniObservableList.add(dbModelSegnalazioni);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dbModelSegnalazioniObservableList;
    }

    private void  LogOut(@NotNull Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(LogOut_Controller.class.getResource("/fxml/LogOut.fxml"));
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
