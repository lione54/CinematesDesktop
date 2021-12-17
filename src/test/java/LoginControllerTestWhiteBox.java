import Controller.LogIn_Controller;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginControllerTestWhiteBox {

    private final static Stage stage = null;
    private final static MouseEvent event = null;

    @Test
    public void TestLogInWhiteBoxPath_2_3_4_5_7(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, "", "sdfse");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestLogInWhiteBoxPath_2_3_4_5_8(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, "test.com", "dfgsd");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestLogInWhiteBoxPath_2_3_4_6_9_10(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, "officialcinemates@gmail.com", "");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestLogInWhiteBoxPath_2_3_4_6_9_11_12_14(){
        LogIn_Controller loginController = new LogIn_Controller();
        loginController.LogIn(stage, event, "test@test.com", "adfgdf");
        Assertions.assertNull(loginController.getNomeAdmin());
    }

    @Test
    public void TestLogInWhiteBoxPath_2_3_4_6_9_11_12_13_16(){
        LogIn_Controller loginController = new LogIn_Controller();
        loginController.LogIn(stage, event, "officialcinemates@gmail.com", "afgrgtad");
        Assertions.assertNull(loginController.getNomeAdmin());
    }

    @Test
    public void TestLogInWhiteBoxPath_2_3_4_6_9_11_12_13_15_17_19(){
        LogIn_Controller loginController = new LogIn_Controller();
        loginController.LogIn(stage, event, "officialcinemates@gmail.com", "OfficialCinemates20");
        Assertions.assertNull(loginController.getNomeAdmin());
    }

    @Test
    public void TestLogInWhiteBoxPath_2_3_4_6_9_11_12_13_15_17_18_21(){
        LogIn_Controller loginController = new LogIn_Controller();
        loginController.LogIn(stage, event, "officialcinemates@gmail.com", "OfficialCinemates20");
        Assertions.assertNull(loginController.getNomeAdmin());
    }

    @Test
    public void TestLogInWhiteBoxPath_2_3_4_6_9_11_12_13_15_17_18_20_22_23(){
        LogIn_Controller loginController = new LogIn_Controller();
        loginController.LogIn(stage, event, "officialcinemates@gmail.com", "OfficialCinemates20");
        Assertions.assertEquals("officialcinemates@gmail.com", loginController.getNomeAdmin());;
    }

    @Test
    public void TestLogInWhiteBoxPath_2_3_4_6_9_11_12_13_15_17_18_20_22_24(){
        LogIn_Controller loginController = new LogIn_Controller();
        loginController.LogIn(stage, event, "officialcinemates@gmail.com", "OfficialCinemates20");
        Assertions.assertEquals("officialcinemates@gmail.com", loginController.getNomeAdmin());
    }
}
