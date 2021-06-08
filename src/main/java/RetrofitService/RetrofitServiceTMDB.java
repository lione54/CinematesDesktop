package RetrofitService;


import Model.ModelTMDB.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitServiceTMDB {

    @GET("movie/popular")
    Call<MovieResponse> GetPopular(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("movie/{movie_id}/similar")
    Call<MovieResponse> Similar(@Path("movie_id") int movie_id, @Query("api_key") String api_key, @Query("language") String lingua);
}
