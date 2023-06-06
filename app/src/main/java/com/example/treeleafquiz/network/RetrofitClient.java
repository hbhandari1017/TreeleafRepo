package com.example.treeleafquiz.network;

import static com.example.treeleafquiz.network.Url.API_KEY;
import static com.example.treeleafquiz.network.Url.BASE_URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            // Add logging interceptor for debugging (optional)
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);

            // Add header token interceptor
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("X-Api-Key", API_KEY)
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }
}

