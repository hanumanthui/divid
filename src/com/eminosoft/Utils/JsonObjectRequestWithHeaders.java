/*package com.eminosoft.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

*//**
 *
 * JSON Request object with headers added. You can add extra headers in getHeaders() method.
 *
 * Created by Aswin Vodapally on 3/5/2015.
 *//*
public class JsonObjectRequestWithHeaders extends JsonObjectRequest {
    public JsonObjectRequestWithHeaders(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String,String> headers=new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return super.getHeaders();
    }
}
*/