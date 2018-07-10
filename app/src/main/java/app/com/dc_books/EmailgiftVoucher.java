package app.com.dc_books;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;


public class EmailgiftVoucher extends AppCompatActivity {

    TextInputLayout ti_emai;
    TextInputLayout ti_name;
    TextInputLayout ti_message;
    TextInputLayout ti_phone;

    TextInputEditText te_email;
    TextInputEditText te_name;
    TextInputEditText te_message;
    TextInputEditText te_phone;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    Toolbar toolbar;

    int NETCONNECTION;

    Map<String,String>Voucherparams=new HashMap<>();
    Map<String, String> PaymentCompleteParams = new HashMap<String, String>();

    String userID;
    String Name;
    String Email;
    String User_ID;

    String android_id;
    String SpMerchantRefNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emailgiftvoucher);

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ti_emai=findViewById(R.id.ti_email);
        ti_name=findViewById(R.id.ti_name);
        ti_message=findViewById(R.id.ti_message);
        ti_phone=findViewById(R.id.ti_phone);

        te_email=findViewById(R.id.te_email);
        te_name=findViewById(R.id.te_name);
        te_message=findViewById(R.id.te_message);
        te_phone=findViewById(R.id.te_phone);







    }
    public void Buy(View v)
    {


        ti_emai.setError(null);
        ti_name.setError(null);
        ti_message.setError(null);

        if(!te_email.getText().toString().matches(String.valueOf(android.util.Patterns.EMAIL_ADDRESS)))
        {
            ti_emai.setError("Enter valid email id");
        }
        else if(te_name.getText().toString().equals(""))
        {
            ti_name.setError("Enter your name");
        }
        else if(te_message.getText().toString().equals(""))
        {
            ti_name.setError("Enter message");
        }
        else if(!te_phone.getText().toString().matches(String.valueOf(Patterns.PHONE)))
        {
            ti_name.setError("Enter valid mobile number");
        }
        else if(User_ID.equals(""))
        {
            Intent in=new Intent(this,LoginActivity.class);
            in.putExtra("Identifier","5");
            startActivity(in);
        }
        else
        {
            isNetworkConnected();
            if(NETCONNECTION==1)
            {
                Voucherparams.put("user_id",userID);
                Voucherparams.put("appkey", MainActivity.appkey);
                Voucherparams.put("appsecurity",MainActivity.appsecurity);
                Voucherparams.put("recipientemail",te_email.getText().toString());
                Voucherparams.put("name",te_name.getText().toString());
                Voucherparams.put("message",te_message.getText().toString());
                Voucherparams.put("voucherid",getIntent().getStringExtra("ProdID"));
                Voucherparams.put("voucheramount",getIntent().getStringExtra("ProdPrice"));
                Voucherparams.put("vouchertitle",getIntent().getStringExtra("ProdName"));

                Call_EmailVoucheService();



            }
            else
            {
                new SweetAlertDialog(EmailgiftVoucher.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No internet")
                        .setContentText("Internet not available, Cross check your internet connectivity and try again")
                        .show();
            }
        }
    }
    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {


                String orderid= response.substring(response.indexOf("orderId=")+8, response.indexOf(":txnId"));
                System.out.println("orderid "+orderid);

                String paymentid= response.substring(response.indexOf("paymentId=")+10, response.indexOf(":token"));
                System.out.println("paymentid "+paymentid);

                String transactionid= response.substring(response.indexOf("txnId=")+6, response.indexOf(":paymentId"));
                System.out.println("transactionid "+transactionid);


                PaymentCompleteParams.put("appkey",MainActivity.appkey);
                PaymentCompleteParams.put("appsecurity",MainActivity.appsecurity);
                PaymentCompleteParams.put("paystatus","success");
                PaymentCompleteParams.put("ResponseCode","0");
                PaymentCompleteParams.put("ResponseMessage","Online Payment Success");
                PaymentCompleteParams.put("PaymentID",paymentid);
                PaymentCompleteParams.put("MerchantRefNo",orderid);
                PaymentCompleteParams.put("paymenttype","Online Payment");
                PaymentCompleteParams.put("totalamount",getIntent().getStringExtra("ProdPrice"));
                PaymentCompleteParams.put("transactionid",transactionid);
                PaymentCompleteParams.put("user_id",userID);
                PaymentCompleteParams.put("paymentmode_id","app-"+orderid);
                PaymentCompleteParams.put("PaymentMode","Online Payment");
                PaymentCompleteParams.put("uniquedevice",android_id);
                PaymentCompleteParams.put("SpMerchantRefNo",SpMerchantRefNo);



                Call_paymentcomplete();
            }

            @Override
            public void onFailure(int code, String reason) {


                PaymentCompleteParams.put("appkey",MainActivity.appkey);
                PaymentCompleteParams.put("appsecurity",MainActivity.appsecurity);
                PaymentCompleteParams.put("paystatus","failed");
                PaymentCompleteParams.put("ResponseCode","1");
                PaymentCompleteParams.put("ResponseMessage","Online Payment Failed");
                PaymentCompleteParams.put("PaymentID","failed");
                PaymentCompleteParams.put("MerchantRefNo", String.valueOf(System.currentTimeMillis()));
                PaymentCompleteParams.put("paymenttype","Online Payment");
                PaymentCompleteParams.put("totalamount",getIntent().getStringExtra("ProdPrice"));
                PaymentCompleteParams.put("transactionid","failed");
                PaymentCompleteParams.put("user_id",userID);
                PaymentCompleteParams.put("paymentmode_id","failed");
                PaymentCompleteParams.put("PaymentMode","Online Payment");
                PaymentCompleteParams.put("uniquedevice",android_id);
                PaymentCompleteParams.put("SpMerchantRefNo",SpMerchantRefNo);

                Call_paymentcomplete();
            }
        };
    }
    public void Call_EmailVoucheService()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        System.out.print("Response "+Voucherparams.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://dcbookstore.tk/api/order/buygiftvoucher", new JSONObject(Voucherparams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();


                        try
                        {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            if(jsonResponse.getString("status").equals("true"))
                            {
                                SpMerchantRefNo=jsonResponse.getString("SpMerchantRefNo");
                                callInstamojoPay(Email,te_phone.getText().toString(),getIntent().getStringExtra("ProdPrice"),"DC BOOKS EMAIL GIFT VOUCHER",Name);
                            }


                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();



                System.out.println("Registration Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
        }

        public void Call_paymentcomplete()
        {
            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(true);
            pDialog.show();

            System.out.print("Response "+PaymentCompleteParams.toString());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    "https://dcbookstore.tk/api/order/completeegvpayment", new JSONObject(PaymentCompleteParams),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            pDialog.dismiss();


                            try
                            {
                                JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                                if(jsonResponse.getString("status").equals("true"))
                                {
                                     Intent in=new Intent(EmailgiftVoucher.this,TransactionActivity.class);
                                     in.putExtra("State","Success");
                                     in.putExtra("Amount",getIntent().getStringExtra("ProdPrice"));
                                     startActivity(in);
                                }

                                }
                            catch (NullPointerException e)
                            {
                                e.printStackTrace();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        }
                    }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();



                    System.out.println("Registration Responce---> "+error.toString());
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq,
                    "jobj_req");
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:

                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }catch (Exception e)
                {

                }

                EmailgiftVoucher.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void onResume() {
        super.onResume();
        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        Name = s.getString("Username", "");
        Email = s.getString("EmailID", "");
        User_ID = s.getString("User_id", "");

    }
    }
