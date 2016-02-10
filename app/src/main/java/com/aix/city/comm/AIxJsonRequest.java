package com.aix.city.comm;


import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import java.nio.charset.Charset;

public class AIxJsonRequest<T> extends JsonRequest<T> {

    private final Class<T> responseType;

    /**
     * Creates a new request.
     *
     * @param method
     *            the HTTP method to use
     * @param url
     *            URL to fetch the JSON from
     * @param requestData
     *            A {@link Object} to post and convert into json as the request. Null is allowed and indicates no parameters will be posted along with request.
     * @param listener
     *            Listener to receive the JSON response
     * @param errorListener
     *            Error listener, or null to ignore errors.
     * @param shouldCache
     *            Whether or not responses to this request should be cached
     */
    public AIxJsonRequest(int method, String url, Object requestData, Class<T> responseType, Response.Listener<T> listener, Response.ErrorListener errorListener, boolean shouldCache) {
        super(method, url, (requestData == null) ? null : Mapper.string(requestData), listener, errorListener);
        this.responseType = responseType;
        this.setShouldCache(shouldCache);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, Charset.forName("UTF-8")  /*HttpHeaderParser.parseCharset(response.headers)*/);
            Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);
            return Response.success(Mapper.objectOrThrow(jsonString, responseType), entry);
        }
        catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    public Class<T> getResponseType() {
        return responseType;
    }
}