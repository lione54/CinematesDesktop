package Model.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

public class DBModelUtentiResponce {

    @SerializedName("Email")
    private String Email;
    @SerializedName("Username")
    private String Username;

    public DBModelUtentiResponce() {
    }

    public DBModelUtentiResponce(String email, String username) {
        Email = email;
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
