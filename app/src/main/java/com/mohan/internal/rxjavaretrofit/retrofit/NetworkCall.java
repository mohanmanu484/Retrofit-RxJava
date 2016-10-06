package com.mohan.internal.rxjavaretrofit.retrofit;


import com.mohan.internal.rxjavaretrofit.pojo.MoviesResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mohan on 2/10/16.
 */

public interface NetworkCall {

    @GET("3/movie/top_rated")
    Observable<MoviesResponse> getMovies(@Query("api_key") String apiKey);
}
