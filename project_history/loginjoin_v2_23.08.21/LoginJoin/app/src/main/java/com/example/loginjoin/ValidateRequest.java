package com.example.loginjoin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
    final static private String URL = "http://stjcompany.dothome.co.kr/myadmin/register.php";
    private Map<String, String> parameters;

    public ValidateRequest(String UserEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("UserEmail", UserEmail);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
