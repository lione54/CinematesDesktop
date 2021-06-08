package Controller;

import com.google.gson.Gson;

import java.io.*;

public class RememberMe {

    public static final String CONFIG_FILE = "config.txt";
    private String Username;
    private String Passwd;

    public RememberMe(String username, String passwd) {
        Username = username;
        Passwd = passwd;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPasswd() {
        return Passwd;
    }

    public void setPasswd(String passwd) {
        Passwd = passwd;
    }

    public static void Config(String user, String pass){
        RememberMe rememberMe = new RememberMe(user,pass);
        File file = new File(CONFIG_FILE);
        Gson gson = new Gson();
        Writer writer = null;
        try {
            if(file != null){
                writer = new FileWriter(CONFIG_FILE);
                gson.toJson(rememberMe,writer);
            }else{
                file.delete();
                writer = new FileWriter(CONFIG_FILE);
                gson.toJson(rememberMe,writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static RememberMe getInfo(){
        Gson gson = new Gson();
        RememberMe rememberMe = new RememberMe(null, null);
        try {
            rememberMe = gson.fromJson(new FileReader(CONFIG_FILE), RememberMe.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return rememberMe;
    }
}
