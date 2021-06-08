package Adapter;

import Controller.ConsigliaAgliUtenti_Controller;
import Model.ModelTMDB.MovieResponseResults;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kairos.layouts.RecyclerView;

public class ConsigliatiAdapter extends RecyclerView.Adapter<ConsigliatiAdapter.DataHolder> {

    @Override public ConsigliatiAdapter.DataHolder onCreateViewHolder(FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(ConsigliaAgliUtenti_Controller.class.getResource("/fxml/layout_consigliati.fxml"));
        return new DataHolder(fxmlLoader);
    }

    @Override public void onBindViewHolder(ConsigliatiAdapter.DataHolder dataHolder, Object o) {
        MovieResponseResults movieResponseResults = (MovieResponseResults) o;
        if(movieResponseResults.getPoster_path() != null) {
            dataHolder.PosterFilm.setImage(new Image(movieResponseResults.getPoster_path()));
        }
        dataHolder.TitoloFilm.setText(movieResponseResults.getTitle());
        if(movieResponseResults.getOverview() != null){
            Integer Maxlen = movieResponseResults.getOverview().length();
            if(Maxlen > 70) {
                String Overview = movieResponseResults.getOverview().substring(0, 70);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Overview).append("...\nContinuare a leggere.");
                dataHolder.Trama.setText(String.valueOf(stringBuilder));
            }else if (Maxlen >= 0 && Maxlen <= 49){
                dataHolder.Trama.setText("Non disponibile.");
            }else{
                String Overview = movieResponseResults.getOverview().substring(0, 50);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Overview).append("...\nContinuare a leggere.");
                dataHolder.Trama.setText(String.valueOf(stringBuilder));
            }
        }else{
            dataHolder.Trama.setText("Non disponibile.");
        }
        dataHolder.Release.setText(movieResponseResults.getRelease_date());
        dataHolder.MediaVoto.setText(String.valueOf(movieResponseResults.getVote_average()));
    }

    public class DataHolder extends RecyclerView.ViewHolder{

        @FXML private ImageView PosterFilm;
        @FXML private Label TitoloFilm, Trama, Release, MediaVoto;
        @FXML private CheckBox SelezionaFilm;

        public DataHolder(FXMLLoader loader) {
            super(loader);
        }
    }
}
