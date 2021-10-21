package Controller;

import Model.ModelDBInterno.*;
import RetrofitClient.RetrofitClientDBInterno;
import RetrofitService.RetrofitServiceDBInterno;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


public class LogIn_Controller extends Controller{

    @FXML private Button closeButton, minimizeButton, LogInButton, AnnullaButton;
    @FXML private TextField Email;
    @FXML private PasswordField Passwd;
    @FXML private CheckBox Ricordami;
    @FXML private Label Error;
    @FXML private Hyperlink CliccaQui;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

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
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(Welcome_Controller.getUsername() != null){
            if(Welcome_Controller.getPasswd() != null){
                Email.setText(Welcome_Controller.getUsername());
                Passwd.setText(Welcome_Controller.getPasswd());
            }
        }
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        LogInButton.setOnMouseClicked(this::LogInButtonClicked);
        AnnullaButton.setOnMouseClicked(this::AnnullaButtonClicked);
        CliccaQui.setOnMouseClicked(this::CliccaQuiButtonClicked);
    }

    private void CliccaQuiButtonClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage)  CliccaQui.getScene().getWindow();
        stage.close();
        Node source = (Node)  mouseEvent.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        RecuperaPasswd(primaryStage);
    }

    private void RecuperaPasswd(Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(LogIn_Controller.class.getResource("/fxml/RecuperaPasswd.fxml"));
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

    @FXML private void ButtonCloseClicked(MouseEvent event) {
        try {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            Platform.exit();
            System.exit(0);
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
        String email = Email.getText().toString();
        String Password = Passwd.getText().toString();
        if (!(email.matches("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}"))) {
            Error.setFont(Font.font("Calibri", 15));
            Error.setTextFill(Color.RED);
            Error.setText("Errore: Email Non Valida.");
        }else{
            LogIn(stage, event, email, Password);
        }
    }

    private void LogIn(Stage stage, MouseEvent event, String email, String password) {
        final String [] LogIn = {null};
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaEsistenzaAdmin(email);
        verificaCall.enqueue(new Callback<DBModelVerifica>() {
            @Override public void onResponse(@NotNull Call<DBModelVerifica> call,@NotNull Response<DBModelVerifica> response) {
                DBModelVerifica dbModelVerifica = response.body();
                if(dbModelVerifica != null) {
                    List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                    if(verificaResults.get(0).getCodVerifica() == 1) {
                        Call<DBModelVerifica> dbModelVerificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin(email, password);
                        dbModelVerificaCall.enqueue(new Callback<DBModelVerifica>() {
                            @Override public void onResponse(@NotNull Call<DBModelVerifica> call,@NotNull Response<DBModelVerifica> response) {
                                DBModelVerifica dbModelVerifica1 = response.body();
                                if(dbModelVerifica1 != null) {
                                    List<DBModelVerificaResults> verificaResultsList = dbModelVerifica1.getResults();
                                    if(verificaResultsList.get(0).getCodVerifica() == 1) {
                                        Call<DBModelFoto> fotoCall = retrofitServiceDBInterno.PrendiFotoAdmin(email, password);
                                        fotoCall.enqueue(new Callback<DBModelFoto>() {
                                            @Override public void onResponse(@NotNull Call<DBModelFoto> call,@NotNull Response<DBModelFoto> response) {
                                                DBModelFoto dbModelFoto = response.body();
                                                if(dbModelFoto != null){
                                                    List<DBModelFotoResponce> fotoResponces = dbModelFoto.getResult();
                                                    setNomeAdmin(email);
                                                    setFotoAdmin(fotoResponces.get(0).getFoto_Profilo());
                                                    LogIn[0] = "Successfull";
                                                    if(Ricordami.isSelected()){
                                                        RememberMe.Config(email, password);
                                                    }
                                                    Platform.runLater(new Runnable() {
                                                        @Override public void run() {
                                                            if(LogIn[0].equals("Successfull")) {
                                                                stage.close();
                                                                Node source = (Node) event.getSource();
                                                                Stage primaryStage = (Stage) source.getScene().getWindow();
                                                                HomeStage(primaryStage);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                            @Override public void onFailure(@NotNull Call<DBModelFoto> call,@NotNull Throwable t) {
                                                Error.setFont(Font.font("Calibri", 15));
                                                Error.setTextFill(Color.RED);
                                                Error.setText("Ops qualcosa e' andato storto.");
                                                LogIn[0] = "Error";
                                            }
                                        });
                                    }else {
                                        Error.setFont(Font.font("Calibri", 15));
                                        Error.setTextFill(Color.RED);
                                        Error.setText("Errore: Password Sbagliata.");
                                        LogIn[0] = "Error";
                                    }
                                }
                            }
                            @Override public void onFailure(@NotNull Call<DBModelVerifica> call,@NotNull Throwable t) {
                                Error.setFont(Font.font("Calibri", 15));
                                Error.setTextFill(Color.RED);
                                Error.setText("Ops qualcosa è andato storto.");
                                LogIn[0] = "Error";
                            }
                        });
                    }else {
                        Error.setFont(Font.font("Calibri", 15));
                        Error.setTextFill(Color.RED);
                        Error.setText("Errore: Admin non presente.");
                        LogIn[0] = "Error";
                    }
                }
            }
            @Override public void onFailure(@NotNull Call<DBModelVerifica> call,@NotNull Throwable t) {
                Error.setFont(Font.font("Calibri", 15));
                Error.setTextFill(Color.RED);
                Error.setText("Ops qualcosa è andato storto.");
                LogIn[0] = "Error";
            }
        });
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
            root =  FXMLLoader.load(Home_Controller.class.getResource("/fxml/HomeVisualizzaSegnalazioni.fxml"));
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
        AnchorPane root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = (AnchorPane) FXMLLoader.load(Welcome_Controller.class.getResource("/fxml/Welcome.fxml"));
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
