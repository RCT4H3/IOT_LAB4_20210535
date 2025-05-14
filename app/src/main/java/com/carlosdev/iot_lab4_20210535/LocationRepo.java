package com.carlosdev.iot_lab4_20210535;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface LocationRepo {
    @GET("search.json")
    Call<List<Location>> buscarUbicaciones(
            @Query("key") String apiKey,
            @Query("q") String query
    );
}
