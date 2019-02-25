package com.hyman.zhh.utils.network;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 *
 */
public interface NetworkService {

    @GET
    Observable<ResponseBody> get(@Url String url);

    @GET
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, String> params);

    @POST
    Observable<ResponseBody> post(@Url String url);

    @Headers({"Content-Type:text/plain;charset=utf-8"})
    @POST
    Observable<ResponseBody> postString(@Url String url, @Body String body);

    @Headers({"Content-Type:application/json;charset=utf-8"})
    @POST
    Observable<ResponseBody> postJson(@Url String url, @Body RequestBody jsonBody);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postFormData(@Url String url, @FieldMap Map<String, String> formParams);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Url String url, @Part("description") RequestBody description,
                                        @Part MultipartBody.Part part);

    @POST
    Observable<ResponseBody> post(@Url String url, @QueryMap Map<String, String> params);

    @Headers({"Content-Type:text/plain;charset=utf-8"})
    @POST
    Observable<ResponseBody> postString(@Url String url, @QueryMap Map<String, String> params, @Body String body);

    @Headers({"Content-Type:application/json;charset=utf-8"})
    @POST
    Observable<ResponseBody> postJson(@Url String url, @QueryMap Map<String, String> params, @Body RequestBody jsonBody);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> postFormData(@Url String url, @QueryMap Map<String, String> params, @FieldMap Map<String, String> formParams);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Url String url, @QueryMap Map<String, String> params, @Part("description") RequestBody description,
                                        @Part MultipartBody.Part part);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Url String url, @Part List<MultipartBody.Part> parts);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Url String url, @QueryMap Map<String, String> params,
                                        @Part List<MultipartBody.Part> parts);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}
