package Controller;

import Model.ModelDBInterno.DBModelResponseToInsert;
import Model.ModelDBInterno.DBModelVerifica;
import Model.ModelDBInterno.DBModelVerificaResults;
import RetrofitClient.RetrofitClientDBInterno;
import RetrofitService.RetrofitServiceDBInterno;
import javafx.application.Platform;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public class CambiaPassword_Controller extends Controller{

    @FXML private Button closeButton;
    @FXML private Button minimizeButton, ConfermaButton, AnnullaButton;
    @FXML private Hyperlink LogOutLink;
    @FXML private Hyperlink VisualizzaSegnalazioniLink;
    @FXML private Hyperlink CambiaPasswdLink;
    @FXML private Hyperlink ConsigliaAgliUtenti;
    @FXML private Label NomeAdminLabel, ErrorePassword;
    @FXML private ImageView FotoProfiloAdmin;
    @FXML private PasswordField VecchiaPassword, NuovaPassword, ConfermaPassword;
    @FXML private Label ErroreSuccesso;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override public void initialize() {
        NomeAdminLabel.setText(NomeAdminLabel.getText() + LogIn_Controller.getNomeAdmin());
        if(LogIn_Controller.getFotoAdmin() != null){
            FotoProfiloAdmin.setImage(new Image(LogIn_Controller.getFotoAdmin()));
        }else{
            FotoProfiloAdmin.setImage(new Image("/images/businessman.png"));
        }
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
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
        FotoProfiloAdmin.setOnMouseClicked(this::FotoProfiloClicked);
        ConsigliaAgliUtenti.setOnMouseClicked(this::ConsigliaAgliUtentiClicked);
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
                Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.CambiaFotoProfiloAdimn(NomeAdminLabel.getText().toString(), encodedString);
                insertCall.enqueue(new retrofit2.Callback<DBModelResponseToInsert>() {
                    @Override public void onResponse(@NotNull Call<DBModelResponseToInsert> call,@NotNull Response<DBModelResponseToInsert> response) {
                        DBModelResponseToInsert dbModelResponseToInsert = response.body();
                        if(dbModelResponseToInsert != null){
                            if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                ErroreSuccesso.setFont(Font.font("Calibri", 15));
                                ErroreSuccesso.setTextFill(Color.GREEN);
                                ErroreSuccesso.setText("Foto cambiata con successo.");
                            }else{
                                ErroreSuccesso.setFont(Font.font("Calibri", 15));
                                ErroreSuccesso.setTextFill(Color.RED);
                                ErroreSuccesso.setText("Errore: La foto non verra' aggiornata.");
                            }
                        }else{
                            ErroreSuccesso.setFont(Font.font("Calibri", 15));
                            ErroreSuccesso.setTextFill(Color.RED);
                            ErroreSuccesso.setText("Errore: Impossibile aggiornare foto.");
                        }
                    }
                    @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call,@NotNull Throwable t) {
                        ErroreSuccesso.setFont(Font.font("Calibri", 15));
                        ErroreSuccesso.setTextFill(Color.RED);
                        ErroreSuccesso.setText("Ops qualcosa e' andato storto.");
                    }
                });
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        Node source = (Node) mouseEvent.getSource();
                        Stage primaryStage = (Stage) source.getScene().getWindow();
                        CambiaPasswd(primaryStage);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ConsigliaAgliUtentiClicked(MouseEvent event) {
        Stage stage = (Stage)  ConsigliaAgliUtenti.getScene().getWindow();
        stage.close();
        Node source = (Node)  event.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        Consiglia(primaryStage);
    }

    private void Consiglia(Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(ConsigliaAgliUtenti_Controller.class.getResource("/fxml/ConsigliaAgliUtenti.fxml"));
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

    @FXML private void AnnullaCambiamentoPassClicked(MouseEvent mouseEvent) {
        VecchiaPassword.clear();
        NuovaPassword.clear();
        ConfermaPassword.clear();
        ErrorePassword.setFont(Font.font("Calibri", 15));
        ErrorePassword.setTextFill(Color.RED);
        ErrorePassword.setText("Attenzione:\nLa password non\nverra' aggiornata.");
        Stage stage = (Stage)   AnnullaButton.getScene().getWindow();
        stage.close();
        Node source = (Node)  mouseEvent.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        VisualizzaSegnalazioni(primaryStage);
    }

    @FXML private void ConfermaCambiamentoPassClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) ConfermaButton.getScene().getWindow();
        String VecchiaPswd = VecchiaPassword.getText().toString();
        String NuovaPswd = NuovaPassword.getText().toString();
        String ConfermaPswd = ConfermaPassword.getText().toString();
        if(VecchiaPswd.length() == 0 || NuovaPswd.length() == 0 || ConfermaPswd.length() == 0){
            if (VecchiaPswd.length() == 0){
                ErrorePassword.setFont(Font.font("Calibri", 15));
                ErrorePassword.setTextFill(Color.RED);
                ErrorePassword.setText("Errore: Il campo Vecchia Password e' vuoto.");
            }else if(NuovaPswd.length() == 0){
                ErrorePassword.setFont(Font.font("Calibri", 15));
                ErrorePassword.setTextFill(Color.RED);
                ErrorePassword.setText("Errore: Il campo Nuova Password e' vuoto.");
            }else{
                ErrorePassword.setFont(Font.font("Calibri", 15));
                ErrorePassword.setTextFill(Color.RED);
                ErrorePassword.setText("Errore: Il campo Conferma Password e' vuoto.");
            }
        }else if(VecchiaPswd.equals(NuovaPswd)){
            ErrorePassword.setFont(Font.font("Calibri", 15));
            ErrorePassword.setTextFill(Color.RED);
            ErrorePassword.setText("Errore: Le password non coincidono.");
        }else if(NuovaPswd.length() <= 5){
            ErrorePassword.setFont(Font.font("Calibri", 15));
            ErrorePassword.setTextFill(Color.RED);
            ErrorePassword.setText("Errore: Password troppo breve deve essere almeno di 6 caratteri.");
        }else if(!(ConfermaPswd.equals(NuovaPswd))){
            ErrorePassword.setFont(Font.font("Calibri", 15));
            ErrorePassword.setTextFill(Color.RED);
            ErrorePassword.setText("Errore: La password non coincide.");
        }else{
            Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin(LogIn_Controller.getNomeAdmin(),VecchiaPswd);
            verificaCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(@NotNull Call<DBModelVerifica> call,@NotNull Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if(dbModelVerifica != null){
                        List<DBModelVerificaResults> results = dbModelVerifica.getResults();
                        if(results.get(0).getCodVerifica() == 1){
                            Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.CambiaPasswdAdmin(LogIn_Controller.getNomeAdmin(), ConfermaPswd);
                            insertCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NotNull Call<DBModelResponseToInsert> call,@NotNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if (dbModelResponseToInsert != null){
                                        if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                            ErroreSuccesso.setFont(Font.font("Calibri", 15));
                                            ErroreSuccesso.setTextFill(Color.DARKGREEN);
                                            ErroreSuccesso.setText("La Password e'\n stata cambiata.");
                                            VecchiaPassword.clear();
                                            NuovaPassword.clear();
                                            ConfermaPassword.clear();
                                        }
                                    }
                                }
                                @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call,@NotNull Throwable t) {
                                    ErrorePassword.setFont(Font.font("Calibri", 15));
                                    ErrorePassword.setTextFill(Color.RED);
                                    ErrorePassword.setText("Ops qualcosa e' andato storto.");
                                }
                            });
                        }else{
                            ErrorePassword.setFont(Font.font("Calibri", 15));
                            ErrorePassword.setTextFill(Color.RED);
                            ErrorePassword.setText("Errore: La vecchia password non coincide.");
                        }
                    }else{
                        ErrorePassword.setFont(Font.font("Calibri", 15));
                        ErrorePassword.setTextFill(Color.RED);
                        ErrorePassword.setText("Errore: Impossibile cambiare password.");
                    }
                }
                @Override public void onFailure(@NotNull Call<DBModelVerifica> call,@NotNull Throwable t) {
                    ErrorePassword.setFont(Font.font("Calibri", 15));
                    ErrorePassword.setTextFill(Color.RED);
                    ErrorePassword.setText("Ops qualcosa e' andato storto.");
                }
            });
        }

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
