package Controller;

import Model.ModelDBInterno.DBModelResponseToInsert;
import Model.ModelDBInterno.DBModelVerifica;
import Model.ModelDBInterno.DBModelVerificaResults;
import RetrofitClient.RetrofitClientDBInterno;
import RetrofitService.RetrofitServiceDBInterno;
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
import org.example.BuildConfig;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

public class RecuperaPasswd_Controller extends Controller{

    @FXML private Button closeButton, minimizeButton , Conferma, Annulla;
    @FXML private TextField Email;
    @FXML private Label Error;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override public void initialize() {
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        Conferma.setOnMouseClicked(this::ConfermaButtonClicked);
        Annulla.setOnMouseClicked(this::ButtonAnnullaClicked);
    }

    private void ButtonAnnullaClicked(MouseEvent mouseEvent) {
        try{
            Stage stage = (Stage) Annulla.getScene().getWindow();
            stage.close();
            Node source = (Node)  mouseEvent.getSource();
            Stage primaryStage  = (Stage) source.getScene().getWindow();
            LogInStage(primaryStage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML private void ConfermaButtonClicked(@NotNull Event event) {
        Stage stage = (Stage) Conferma.getScene().getWindow();
        final String[] SignIn = {null};
        String email = Email.getText().toString();
        String Passwd = RandomPass(10).toString();
        if (!(email.matches("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}"))){
            Error.setFont(Font.font("Calibri", 15));
            Error.setTextFill(Color.RED);
            Error.setText("Errore: Email Non Valida.");
        } else {
            Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaEsistenzaAdmin(email);
            verificaCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(@NotNull Call<DBModelVerifica> call,@NotNull Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if(dbModelVerifica != null){
                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                        if(verificaResults.get(0).getCodVerifica() == 1){
                            Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.RecuperoPasswd(email, Passwd);
                            insertCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NotNull Call<DBModelResponseToInsert> call,@NotNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert responseToInsert = response.body();
                                    if(responseToInsert != null){
                                        if(responseToInsert.getStato().equals("Successfull")){
                                            Properties properties = new Properties();
                                            properties.put("mail.smtp.auth","true");
                                            properties.put("mail.smtp.starttls.enable","true");
                                            properties.put("mail.smtp.host","smtp.gmail.com");
                                            properties.put("mail.smtp.port","587");
                                            Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
                                                @Override protected PasswordAuthentication getPasswordAuthentication() {
                                                    return new PasswordAuthentication(BuildConfig.Username, BuildConfig.Passwd);
                                                }
                                            });
                                            try {
                                                Message message = new MimeMessage(session);
                                                message.setFrom(new InternetAddress());
                                                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                                                message.setSubject("Codice Per Password Dimenticata.");
                                                message.setText("Bentornato" + email + ",\nquesta e' la password generata in automatico per aver dimenticato la propria.\nLe consigliamo di cambiarla dopo l'accesso.\nCodice:" + Passwd + ".\nCordiali Saluti,\nIl Team di Cinemates.");
                                                Transport.send(message);
                                                SignIn[0] = "Successfull";
                                                Platform.runLater(new Runnable() {
                                                    @Override public void run() {
                                                        if(SignIn[0].equals("Successfull")) {
                                                            stage.close();
                                                            Node source = (Node) event.getSource();
                                                            Stage primaryStage = (Stage) source.getScene().getWindow();
                                                            LogInStage(primaryStage);
                                                        }
                                                    }
                                                });
                                            }catch (MessagingException e){
                                                Error.setFont(Font.font("Calibri", 15));
                                                Error.setTextFill(Color.RED);
                                                Error.setText("Errore: Invio email fallito");
                                                SignIn[0] = "Error";
                                            }
                                        }
                                    }
                                }
                                @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call,@NotNull Throwable t) {
                                    Error.setFont(Font.font("Calibri", 15));
                                    Error.setTextFill(Color.RED);
                                    Error.setText("Ops qualcosa è andato storto.");
                                    SignIn[0] = "Error";
                                }
                            });
                        }else{
                            Error.setFont(Font.font("Calibri", 15));
                            Error.setTextFill(Color.RED);
                            Error.setText("Errore: Admin Inesistente");
                            SignIn[0] = "Error";
                        }
                    }
                }
                @Override public void onFailure(@NotNull Call<DBModelVerifica> call,@NotNull Throwable t) {
                    Error.setFont(Font.font("Calibri", 15));
                    Error.setTextFill(Color.RED);
                    Error.setText("Ops qualcosa è andato storto.");
                    SignIn[0] = "Error";
                }
            });
        }
    }

    private void LogInStage(@NotNull Stage primaryStage) {
        Parent root = null;
        try {
            Platform.setImplicitExit(false);
            primaryStage.hide();
            root = FXMLLoader.load(RecuperaPasswd_Controller.class.getResource("/fxml/LogIn.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage newPrimaryStage = new Stage();
            newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
            newPrimaryStage.setScene(scene);
            newPrimaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(RecuperaPasswd_Controller.class.toString()).severe("LogIn loading failed");
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
