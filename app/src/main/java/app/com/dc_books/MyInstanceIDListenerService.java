package app.com.dc_books;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by JINS on 10/19/2016.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {


    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";
    SharedPreferences s;
    Map<String, String> FCMParams = new HashMap<String, String>();
    String ip_head = "http://www.level10boutique.com/";
    String SendFCMId_Url=ip_head+"admin/services/AppUsernotification";
    int NETCONNECTION;
    String refreshedToken,savedToken;

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        savedToken=s.getString("Token_Status","0");

        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        edit.putString("Token",refreshedToken);
        edit.commit();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    if(savedToken.equals("0")) {
        Log.d("kfjadkfjasfka",savedToken);
        sendRegistrationToServer(refreshedToken);
    }
    }

    private void sendRegistrationToServer(String refreshedToken) {
        FCMParams.put("appkey", "TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc");
        FCMParams.put("appsecurity", "TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==");
        FCMParams.put("devicetype", "0");
        FCMParams.put("deviceid", refreshedToken);
        isNetworkConnected();
        if(NETCONNECTION==1){
            sendFCMId();
        }else {
            new SweetAlertDialog(MyInstanceIDListenerService.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Check your internet connectivity and try again")
                    .show();
        }
    }
    public void sendFCMId(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                SendFCMId_Url, new JSONObject(FCMParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("Registration Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
//                            String Message=jsonResponse.getString("result");

                            if(Status.equals("true"))
                            {
                                edit.putString("Token",jsonResponse.getString(refreshedToken));
                                edit.putString("Token_Status",jsonResponse.getString(Status));
                                edit.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("Registration Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    private boolean isNetworkConnected()
    {
        ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            NETCONNECTION=0;
        }
        else
        {

            NETCONNECTION=1;
        }

        return true;
    }
}