package Controller;

import ModelData.DBModelSegnalazioni;
import com.goebl.david.Webb;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.EventObject;
import java.util.logging.Logger;

public class CambiaPassword_Controller extends Controller{

    @FXML private Button closeButton;
    @FXML private Button minimizeButton, ConfermaButton, AnnullaButton;
    @FXML private Hyperlink LogOutLink;
    @FXML private Hyperlink VisualizzaSegnalazioniLink;
    @FXML private Hyperlink CambiaPasswdLink;
    @FXML private Label NomeAdminLabel, ErrorePassword;
    @FXML private ImageView FotoProfiloAdmin;
    @FXML private PasswordField VecchiaPassword, NuovaPassword, ConfermaPassword;

    @Override public void initialize() {
        NomeAdminLabel.setText(NomeAdminLabel.getText() + LogIn_Controller.getNomeAdmin());
        FotoProfiloAdmin.setImage(new Image("http://192.168.1.9/cinematesdb/" +  LogIn_Controller.getFotoAdmin()));
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        VisualizzaSegnalazioniLink.setOnMouseClicked(this::VisualizzaSegnalazioniLinkClicked);
        CambiaPasswdLink.setOnMouseClicked(this::CambiaPasswdLinkClicked);
        ConfermaButton.setOnMouseClicked(this::ConfermaCambiamentoPassClicked);
        AnnullaButton.setOnMouseClicked(this::AnnullaCambiamentoPassClicked);
        LogOutLink.setOnMouseClicked(this::LogOutLinkClicked);
    }

    @FXML private void AnnullaCambiamentoPassClicked(MouseEvent mouseEvent) {
        VecchiaPassword.clear();
        NuovaPassword.clear();
        ErrorePassword.setFont(Font.font("Calibri", 15));
        ErrorePassword.setTextFill(Color.RED);
        ErrorePassword.setText("Attenzione: La password non verra' aggiornata.");
        Stage stage = (Stage)   AnnullaButton.getScene().getWindow();
        stage.close();
        Node source = (Node)  mouseEvent.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        VisualizzaSegnalazioni(primaryStage);
    }

    @FXML private void ConfermaCambiamentoPassClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) ConfermaButton.getScene().getWindow();
        if(CambiaPassword().equals("Successfull")) {
            stage.close();
            Node source = (Node) mouseEvent.getSource();
            Stage primaryStage = (Stage) source.getScene().getWindow();
            CambiaPasswd(primaryStage);
        }
    }

    private String CambiaPassword() {
        String VecchiaPswd = VecchiaPassword.getText().toString();
        String NuovaPswd = NuovaPassword.getText().toString();
        String ConfermaPswd = ConfermaPassword.getText().toString();
        if(VecchiaPswd.length() == 0 || NuovaPswd.length() == 0 || ConfermaPswd.length() == 0){
            if (VecchiaPswd.length() == 0){
                ErrorePassword.setFont(Font.font("Calibri", 15));
                ErrorePassword.setTextFill(Color.RED);
                ErrorePassword.setText("Errore: Il campo Vecchia Password e' vuoto.");
                return "Error";
            }else if(NuovaPswd.length() == 0){
                ErrorePassword.setFont(Font.font("Calibri", 15));
                ErrorePassword.setTextFill(Color.RED);
                ErrorePassword.setText("Errore: Il campo Nuova Password e' vuoto.");
                return "Error";
            }else{
                ErrorePassword.setFont(Font.font("Calibri", 15));
                ErrorePassword.setTextFill(Color.RED);
                ErrorePassword.setText("Errore: Il campo Conferma Password e' vuoto.");
                return "Error";
            }
        }else if(VecchiaPswd.equals(NuovaPswd)){
            ErrorePassword.setFont(Font.font("Calibri", 15));
            ErrorePassword.setTextFill(Color.RED);
            ErrorePassword.setText("Errore: Le password non possono coincidere.");
            return "Error";
        }else if(NuovaPswd.length() <= 5){
            ErrorePassword.setFont(Font.font("Calibri", 15));
            ErrorePassword.setTextFill(Color.RED);
            ErrorePassword.setText("Errore: Password troppo breve deve essere almeno di 6 caratteri.");
            return "Error";
        }else if(!(ConfermaPswd.equals(NuovaPswd))){
            ErrorePassword.setFont(Font.font("Calibri", 15));
            ErrorePassword.setTextFill(Color.RED);
            ErrorePassword.setText("Errore: La password non coincide.");
            return "Error";
        }else{
            Webb webb = Webb.create();
            webb.post("http://192.168.1.9/cinematesdb/CambiaPasswdAdmin.php").param("Email_Admin", NomeAdminLabel.getText().toString()).param("Psw_Admin", ConfermaPswd).ensureSuccess().asVoid();
        }
        return "Successfull";
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
