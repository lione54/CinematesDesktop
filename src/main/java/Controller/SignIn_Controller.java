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

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;


public class SignIn_Controller extends Controller {
    @FXML private Button closeButton, minimizeButton, ConfermaButton, AnnullaButton;
    @FXML private TextField Email;
    @FXML private Label Error;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;


    @Override public void initialize(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
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
        Platform.exit();
        System.exit(0);
    }

    @FXML private void ButtonMinimizeClicked(MouseEvent event) {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML private void ConfermaButtonClicked(@NotNull Event event) {
        Stage stage = (Stage) ConfermaButton.getScene().getWindow();
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
                        if(verificaResults.get(0).getCodVerifica() == 0){
                            Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.SignInAdmin(email, Passwd);
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
                                                message.setSubject("Codice Per Verifica Email.");
                                                MimeMultipart multipart = new MimeMultipart("related");
                                                BodyPart textPart = new MimeBodyPart();
                                                String htmlText ="<center><img src=\"cid:image\"></center>" +
                                                        "<html><head><style>h1 {background-color: #FF8C00;padding: 15px; text-indent: 40px;} " +
                                                        "p {text-indent: 60px;}</style></head><body><h1>Codice Per Verifica Email</h1> " +
                                                        "<p> Benvenuto nuovo admin,questa e' la prima password generata per il suo primo accesso alle funzionalita' da admin le consigliamo di cambiarla dopo l'accesso. </p>" +
                                                        "<p> Codice: " + Passwd + " .</p> <p>Cordiali Saluti,</p><p>Il Team di Cinemates." + "</p></div></body></html>";
                                                textPart.setContent(htmlText, "text/html");
                                                multipart.addBodyPart(textPart);
                                                BodyPart imagePart = new MimeBodyPart();
                                                DataSource fds = new FileDataSource("C:/Users/matti/Desktop/CinematesDesktop/src/main/resources/images/logocinemates.png");
                                                imagePart.setDataHandler(new DataHandler(fds));
                                                imagePart.setHeader("Content-ID","<image>");
                                                imagePart.setDisposition(MimeBodyPart.INLINE);
                                                multipart.addBodyPart(imagePart);
                                                message.setContent(multipart);
                                                Transport.send(message);
                                                SignIn[0] = "Successfull";
                                                Platform.runLater(new Runnable() {
                                                    @Override public void run() {
                                                        if(SignIn[0].equals("Successfull")) {
                                                            stage.close();
                                                            Node source = (Node) event.getSource();
                                                            Stage primaryStage = (Stage) source.getScene().getWindow();
                                                            ContinuaStage(primaryStage);
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
                            Error.setText("Errore: Admin Esistente");
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
