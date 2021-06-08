package Model.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

public class DBModelFotoResponce {

    @SerializedName("Foto_Admin")
    private String Foto_Profilo;

    public DBModelFotoResponce() {
    }

    public DBModelFotoResponce(String foto_Profilo) {
        Foto_Profilo = foto_Profilo;
    }

    public String getFoto_Profilo() {
        if(Foto_Profilo == null){
            return null;
        }else{
            String UrlBase = "http://192.168.1.9/cinematesdb/";
            return UrlBase + Foto_Profilo;
        }
    }

    public void setFoto_Profilo(String foto_Profilo) {
        Foto_Profilo = foto_Profilo;
    }
}
