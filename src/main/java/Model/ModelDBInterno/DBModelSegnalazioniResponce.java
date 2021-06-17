package Model.ModelDBInterno;

import com.google.gson.annotations.SerializedName;
import org.example.BuildConfig;

public class DBModelSegnalazioniResponce {

    @SerializedName("Id_Segnalazione")
    private Integer Id_Segnalazione;
    @SerializedName("UserSegnalato")
    private String UserSegnalato;
    @SerializedName("UserSegnalatore")
    private String UserSegnalatore;
    @SerializedName("Motivazione")
    private String Motivazione;
    @SerializedName("Titolo_Film")
    private String Titolo_Film_Recensito;
    @SerializedName("Testo_Recensione")
    private String Testo_Recensione;
    @SerializedName("Foto_Profilo")
    private String FotoProfilo;

    public DBModelSegnalazioniResponce() {
    }

    public DBModelSegnalazioniResponce(Integer id_Segnalazione, String userSegnalato, String userSegnalatore, String motivazione, String titolo_Film_Recensito, String testo_Recensione, String fotoProfilo) {
        Id_Segnalazione = id_Segnalazione;
        UserSegnalato = userSegnalato;
        UserSegnalatore = userSegnalatore;
        Motivazione = motivazione;
        Titolo_Film_Recensito = titolo_Film_Recensito;
        Testo_Recensione = testo_Recensione;
        FotoProfilo = fotoProfilo;
    }

    public Integer getId_Segnalazione() {
        return Id_Segnalazione;
    }

    public void setId_Segnalazione(Integer id_Segnalazione) {
        Id_Segnalazione = id_Segnalazione;
    }

    public String getUserSegnalato() {
        return UserSegnalato;
    }

    public void setUserSegnalato(String userSegnalato) {
        UserSegnalato = userSegnalato;
    }

    public String getUserSegnalatore() {
        return UserSegnalatore;
    }

    public void setUserSegnalatore(String userSegnalatore) {
        UserSegnalatore = userSegnalatore;
    }

    public String getMotivazione() {
        return Motivazione;
    }

    public void setMotivazione(String motivazione) {
        Motivazione = motivazione;
    }

    public String getTitolo_Film_Recensito() {
        return Titolo_Film_Recensito;
    }

    public void setTitolo_Film_Recensito(String titolo_Film_Recensito) {
        Titolo_Film_Recensito = titolo_Film_Recensito;
    }

    public String getTesto_Recensione() {
        return Testo_Recensione;
    }

    public void setTesto_Recensione(String testo_Recensione) {
        Testo_Recensione = testo_Recensione;
    }

    public String getFotoProfilo() {
        if(FotoProfilo == null){
            return null;
        }else{
            String UrlBase = "http://" + BuildConfig.IP_AWS + "/cinematesdb/";
            return UrlBase + FotoProfilo;
        }
    }

    public void setFotoProfilo(String fotoProfilo) {
        FotoProfilo = fotoProfilo;
    }

}
