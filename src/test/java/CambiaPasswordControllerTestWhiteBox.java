import Controller.CambiaPassword_Controller;
import Controller.LogIn_Controller;
import Model.ModelDBInterno.DBModelVerifica;
import Model.ModelDBInterno.DBModelVerificaResults;
import RetrofitClient.RetrofitClientDBInterno;
import RetrofitService.RetrofitServiceDBInterno;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class CambiaPasswordControllerTestWhiteBox {

    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Test
    public void TestCambiaPasswordWhiteBoxPath_2_3_4_6(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword("", "Test1", "Test1", "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
	public void TestCambiaPasswordWhiteBoxPath_2_3_4_6_7_10() {
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword("OfficialCineates20", "", "Test2", "officialcinemates@gmail.com");
        } catch (NullPointerException e) {

        }
    }

    @Test
	public void TestCambiaPasswordWhiteBoxPath_2_3_4_6_7_11(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword("OfficialCineates20", "Test1", "", "officialcinemates@gmail.com");
        } catch (NullPointerException e) {

        }
    }

    @Test
	public void TestCambiaPasswordWhiteBoxPath_2_3_4_5_8(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword("OfficialCineates20", "OfficialCineates20", "OfficialCineates20", "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestCambiaPasswordWhiteBoxPath_2_3_4_5_9_12(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword("OfficialCineates20", "Test1", "Test1", "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestCambiaPasswordWhiteBoxPath_2_3_4_5_9_14(){
        try {
            CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
            cambiaPassword_controller.CambiaPassword("OfficialCineates20", "TestUno", "UnTest", "officialcinemates@gmail.com");
        }catch (NullPointerException e){

        }
    }

    @Test
    public void TestCambiaPasswordWhiteBoxPath_2_3_4_5_9_15_16_17_19(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
        cambiaPassword_controller.CambiaPassword("OfficialCineates20", "TestUno", "TestUno", "officialcinemates@gmail.com");
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin("officialcinemates@gmail.com", "TestUno");
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
    public void TestCambiaPasswordWhiteBoxPath_2_3_4_5_9_15_16_17_18_20(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
        cambiaPassword_controller.CambiaPassword("OfficialCineates", "TestUno", "TestUno", "officialcinemates@gmail.com");
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin("officialcinemates@gmail.com", "TestUno");
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
    public void TestCambiaPasswordWhiteBoxPath_2_3_4_5_9_15_16_17_18_21_22_23(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
        cambiaPassword_controller.CambiaPassword("OfficialCineates20", "TestUno", "TestUno", "officialcinemates@gmail.com");
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin("officialcinemates@gmail.com", "TestUno");
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
    public void TestCambiaPawordWhiteBoxPath_2_3_4_5_9_15_16_17_18_21_22_22_24_25(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
        cambiaPassword_controller.CambiaPassword("OfficialCineates20", "TestUno", "TestUno", "officialcinemates@gmail.com");
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin("officialcinemates@gmail.com", "TestUno");
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
    public void TestCambiaPawordWhiteBoxPath_2_3_4_5_9_15_16_17_18_21_22_22_24_26(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        CambiaPassword_Controller cambiaPassword_controller = new CambiaPassword_Controller();
        cambiaPassword_controller.CambiaPassword("OfficialCineates20", "TestUno", "TestUno", "officialcinemates@gmail.com");
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdAdmin("officialcinemates@gmail.com", "TestUno");
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
}
