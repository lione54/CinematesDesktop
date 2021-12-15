package Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Welcome_Controller extends Controller{

    @FXML private Button closeButton;
    @FXML private Button minimizeButton;
    @FXML private Button SignInButton;
    @FXML private Button LogInButton;
    @FXML private Label Citazione;

    private static String Username;
    private static String Passwd;

    public static String getUsername() {
        return Username;
    }

    public void setUsername(String myVariable) {
        this.Username = myVariable;
    }

    public static String getPasswd() {
        return Passwd;
    }

    public void setPasswd(String myVariable) {
        this.Passwd = myVariable;
    }

    @Override public void initialize() {
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        SignInButton.setOnMouseClicked(this::SignInButtonClicked);
        LogInButton.setOnMouseClicked(this::AccediButtonClicked);
    }

    @FXML private void ButtonCloseClicked(MouseEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

    @FXML private void ButtonMinimizeClicked(MouseEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML private void SignInButtonClicked(@NotNull MouseEvent event){
        Stage stage = (Stage) SignInButton.getScene().getWindow();
        stage.close();
        Node source = (Node) event.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();
        SignInStage(primaryStage);
    }

    private void SignInStage(@NotNull Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(SignIn_Controller.class.getResource("/fxml/SignIn.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newPrimaryStage = new Stage();
            newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
            newPrimaryStage.setScene(scene);
            newPrimaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(Welcome_Controller.class.toString()).severe("SignIn loading failed");
        }
    }

    @FXML private void AccediButtonClicked(@NotNull MouseEvent event) {
        File file = new File("Config.txt");
        Stage stage = (Stage) LogInButton.getScene().getWindow();
        stage.close();
        Node source = (Node) event.getSource();
        Stage primaryStage = (Stage) source.getScene().getWindow();
        if(file != null){
            RememberMe rememberMe = RememberMe.getInfo();
            setUsername(rememberMe.getUsername());
            setPasswd(rememberMe.getPasswd());
            LogInStage(primaryStage);
        }else{
            LogInStage(primaryStage);
        }
    }

    private void LogInStage(@NotNull Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(Welcome_Controller.class.getResource("/fxml/LogIn.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newPrimaryStage = new Stage();
            newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
            newPrimaryStage.setScene(scene);
            newPrimaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(Welcome_Controller.class.toString()).severe("LogIn loading failed");
        }
    }
}
