package Controller;

import Adapter.SegnalazioniAdapter;
import Model.ModelDBInterno.*;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public class Home_Controller extends Controller {

    @FXML private RecyclerView ListeSegnalazioni;
    @FXML private Button closeButton, minimizeButton;
    @FXML private Hyperlink LogOutLink, VisualizzaSegnalazioniLink, CambiaPasswdLink, ConsigliaAgliUtenti;
    @FXML private Label NomeAdminLabel, ErroreSuccesso;
    @FXML private ImageView FotoProfiloAdmin;
          private SegnalazioniAdapter segnalazioniAdapter;
          private RetrofitServiceDBInterno retrofitServiceDBInterno;
          private RetrofitServiceTMDB retrofitServiceTMDB;
          private List<Integer> id = new ArrayList<>();

          private static List<Integer> passggiodilista = new ArrayList<>();

          public static List<Integer> getListadiID() { return passggiodilista; }

          public void setListadiID(List<Integer> myVariable) { this.passggiodilista = myVariable; }

    @Override public void initialize(){
        NomeAdminLabel.setText(NomeAdminLabel.getText() + LogIn_Controller.getNomeAdmin());
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        retrofitServiceTMDB = RetrofitClientTMDB.getClient().create(RetrofitServiceTMDB.class);
        CercaSegnalazioni();
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
    }

    private void ConsigliaAgliUtentiClicked(MouseEvent event) {
        Stage stage = (Stage)  ConsigliaAgliUtenti.getScene().getWindow();
        Call<MovieResponse> topratedCall = retrofitServiceTMDB.GetPopular(BuildConfig.THE_MOVIE_DB_APY_KEY, "it-IT");
        topratedCall.enqueue(new retrofit2.Callback<MovieResponse>() {
            @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                if(movieResponse != null){
                    List<MovieResponseResults> resultsList = movieResponse.getResults();
                    for (int i = 0; i < resultsList.size(); i++){
                        id.add(resultsList.get(i).getId());
                    }
                    setListadiID(id);
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            stage.close();
                            Node source = (Node)  event.getSource();
                            Stage primaryStage  = (Stage) source.getScene().getWindow();
                            Consiglia(primaryStage);
                        }
                    });
                }
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
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
                            VisualizzaSegnalazioni(primaryStage);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
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

    public void CercaSegnalazioni() {
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaSeCiSonoSegnalazioni(LogIn_Controller.getNomeAdmin());
        verificaCall.enqueue(new retrofit2.Callback<DBModelVerifica>() {
            @Override public void onResponse(@NotNull Call<DBModelVerifica> call,@NotNull Response<DBModelVerifica> response) {
                DBModelVerifica dbModelVerifica = response.body();
                if(dbModelVerifica != null){
                    List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                    if(verificaResults.get(0).getCodVerifica() == 1){
                        Call<DBModelSegnalazioni> segnalazioniCall = retrofitServiceDBInterno.PrendiSegnalazioni(LogIn_Controller.getNomeAdmin());
                        segnalazioniCall.enqueue(new retrofit2.Callback<DBModelSegnalazioni>() {
                            @Override public void onResponse(@NotNull Call<DBModelSegnalazioni> call,@NotNull Response<DBModelSegnalazioni> response) {
                                DBModelSegnalazioni dbModelSegnalazioni = response.body();
                                if(dbModelSegnalazioni != null){
                                    List<DBModelSegnalazioniResponce> segnalazioniResponces = dbModelSegnalazioni.getResult();
                                    if(!(segnalazioniResponces.isEmpty())){
                                        segnalazioniAdapter = new SegnalazioniAdapter();
                                        ListeSegnalazioni.setAdapter(segnalazioniAdapter);
                                        ListeSegnalazioni.getItems().addAll(segnalazioniResponces);
                                    }
                                }
                            }
                            @Override public void onFailure(@NotNull Call<DBModelSegnalazioni> call,@NotNull Throwable t) {
                                ErroreSuccesso.setFont(Font.font("Calibri", 15));
                                ErroreSuccesso.setTextFill(Color.RED);
                                ErroreSuccesso.setText("Ops qualcosa e'\nandato storto.");
                            }
                        });
                    }else{
                        ErroreSuccesso.setFont(Font.font("Calibri", 15));
                        ErroreSuccesso.setTextFill(Color.RED);
                        ErroreSuccesso.setText("Attenzione:\nNon ci sono\nsegnalazioni.");
                    }
                }else{
                    ErroreSuccesso.setFont(Font.font("Calibri", 15));
                    ErroreSuccesso.setTextFill(Color.RED);
                    ErroreSuccesso.setText("Attenzione:\nNon ci sono\nsegnalazioni.");
                }
            }
            @Override public void onFailure(@NotNull Call<DBModelVerifica> call,@NotNull Throwable t) {
                ErroreSuccesso.setFont(Font.font("Calibri", 15));
                ErroreSuccesso.setTextFill(Color.RED);
                ErroreSuccesso.setText("Ops qualcosa e'\nandato storto.");
            }
        });
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
