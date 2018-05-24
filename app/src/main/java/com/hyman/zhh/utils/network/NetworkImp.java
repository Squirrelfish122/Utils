package com.hyman.zhh.utils.network;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.hyman.zhh.utils.utils.UrlUtils;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class NetworkImp implements NetworkApi {

    private NetworkService mNetworkService;

    private static NetworkImp INSTANCE;

    private static final int DEFAULT_TIMEOUT = 8;
    private static final int READ_TIME_OUT = 8;
    private static final int WRITE_TIME_OUT = 8;

    // TODO: 2018/5/24 update to your self url
    private static final String BASE_URL_PREVIEW = "https://www.baidu.com";
    private static final String BASE_URL_RELEASE = "http://www.baidu.com";

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_MULTIPART_FORM_DATA = MediaType.parse("multipart/form-data");
    private String mBaseUrl = BASE_URL_RELEASE;

    private Gson mGson = new Gson();

    private NetworkImp(boolean release) {


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .followRedirects(true);

        // TODO: ignore all https certificate
        SSLSocketClient.setSSLSocketFactory(builder);
        SSLSocketClient.setHostnameVerifier(builder);

        if (!release) {
            mBaseUrl = BASE_URL_PREVIEW;
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addNetworkInterceptor(new StethoInterceptor());
        }

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mBaseUrl)
                .client(builder.build())
                .build();
        mNetworkService = retrofit.create(NetworkService.class);
    }

    public static NetworkImp getInstance(boolean release) {
        if (INSTANCE == null) {
            synchronized (NetworkImp.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetworkImp(release);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public <T> void get(@NonNull String url, Map<String, String> params,
                        final NetworkListener<T> listener) {
        Preconditions.checkNotNull(url);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.get(url, params);
        } else {
            observable = mNetworkService.get(url);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        String body = responseBody.string();
                        Class<T> clz = (Class<T>) ((ParameterizedType) listener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        return mGson.fromJson(body, clz);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getForString(@NonNull String url, Map<String, String> params,
                             final NetworkListener<String> listener) {
        Preconditions.checkNotNull(url);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.get(url, params);
        } else {
            observable = mNetworkService.get(url);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        return responseBody.string();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public <T> void post(@NonNull String url, Map<String, String> params,
                         final NetworkListener<T> listener) {
        Preconditions.checkNotNull(url);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.post(url, params);
        } else {
            observable = mNetworkService.post(url);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        String body = responseBody.string();
                        Class<T> clz = (Class<T>) ((ParameterizedType) listener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        return mGson.fromJson(body, clz);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void postForString(@NonNull String url, Map<String, String> params,
                              final NetworkListener<String> listener) {
        Preconditions.checkNotNull(url);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.post(url, params);
        } else {
            observable = mNetworkService.post(url);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        return responseBody.string();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public <T> void postString(@NonNull String url, @NonNull String body, Map<String, String> params,
                               final NetworkListener<T> listener) {
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(body);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.postString(url, params, body);
        } else {
            observable = mNetworkService.postString(url, body);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        String body = responseBody.string();
                        Class<T> clz = (Class<T>) ((ParameterizedType) listener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        return mGson.fromJson(body, clz);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void postStringForString(@NonNull String url, @NonNull String body,
                                    Map<String, String> params,
                                    final NetworkListener<String> listener) {
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(body);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.postString(url, params, body);
        } else {
            observable = mNetworkService.postString(url, body);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        return responseBody.string();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public <T> void postJson(@NonNull String url, @NonNull String jsonBody,
                             Map<String, String> params, final NetworkListener<T> listener) {
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(jsonBody);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonBody);
        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.postJson(url, params, requestBody);
        } else {
            observable = mNetworkService.postJson(url, requestBody);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        String body = responseBody.string();
                        Class<T> clz = (Class<T>) ((ParameterizedType) listener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        return mGson.fromJson(body, clz);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void postJsonForString(@NonNull String url, @NonNull String jsonBody,
                                  Map<String, String> params,
                                  final NetworkListener<String> listener) {
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(jsonBody);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonBody);
        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.postJson(url, params, requestBody);
        } else {
            observable = mNetworkService.postJson(url, requestBody);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        return responseBody.string();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public <T> void postFormData(@NonNull String url, @NonNull Map<String, String> formData,
                                 Map<String, String> params, final NetworkListener<T> listener) {
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(formData);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.postFormData(url, params, formData);
        } else {
            observable = mNetworkService.postFormData(url, formData);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        String body = responseBody.string();
                        Class<T> clz = (Class<T>) ((ParameterizedType) listener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        return mGson.fromJson(body, clz);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void postFormDataForString(@NonNull String url, @NonNull Map<String, String> formData,
                                      Map<String, String> params,
                                      final NetworkListener<String> listener) {
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(formData);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.postFormData(url, params, formData);
        } else {
            observable = mNetworkService.postFormData(url, formData);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        return responseBody.string();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public <T> void uploadFile(@NonNull String url, @NonNull String fileDescription,
                               @NonNull File file, Map<String, String> params,
                               final NetworkListener<T> listener) {
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(fileDescription);
        Preconditions.checkNotNull(file);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        RequestBody requestFile = RequestBody.create(MEDIA_TYPE_MULTIPART_FORM_DATA, file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody description = RequestBody.create(MEDIA_TYPE_MULTIPART_FORM_DATA, fileDescription);

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.uploadFile(url, params, description, body);
        } else {
            observable = mNetworkService.uploadFile(url, description, body);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        String body = responseBody.string();
                        Class<T> clz = (Class<T>) ((ParameterizedType) listener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        return mGson.fromJson(body, clz);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void uploadFileForString(@NonNull String url, @NonNull String fileDescription,
                                    @NonNull File file, Map<String, String> params,
                                    final NetworkListener<String> listener) {
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(fileDescription);
        Preconditions.checkNotNull(file);
        HashMap<String, String> map = new HashMap<>();
        url = UrlUtils.urlSkipParams(url, map);

        if (params == null) {
            params = new HashMap<>();
        }

        RequestBody requestFile = RequestBody.create(MEDIA_TYPE_MULTIPART_FORM_DATA, file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody description = RequestBody.create(MEDIA_TYPE_MULTIPART_FORM_DATA, fileDescription);

        Observable<ResponseBody> observable;
        if (checkHasParams(params, map)) {
            observable = mNetworkService.uploadFile(url, params, description, body);
        } else {
            observable = mNetworkService.uploadFile(url, description, body);
        }

        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        return responseBody.string();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String t) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null && !listener.isCancel()) {
                            listener.onFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private boolean checkHasParams(Map<String, String> params, HashMap<String, String> map) {
        if (map.size() > 0) {
            params.putAll(map);
        }
        return params.size() > 0;
    }
}
