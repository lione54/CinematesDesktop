package Model.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelUtenti {

    @SerializedName("dbdata")
    private List<DBModelUtentiResponce> results;

    public DBModelUtenti() {
    }

    public DBModelUtenti(List<DBModelUtentiResponce> results) {
        this.results = results;
    }

    public List<DBModelUtentiResponce> getResults() {
        return results;
    }

    public void setResults(List<DBModelUtentiResponce> results) {
        this.results = results;
    }
}
