package jjh.com.palette;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {
    final static private  String URL = "http://wuddlaa.dothome.co.kr/Pallet/";
    private Map<String,String> parameters = new HashMap<>();
    ;

    public ValidateRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, URL+"selectAccount.php", listener, null);
        parameters.put("id",id);
    }
    public ValidateRequest(boolean deleteAll,String id, Response.Listener<String> listener) {
        super(Method.POST, URL+"deleteAll.php", listener, null);
        parameters.put("id",id);
    }
    public ValidateRequest(int library,String id, Response.Listener<String> listener) {
        super(Method.POST, URL+"selectLibrary.php", listener, null);
        parameters.put("id",id);
    }public ValidateRequest(String id,String library, Response.Listener<String> listener) {
        super(Method.POST, URL+"insertLibrary.php", listener, null);
        parameters.put("id",id);
        parameters.put("library",library);
    }
    public ValidateRequest(String id, int num, boolean checked, String keyword, Response.Listener<String> listener) {
        super(Method.POST, URL+"selectTheme.php", listener, null);
        //Log.d("aaaa",id+num+checked+keyword);
        parameters.put("id",id);
        if (num == 0)
            parameters.put("num","0");
        else if (num == 1)
            parameters.put("num","1");
        else if (num == 2)
            parameters.put("num","2");

        if (checked)
            parameters.put("checked","0");
        else
            parameters.put("checked","1");

        if (keyword.isEmpty())
            parameters.put("keyword","");
        else
            parameters.put("keyword",keyword);
    }
    public ValidateRequest(String id, String birth, String hint, Response.Listener<String> listener) {
        super(Method.POST, URL+"findId.php", listener, null);
        parameters.put("id",id);
        parameters.put("birth",birth);
        parameters.put("hint",hint);
    }
    public ValidateRequest(String id,String pw, String birth, String hint, Response.Listener<String> listener) {
        super(Method.POST, URL+"signUp.php", listener, null);
        parameters.put("id",id);
        parameters.put("pw",pw);
        parameters.put("birth",birth);
        parameters.put("hint",hint);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
