import Controller.LogIn_Controller;
import RetrofitService.RetrofitServiceDBInterno;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

public class LoginControllerTestBlackBox {

    private final static String MAIL_CE1 = "";
    private final static String MAIL_CE2 = "test@test.com";
    private final static String MAIL_CE3 = "testtest.com";
    private final static String PWD_CE1 = "";
    private final static String PWD_CE2 = "Test01";
    private final static String PWD_CE3 = "steT01";
    private final static Stage stage = null;
    private final static MouseEvent event = null;

    @Test
    public void TestMetodoMAIL_CE1_PWD_CE1(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, MAIL_CE1, PWD_CE1);
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoMAIL_CE1_PWD_CE2(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, MAIL_CE1, PWD_CE2);
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoMAIL_CE1_PWD_CE3(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, MAIL_CE1, PWD_CE3);
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoMAIL_CE2_PWD_CE1(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, MAIL_CE2, PWD_CE1);
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoMAIL_CE2_PWD_CE2(){
        LogIn_Controller loginController = new LogIn_Controller();
        loginController.LogIn(stage, event, MAIL_CE2, PWD_CE2);
    }

    @Test
    public void TestMetodoMAIL_CE2_PWD_CE3(){
        LogIn_Controller loginController = new LogIn_Controller();
        loginController.LogIn(stage, event, MAIL_CE2, PWD_CE3);
    }

    @Test
    public void TestMetodoMAIL_CE3_PWD_CE1(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, MAIL_CE3, PWD_CE1);
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoMAIL_CE3_PWD_CE2(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, MAIL_CE3, PWD_CE2);
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoMAIL_CE3_PWD_CE3(){
        try {
            LogIn_Controller loginController = new LogIn_Controller();
            loginController.LogIn(stage, event, MAIL_CE3, PWD_CE3);
        }catch (NullPointerException e){

        }
    }
}
