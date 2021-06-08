package Model.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelFoto {

    @SerializedName("dbdata")
    private List<DBModelFotoResponce> result;

    public DBModelFoto() {
    }

    public DBModelFoto(List<DBModelFotoResponce> result) {
        this.result = result;
    }

    public List<DBModelFotoResponce> getResult() {
        return result;
    }

    public void setResult(List<DBModelFotoResponce> result) {
        this.result = result;
    }
}
