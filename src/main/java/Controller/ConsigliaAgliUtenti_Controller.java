package Controller;


import Adapter.ConsigliatiAdapter;
import Email.SendEmail;
import Model.ModelDBInterno.DBModelResponseToInsert;
import Model.ModelDBInterno.DBModelUtenti;
import Model.ModelDBInterno.DBModelUtentiResponce;
import Model.ModelTMDB.MovieResponse;
import Model.ModelTMDB.MovieResponseResults;
import RetrofitClient.RetrofitClientDBInterno;
import RetrofitClient.RetrofitClientTMDB;
import RetrofitService.RetrofitServiceDBInterno;
import RetrofitService.RetrofitServiceTMDB;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import org.example.BuildConfig;
import org.jetbrains.annotations.NotNull;
import org.kairos.layouts.RecyclerView;
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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class ConsigliaAgliUtenti_Controller extends Controller{

    @FXML private Button closeButton;
    @FXML private Button minimizeButton, RicaricaCondigliati, InviaConsigli;
    @FXML private Hyperlink LogOutLink;
    @FXML private Hyperlink VisualizzaSegnalazioniLink;
    @FXML private Hyperlink CambiaPasswdLink;
    @FXML private Hyperlink ConsigliaAgliUtenti;
    @FXML private Label NomeAdminLabel;
    @FXML private ImageView FotoProfiloAdmin;
    @FXML private Label ErroreSuccesso;
    @FXML private RecyclerView FilmConsigliati;
    private ConsigliatiAdapter consigliatiAdapter;
    private List<MovieResponseResults> resultsList = new ArrayList<>();
    private RetrofitServiceTMDB retrofitServiceTMDB;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;


    @Override public void initialize() {
        retrofitServiceTMDB = RetrofitClientTMDB.getClient().create(RetrofitServiceTMDB.class);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        NomeAdminLabel.setText(NomeAdminLabel.getText() + LogIn_Controller.getNomeAdmin());
        PreparareConsigliati();
        if(LogIn_Controller.getFotoAdmin() != null){
            FotoProfiloAdmin.setImage(new Image(LogIn_Controller.getFotoAdmin()));
        }else{
            FotoProfiloAdmin.setImage(new Image("/images/businessman.png"));
        }
        Eventi();
    }

    @Override public void Eventi() {
        closeButton.setOnMouseClicked(this::ButtonCloseClicked);
        minimizeButton.setOnMouseClicked(this::ButtonMinimizeClicked);
        VisualizzaSegnalazioniLink.setOnMouseClicked(this::VisualizzaSegnalazioniLinkClicked);
        CambiaPasswdLink.setOnMouseClicked(this::CambiaPasswdLinkClicked);
        LogOutLink.setOnMouseClicked(this::LogOutLinkClicked);
        FotoProfiloAdmin.setOnMouseClicked(this::FotoProfiloClicked);
        ConsigliaAgliUtenti.setOnMouseClicked(this::ConsigliaAgliUtentiClicked);
        RicaricaCondigliati.setOnMouseClicked(this::RicaricaConsigliatiClicked);
        InviaConsigli.setOnMouseClicked(this::InviaConsigliClicked);
    }

    private void InviaConsigliClicked(MouseEvent mouseEvent) {
        Call<DBModelUtenti> utentiCall = retrofitServiceDBInterno.PrendiEmailUtenti(LogIn_Controller.getNomeAdmin());
        utentiCall.enqueue(new Callback<DBModelUtenti>() {
            @Override public void onResponse(@NotNull Call<DBModelUtenti> call,@NotNull Response<DBModelUtenti> response) {
                DBModelUtenti dbModelUtenti = response.body();
                if(dbModelUtenti != null){
                    List<DBModelUtentiResponce> emailList = dbModelUtenti.getResults();
                    if(!(emailList.isEmpty())){
                        for (int i = 0; i < emailList.size(); i++){
                            try {
                                StringBuffer htmlText = new StringBuffer("<!DOCTYPE html> <head> </head>");
                                htmlText.append("<body style=\"background: #0e1111; padding: 0; margin: 0; font-family: verdana; color: #FF8C00;\" > <div style=\"display:flex\"> <img src=\"cid:image2\" style=\" background-repeat: no-repeat; position: left; height:100px; margin-left: 1%; width:64px;height:64px;margin-top: 18px;margin-left: 10px;\"> <img src=\"cid:image1\" style=\"background-repeat: no-repeat; padding: 0; left:0; display: block; margin: 0 auto; height:100px; width:260px;\"> </div>");
                                htmlText.append("<p style=\"font-family: verdana; color: #FF8C00;margin-left: 10px;\">Caro " + emailList.get(i).getUsername() + ",</p> <p style=\"font-family: verdana; color: #FF8C00;margin-left: 10px;\">i film riportati di seguito sono sono stati scelti dai nostri admin per voi.</p> <ul style=\"list-style-type: none; padding: 0; margin: 0; margin-top: -1px; background-color: #0e1111; padding: 12px;\">");
                                for(int j = 0; j < resultsList.size(); j++){
                                    htmlText.append("<li style=\"display: flex; width: 100%; height: 300px; margin-bottom: 1%;\" > <img src=" + resultsList.get(j).getPoster_path() + " style=\" background-repeat: no-repeat; background-size:contain; height:300px; padding: 0; margin 0; left: 0; width: 30%;\"> <div style=\"display:block; width:100%;   background-image: linear-gradient(to right, #FF8C00 , #FF8C00);\"> <h1 style=\"color:#0e1111; margin-left: 1%;\">" + resultsList.get(j).getTitle() + "</h1> <p style=\"font-size:14px; color:#0e1111;margin-left: 1%;\">"+ resultsList.get(j).getOverview() +"</p> <p style=\"font-size:14px; color:#0e1111; margin-left: 1%;\">Vai nell'App Cinemates per ulteriori dettagli!</p> </div> </li> <li>");
                                }
                                htmlText.append("</ul> <p style=\"font-family: verdana; color: #FF8C00;margin-left: 10px;\">Cordiali Saluti,</p><p style=\"font-family: verdana; color: #FF8C00;margin-left: 10px;\">Lo staff di Cinemates.</p> </body> </html>");
                                Map<String, String> inlineImages = new HashMap<String, String>();
                                inlineImages.put("image1", BuildConfig.Logo_Email);
                                inlineImages.put("image2", BuildConfig.Clapperboard_Image);
                                SendEmail.send(BuildConfig.host, BuildConfig.port, BuildConfig.Username, BuildConfig.Passwd, emailList.get(i).getEmail(), "Film Consigliati dagli Admin.", htmlText.toString(), inlineImages, "Consigliati");
                                ErroreSuccesso.setFont(Font.font("Calibri", 15));
                                ErroreSuccesso.setTextFill(Color.GREEN);
                                ErroreSuccesso.setText("Email inviata\ncon successo.");
                            }catch (MessagingException | IOException e){
                                ErroreSuccesso.setFont(Font.font("Calibri", 15));
                                ErroreSuccesso.setTextFill(Color.RED);
                                ErroreSuccesso.setText("Errore:\nInvio email fallito");
                            }
                        }
                    }else{
                        ErroreSuccesso.setFont(Font.font("Calibri", 15));
                        ErroreSuccesso.setTextFill(Color.RED);
                        ErroreSuccesso.setText("Ricarica la pagina");
                    }
                }
            }
            @Override public void onFailure(@NotNull Call<DBModelUtenti> call,@NotNull Throwable t) {

            }
        });
    }

    private void RicaricaConsigliatiClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage)  RicaricaCondigliati.getScene().getWindow();
        stage.close();
        Node source = (Node)  mouseEvent.getSource();
        Stage primaryStage  = (Stage) source.getScene().getWindow();
        Consiglia(primaryStage);
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
                                ErroreSuccesso.setText("Foto cambiata\ncon successo.");
                            }else{
                                ErroreSuccesso.setFont(Font.font("Calibri", 15));
                                ErroreSuccesso.setTextFill(Color.RED);
                                ErroreSuccesso.setText("Errore:\nLa foto non\nverra' aggiornata.");
                            }
                        }else{
                            ErroreSuccesso.setFont(Font.font("Calibri", 15));
                            ErroreSuccesso.setTextFill(Color.RED);
                            ErroreSuccesso.setText("Errore:\nImpossibile\naggiornare foto.");
                        }
                    }
                    @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call,@NotNull Throwable t) {
                        ErroreSuccesso.setFont(Font.font("Calibri", 15));
                        ErroreSuccesso.setTextFill(Color.RED);
                        ErroreSuccesso.setText("Ops qualcosa e'\nandato storto.");
                    }
                });
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        Node source = (Node) mouseEvent.getSource();
                        Stage primaryStage = (Stage) source.getScene().getWindow();
                        Consiglia(primaryStage);
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

    private void PreparareConsigliati(){
        int posizione = 0, id = 0;
        if(!(Home_Controller.getListadiID().isEmpty())){
            posizione = generateRandom(Home_Controller.getListadiID().size());
            id=Home_Controller.getListadiID().get(posizione);
        }else if(!(CambiaPassword_Controller.getListadiID().isEmpty())){
            posizione = generateRandom(CambiaPassword_Controller.getListadiID().size());
            id = CambiaPassword_Controller.getListadiID().get(posizione);
        }
        Call<MovieResponse> racomandedCall = retrofitServiceTMDB.Similar(id, BuildConfig.THE_MOVIE_DB_APY_KEY, "it-IT");
        racomandedCall.enqueue(new Callback<MovieResponse>() {
            @Override public void onResponse(@NotNull Call<MovieResponse> call,@NotNull Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                if(movieResponse != null){
                    resultsList = movieResponse.getResults();
                    if(!(resultsList.isEmpty())){
                        consigliatiAdapter = new ConsigliatiAdapter();
                        FilmConsigliati.setAdapter(consigliatiAdapter);
                        FilmConsigliati.getItems().addAll(resultsList);
                    }else{
                        ErroreSuccesso.setFont(Font.font("Calibri", 15));
                        ErroreSuccesso.setTextFill(Color.RED);
                        ErroreSuccesso.setText("Attenzione:\nRicaricare\nconsigliati.");
                    }
                }
            }
            @Override public void onFailure(@NotNull Call<MovieResponse> call,@NotNull Throwable t) {
                ErroreSuccesso.setFont(Font.font("Calibri", 15));
                ErroreSuccesso.setTextFill(Color.RED);
                ErroreSuccesso.setText("Ops qualcosa e'\nandato storto.");
            }
        });
    }

    public int generateRandom(int max) {
        Random r = new Random( System.currentTimeMillis() );
        return r.nextInt(max);
    }
}
