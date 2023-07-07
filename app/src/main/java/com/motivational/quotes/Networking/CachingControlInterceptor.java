package com.motivational.quotes.Networking;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CachingControlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        request = request.newBuilder()
                .header("Cache-Control", "public, max-stale=2419200")
                .build();

        Response originalResponse = chain.proceed(request);
        return originalResponse.newBuilder()
                .header("Cache-Control", "max-age=600")
                .build();
    }

    public class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return "No connectivity exception";
        }
    }

}