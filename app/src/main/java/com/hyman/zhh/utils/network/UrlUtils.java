package com.hyman.zhh.utils.network;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.net.URI;
import java.util.Map;

public class UrlUtils {

    public static String urlSkipParams(@NonNull String url, @NonNull Map<String, String> params) {
        String _url = url;
        if (TextUtils.isEmpty(url))
            return url;
        try {
            URI uri = URI.create(url);

            String query = uri.getQuery();
            if (query != null) {
                _url = url.replaceAll("\\?" + query, "");
                String[] qs = query.split("\\&");
                for (int i = 0; i < qs.length; i++) {
                    String[] q = qs[i].split("=");
                    if (q.length == 2) {
                        params.put(q[0], q[1]);
                        // System.out.println(q[0] + ":" + q[1]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _url;
    }

}
