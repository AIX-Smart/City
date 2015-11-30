package com.aix.city.comm;


import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

public class AIxRequest<T> extends JsonRequest<T> {

    private Class<T> responseType;
    private boolean ignoreCache;

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
     */
    public AIxRequest(int method, String url, Object requestData, Class<T> responseType, Response.Listener<T> listener, Response.ErrorListener errorListener, boolean ignoreCache)
    {
        super(method, url, (requestData == null) ? null : Mapper.string(requestData), listener, errorListener);
        this.responseType = responseType;
        this. ignoreCache = ignoreCache;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Cache.Entry entry;
            if(ignoreCache) entry = HttpHeaderParser.parseCacheHeaders(response); //TODO:HttpHeaderParser.parseIgnoreCacheHeaders(response);
            else entry = HttpHeaderParser.parseCacheHeaders(response);
            return Response.success(Mapper.objectOrThrow(jsonString, responseType), entry);
        }
        catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    public boolean isIgnoreCache() {
        return ignoreCache;
    }

    public Class<T> getResponseType() {
        return responseType;
    }
}