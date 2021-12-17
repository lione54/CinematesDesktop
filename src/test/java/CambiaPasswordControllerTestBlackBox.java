
import Controller.CambiaPassword_Controller;
import Controller.LogIn_Controller;
import Model.ModelDBInterno.DBModelVerifica;
import Model.ModelDBInterno.DBModelVerificaResults;
import RetrofitClient.RetrofitClientDBInterno;
import RetrofitService.RetrofitServiceDBInterno;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;


public class CambiaPasswordControllerTestBlackBox {

    private final static String VPWD_CE1 = "";
    private final static String VPWD_CE2 = "OfficialCinemates20";
    private final static String VPWD_CE3 = "OfficialCinemates";
    private final static String NPWD_CE1 = "";
    private final static String NPWD_CE2 = "Test01";
    private final static String CPWD_CE1 = "";
    private final static String CPWD_CE2 = "Test01";
    private final static String CPWD_CE3 = "steT01";
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Test
    public void TestMetodoVPWD_CE1_NPWD_CE1_CPWD_CE1(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE1,NPWD_CE1, CPWD_CE1, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoVPWD_CE1_NPWD_CE2_CPWD_CE2(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE1,NPWD_CE2, CPWD_CE2, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }


    @Test
    public void TestMetodoVPWD_CE2_NPWD_CE1_CPWD_CE1(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE2,NPWD_CE1, CPWD_CE1, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoVPWD_CE2_NPWD_CE2_CPWD_CE2(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
        cambiaPassword_controller.CambiaPassword(VPWD_CE2,NPWD_CE2, CPWD_CE2, LogIn_Controller.getNomeAdmin());
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin("officialcinemates@gmail.com", CPWD_CE2);
        verificaCall.enqueue(new Callback<DBModelVerifica>() {
            @Override public void onResponse(Call<DBModelVerifica> call, Response<DBModelVerifica> response) {
                        DBModelVerifica dbModelVerifica = response.body();
                        if(dbModelVerifica != null) {
                            List<DBModelVerificaResults> results = dbModelVerifica.getResults();
                            Assertions.assertEquals("1", results.get(0).getCodVerifica());
                        }
            }
            @Override public void onFailure(Call<DBModelVerifica> call, Throwable t) {

            }
        });
    }


    @Test
    public void TestMetodoVPWD_CE3_NPWD_CE1_CPWD_CE1(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE3,NPWD_CE1, CPWD_CE1, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoVPWD_CE3_NPWD_CE2_CPWD_CE2(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
        cambiaPassword_controller.CambiaPassword(VPWD_CE3,NPWD_CE2, CPWD_CE2, LogIn_Controller.getNomeAdmin());
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin("officialcinemates@gmail.com", CPWD_CE2);
        verificaCall.enqueue(new Callback<DBModelVerifica>() {
            @Override public void onResponse(Call<DBModelVerifica> call, Response<DBModelVerifica> response) {
                DBModelVerifica dbModelVerifica = response.body();
                if(dbModelVerifica != null) {
                    List<DBModelVerificaResults> results = dbModelVerifica.getResults();
                    Assertions.assertEquals("0", results.get(0).getCodVerifica());
                }
            }
            @Override public void onFailure(Call<DBModelVerifica> call, Throwable t) {

            }
        });
    }


    @Test
    public void TestMetodoVPWD_CE2_NPWD_CE1_CPWD_CE2(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE2,NPWD_CE1, CPWD_CE2, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoVPWD_CE3_NPWD_CE1_CPWD_CE3(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE3,NPWD_CE1, CPWD_CE3, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoVPWD_CE1_NPWD_CE2_CPWD_CE1(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE1,NPWD_CE2, CPWD_CE1, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoVPWD_CE3_NPWD_CE2_CPWD_CE3(){
        try {
            retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE3, NPWD_CE2, CPWD_CE3, LogIn_Controller.getNomeAdmin());
            Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin("officialcinemates@gmail.com", CPWD_CE3);
            verificaCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(Call<DBModelVerifica> call, Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if (dbModelVerifica != null) {
                        List<DBModelVerificaResults> results = dbModelVerifica.getResults();
                        Assertions.assertEquals("0", results.get(0).getCodVerifica());
                    }
                }
                @Override public void onFailure(Call<DBModelVerifica> call, Throwable t) {
                }
            });
        }catch (NullPointerException e){

        }
    }


    @Test
    public void TestMetodoVPWD_CE2_NPWD_CE2_CPWD_CE1(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE2,NPWD_CE2, CPWD_CE1, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }


    @Test
    public void TestMetodoVPWD1_CE_NPWD_CE1_CPWD_CE2(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE2,NPWD_CE1, CPWD_CE2, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }


    @Test
    public void TestMetodoVPWD_CE1_NPWD_CE1_CPWD_CE3(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE1,NPWD_CE1, CPWD_CE3, "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestMetodoVPWD_CE2_NPWD_CE2_CPWD_CE3(){
        try {
            retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword(VPWD_CE2, NPWD_CE2, CPWD_CE3, LogIn_Controller.getNomeAdmin());
            Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin("officialcinemates@gmail.com", CPWD_CE3);
            verificaCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(Call<DBModelVerifica> call, Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if (dbModelVerifica != null) {
                        List<DBModelVerificaResults> results = dbModelVerifica.getResults();
                        Assertions.assertEquals("0", results.get(0).getCodVerifica());
                    }
                }
                @Override public void onFailure(Call<DBModelVerifica> call, Throwable t) {

                }
            });
        }catch (NullPointerException e){

        }
    }

}
