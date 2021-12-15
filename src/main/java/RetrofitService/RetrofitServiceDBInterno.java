package RetrofitService;

import Model.ModelDBInterno.*;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitServiceDBInterno {

    @FormUrlEncoded
    @POST("VerificaEsistenzaAdmin.php")
    Call<DBModelVerifica> VerificaEsistenzaAdmin(@Field("Email_Admin") String EmailAdmin);

    @FormUrlEncoded
    @POST("SignInAdmin.php")
    Call<DBModelResponseToInsert> SignInAdmin(@Field("Email_Admin") String EmailAdmin, @Field("Psw_Admin") String PswAdmin);

    @FormUrlEncoded
    @POST("VerificaPasswdAdmin.php")
    Call<DBModelVerifica> VerificaPasswdAdmin(@Field("Email_Admin") String EmailAdmin, @Field("Psw_Admin") String PswAdmin);

    @FormUrlEncoded
    @POST("PrendiFotoAdmin.php")
    Call<DBModelFoto> PrendiFotoAdmin(@Field("Email_Admin") String EmailAdmin, @Field("Psw_Admin") String PswAdmin);

    @FormUrlEncoded
    @POST("CambiaFotoProfiloAdimn.php")
    Call<DBModelResponseToInsert> CambiaFotoProfiloAdimn(@Field("Email_Admin") String EmailAdmin, @Field("nome") String Foto);

    @FormUrlEncoded
    @POST("VerificaSeCiSonoSegnalazioni.php")
    Call<DBModelVerifica> VerificaSeCiSonoSegnalazioni(@Field("Email_Admin") String EmailAdmin);

    @FormUrlEncoded
    @POST("CercaSegnalazioni.php")
    Call<DBModelSegnalazioni> PrendiSegnalazioni(@Field("Email_Admin") String EmailAdmin);

    @FormUrlEncoded
    @POST("RecuperoPasswdAdmin.php")
    Call<DBModelResponseToInsert> RecuperoPasswd(@Field("Email_Admin") String EmailAdmin, @Field("Psw_Admin") String PswAdmin);

    @FormUrlEncoded
    @POST("CambiaPasswdAdmin.php")
    Call<DBModelResponseToInsert> CambiaPasswdAdmin(@Field("Email_Admin") String EmailAdmin, @Field("Psw_Admin") String NuovaPswAdmin);

    @FormUrlEncoded
    @POST("AccettaSegnalazione.php")
    Call<DBModelResponseToInsert> AccettaSegnalazione(@Field("UtenteSegnalato") String UtenteSegnalato, @Field("UtenteSegnalatore") String UtenteSegnalatore, @Field("Id_Segnalazione") String Id_Segnalazione);

    @FormUrlEncoded
    @POST("DeclinaSengnalazione.php")
    Call<DBModelResponseToInsert> DeclinaSengnalazione(@Field("UtenteSegnalato") String UtenteSegnalato, @Field("UtenteSegnalatore") String UtenteSegnalatore, @Field("Id_Segnalazione") String Id_Segnalazione);

    @FormUrlEncoded
    @POST("PrendiEmailUtenti.php")
    Call<DBModelUtenti> PrendiEmailUtenti(@Field("Email_Admin") String Email_Admin);
}
