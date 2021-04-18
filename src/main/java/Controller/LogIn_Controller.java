package Controller;

import com.goebl.david.Webb;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import java.io.IOException;
import java.util.logging.Logger;


public class LogIn_Controller extends Controller{

    @FXML private Button closeButton;
    @FXML private Button minimizeButton;
    @FXML private Button LogInButton;
    @FXML private Button AnnullaButton;
    @FXML private TextField Email;
    @FXML private PasswordField Passwd;
    @FXML private Label Error;
    public static final String JSON_ARRAY = "dbdata";

    private static String NomeAdmin;
    private static String FotoProf;

    public static String getNomeAdmin() {
        return NomeAdmin;
    }

    public void setNomeAdmin(String myVariable) {
        this.NomeAdmin = myVariable;
    }

    public static String getFotoAdmin() {
        return FotoProf;
    }

    public void setFotoAdmin(String myVariable) {
        this.FotoProf = myVariable;
    }


    @Override public void initialize() {
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        LogInButton.setOnMouseClicked(this::LogInButtonClicked);
        AnnullaButton.setOnMouseClicked(this::AnnullaButtonClicked);
    }

    @FXML private void ButtonCloseClicked(MouseEvent event) {
        try {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            Platform.setImplicitExit(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML private void ButtonMinimizeClicked(MouseEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML private void LogInButtonClicked(MouseEvent event){
        Stage stage = (Stage) LogInButton.getScene().getWindow();
        if(Log().equals("Successfull")) {
            stage.close();
            Node source = (Node) event.getSource();
            Stage primaryStage = (Stage) source.getScene().getWindow();
            HomeStage(primaryStage);
        }
    }

    private String Log() {
        String email = Email.getText().toString();
        String Password = Passwd.getText().toString();
        String Foto = null;
        final int[] validatiUser = new int[1];
        final int[] validatiPass = new int[1];
        Webb webb = Webb.create();
        if (!(email.matches("(.*)@(.*)"))) {
            Error.setFont(Font.font("Calibri", 15));
            Error.setTextFill(Color.RED);
            Error.setText("Errore: Email Non Valida.");
            return "Error";
        } else {
            JSONObject response = webb.post("http://192.168.1.9/cinematesdb/VerificaEsistenzaAdmin.php").param("Email_Admin", email).retry(1, false).asJsonObject().getBody();
            try {
                JSONArray arrayuser = response.getJSONArray(JSON_ARRAY);
                for (int i = 0; i < arrayuser.length(); i++) {
                    JSONObject object = arrayuser.getJSONObject(i);
                    String respo = object.getString("Esiste_Email_Admin");
                    if(respo.equals("1")){
                        Foto = object.getString("Foto_Profilo");
                    }
                    validatiUser[0] = Integer.parseInt(respo);
                }
                if (validatiUser[0] == 1) {
                    JSONObject response1 = webb.post("http://192.168.1.9/cinematesdb/VerificaPasswdAdmin.php").param("Psw_Admin", Password).retry(1, false).asJsonObject().getBody();
                    JSONArray arraypass = response1.getJSONArray(JSON_ARRAY);
                    for (int i = 0; i < arraypass.length(); i++) {
                        JSONObject object = arraypass.getJSONObject(i);
                        String respo = object.getString("Esiste_Passwd_Admin");
                        validatiPass[0] = Integer.parseInt(respo);
                    }
                    if (validatiPass[0] == 0) {
                        Error.setFont(Font.font("Calibri", 15));
                        Error.setTextFill(Color.RED);
                        Error.setText("Errore: Password Sbagliata.");
                        return "Error";
                    }
                } else {
                    Error.setFont(Font.font("Calibri", 15));
                    Error.setTextFill(Color.RED);
                    Error.setText("Errore: Admin Non Esistente");
                    return "Error";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setNomeAdmin(email);
        if(!(Foto.equals("null"))){
            setFotoAdmin(Foto);
        }
        return "Successfull";
    }

    @FXML private void AnnullaButtonClicked(MouseEvent event){
        try{
            Stage stage = (Stage) AnnullaButton.getScene().getWindow();
            stage.close();
            Node source = (Node)  event.getSource();
            Stage primaryStage  = (Stage) source.getScene().getWindow();
            WelcomeStage(primaryStage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void HomeStage(@NotNull Stage primaryStage) {
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
            Logger.getLogger(LogIn_Controller.class.toString()).severe("HomeStage Caricamento Fallito");
        }
    }

    private void WelcomeStage(@NotNull Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(Welcome_Controller.class.getResource("/fxml/Welcome.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newPrimaryStage = new Stage();
            newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
            newPrimaryStage.setScene(scene);
            newPrimaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(LogIn_Controller.class.toString()).severe("main_view loading failed");
        }
    }
}
