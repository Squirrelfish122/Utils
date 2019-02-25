package com.hyman.zhh.utils.network;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface NetworkApi {

    <T> void get(@NonNull String url, Map<String, String> params, NetworkListener<T> listener);

    void getForString(@NonNull String url, Map<String, String> params, NetworkListener<String> listener);

    <T> void post(@NonNull String url, Map<String, String> params, NetworkListener<T> listener);

    void postForString(@NonNull String url, Map<String, String> params, NetworkListener<String> listener);

    <T> void postString(@NonNull String url, @NonNull String body, Map<String, String> params, NetworkListener<T> listener);

    void postStringForString(@NonNull String url, @NonNull String body, Map<String, String> params, NetworkListener<String> listener);

    <T> void postJson(@NonNull String url, @NonNull String jsonBody, Map<String, String> params, NetworkListener<T> listener);

    void postJsonForString(@NonNull String url, @NonNull String jsonBody, Map<String, String> params, NetworkListener<String> listener);

    <T> void postFormData(@NonNull String url, @NonNull Map<String, String> formData, Map<String, String> params, NetworkListener<T> listener);

    void postFormDataForString(@NonNull String url, @NonNull Map<String, String> formData, Map<String, String> params, NetworkListener<String> listener);

    <T> void uploadFile(@NonNull String url, @NonNull String fileDescription, @NonNull File file, Map<String, String> params, NetworkListener<T> listener);

    void uploadFileForString(@NonNull String url, @NonNull String fileDescription, @NonNull File file, Map<String, String> params, NetworkListener<String> listener);

    void uploadFile(String url, Map<String, String> params, List<File> files, NetworkListener<Boolean> listener);

    void downloadFile(String url, String savePath, NetworkListener<DownloadResult> listener);
}
