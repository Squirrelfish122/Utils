package com.hyman.zhh.utils.network;

import com.hyman.zhh.utils.log.PrintLog;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 *
 */
public class SSLSocketClient {

    private static final String TAG = "sdk.SSLSocketClient";
    private static final String PROTOCOL_SSL = "SSL";

    public static void setHostnameVerifier(OkHttpClient.Builder builder) {
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    public static void setSSLSocketFactory(OkHttpClient.Builder builder) {
        boolean result = setSSLSocketFactory2(builder);
        PrintLog.d(TAG, "set ssl 2, result = " + result);
    }

    private static boolean setSSLSocketFactory2(OkHttpClient.Builder builder) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_SSL);
            sslContext.init(null, new TrustManager[]{getX509TrustManager()}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        if (sslSocketFactory == null) {
            return false;
        }
        builder.sslSocketFactory(sslSocketFactory);
        return true;
    }

    private static X509TrustManager getX509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }
}
