package com.sar.user.fligh_app;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String Tag=ApiClient.class.getSimpleName();
    private static int TIME_OUT=60;
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    public  static Retrofit getClient(Retrofit retrofit)
    {
        if(okHttpClient==null)
        {
            initOkhttp();
        }
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder().baseUrl(Const.BASE_URL).client(okHttpClient).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
    public static void initOkhttp()
    {
           OkHttpClient.Builder httpClient=new OkHttpClient().newBuilder().readTimeout(TIME_OUT, TimeUnit.SECONDS).writeTimeout(TIME_OUT, TimeUnit.SECONDS).connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
       httpClient.addInterceptor(httpLoggingInterceptor);
       httpClient.addInterceptor(new Interceptor() {
           @Override
           public Response intercept(Chain chain) throws IOException {
               Request orignal=chain.request();
               Request.Builder requestBuilder=orignal.newBuilder().addHeader("Accept","application/json").addHeader("Request-Type", "Android")
                       .addHeader("Content-Type", "application/json");


                Request request=requestBuilder.build();

               return chain.proceed(request);
           }
       });
       okHttpClient=httpClient.build();
    }


}
