package Model.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelSegnalazioni {

    @SerializedName("dbdata")
    private List<DBModelSegnalazioniResponce> result;

    public DBModelSegnalazioni() {
    }

    public DBModelSegnalazioni(List<DBModelSegnalazioniResponce> result) {
        this.result = result;
    }

    public List<DBModelSegnalazioniResponce> getResult() {
        return result;
    }

    public void setResult(List<DBModelSegnalazioniResponce> result) {
        this.result = result;
    }
}
