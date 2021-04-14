package Controller;


import com.goebl.david.Response;
import com.goebl.david.Webb;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Random;
import java.util.logging.Logger;



public class SignIn_Controller extends Controller {
    @FXML private Button closeButton;
    @FXML private Button minimizeButton;
    @FXML private Button ConfermaButton;
    @FXML private Button AnnullaButton;
    @FXML private TextField Email;
    @FXML private Label Error;
    public static final String JSON_ARRAY = "dbdata";

    @Override public void initialize(){
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        ConfermaButton.setOnMouseClicked(this::ConfermaButtonClicked);
        AnnullaButton.setOnMouseClicked(this::AnnullaButtonClicked);
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

    @FXML private void ConfermaButtonClicked(@NotNull Event event) {
        if (Sigin().equals("Successfull")) {
            Stage stage = (Stage) ConfermaButton.getScene().getWindow();
            stage.close();
            Node source = (Node) event.getSource();
            Stage primaryStage = (Stage) source.getScene().getWindow();
            ContinuaStage(primaryStage);
        }
    }

    @FXML private void AnnullaButtonClicked(@NotNull Event event){
        Stage stage = (Stage) AnnullaButton.getScene().getWindow();
        stage.close();
        Node source = (Node)  event.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        WelcomeStage(primaryStage);
    }



    private void WelcomeStage(@NotNull Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(SignIn_Controller.class.getResource("/fxml/Welcome.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newPrimaryStage = new Stage();
            newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
            newPrimaryStage.setScene(scene);
            newPrimaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(SignIn_Controller.class.toString()).severe("main_view loading failed");
        }
    }
    private void ContinuaStage(@NotNull Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(SignIn_Controller.class.getResource("/fxml/Continua.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newPrimaryStage = new Stage();
            newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
            newPrimaryStage.setScene(scene);
            newPrimaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(SignIn_Controller.class.toString()).severe("main_view loading failed");
        }
    }

    private String Sigin() {
        String email = Email.getText().toString();
        String Passwd = RandomPass(10).toString();
        final int[] validati = new int[1];
        Webb webb = Webb.create();
        if (!(email.matches("(.*)@(.*)"))) {
            Error.setFont(Font.font("Calibri", 15));
            Error.setTextFill(Color.RED);
            Error.setText("Errore: Email Non Valida.");
            return "Error";
        } else {
            JSONObject response = webb.post("http://192.168.1.9/cinematesdb/VerificaEsistenzaAdmin.php").param("Email_Admin", email).retry(1,false).asJsonObject().getBody();
            try {
                JSONArray array = response.getJSONArray(JSON_ARRAY);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String respo = object.getString("Esiste_Email_Admin");
                    validati[0] = Integer.parseInt(respo);
                }
                if(validati[0] == 0){
                    webb.post("http://192.168.1.9/cinematesdb/SignInAdmin.php").param("Email_Admin", email).param("Psw_Admin", Passwd).ensureSuccess().asVoid();
                }else{
                    Error.setFont(Font.font("Calibri", 15));
                    Error.setTextFill(Color.RED);
                    Error.setText("Errore: Admin Esistente");
                    return "Error";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "Successfull";
    }

    private String RandomPass(int length) {
        Random random = new Random();
        final int alphabetLength = 'Z' - 'A' + 1;
        StringBuilder result = new StringBuilder(length);
        while (result.length() < length) {
            final char charCaseBit = (char) (random.nextInt(2) << 5);
            result.append((char) (charCaseBit | ('A' + random.nextInt(alphabetLength))));
        }
        return result.toString();
    }
}
