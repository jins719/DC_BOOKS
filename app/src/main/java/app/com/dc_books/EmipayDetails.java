package app.com.dc_books;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EmipayDetails extends AppCompatActivity {

    String preorderemipaymenturl="https://dcbookstore.tk/api/order/preorderemipayment";

    TextView t_txtproduct;
    TextView t_txtproductname;
    TextView t_txtinstallmentpaid;
    TextView t_txtbalanceamount;
    TextView t_txtinstallmentunpaid;
    TextView t_txtnextinstallment;

    TextView t_productname;
    TextView t_installmentpaid;
    TextView t_balanceamount;
    TextView t_installmentunpaid;
    TextView t_nextinstallment;

    Typeface font;
    Typeface font2;
    Typeface font3;

    int NETCONNECTION;

    Map<String, String> PreorderParams = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.emipaydetails);
        setResult(Activity.RESULT_CANCELED);



        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3= Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");


        t_txtproduct=findViewById(R.id.txt_product);
        t_txtproductname=findViewById(R.id.txt_productname);
        t_txtinstallmentpaid=findViewById(R.id.txt_intalmentpaid);
        t_txtbalanceamount=findViewById(R.id.txt_balanceamount);
        t_txtinstallmentunpaid=findViewById(R.id.txt_unpaid);
        t_txtnextinstallment=findViewById(R.id.txt_nextinstallment);

        t_productname=findViewById(R.id.productname);
        t_installmentpaid=findViewById(R.id.installmentpaid);
        t_balanceamount=findViewById(R.id.balanceamount);
        t_installmentunpaid=findViewById(R.id.unpaid);
        t_nextinstallment=findViewById(R.id.nextinstallment);

        t_txtproduct.setTypeface(font3);
        t_txtproductname.setTypeface(font3);
        t_txtinstallmentpaid.setTypeface(font3);
        t_txtbalanceamount.setTypeface(font3);
        t_txtinstallmentunpaid.setTypeface(font3);
        t_txtnextinstallment.setTypeface(font3);

        t_productname.setTypeface(font);
        t_installmentpaid.setTypeface(font);
        t_balanceamount.setTypeface(font);
        t_installmentunpaid.setTypeface(font);
        t_nextinstallment.setTypeface(font);

        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            PreorderParams.put("appkey", MainActivity.appkey);
            PreorderParams.put("appsecurity", MainActivity.appsecurity);
            PreorderParams.put("preorderid",getIntent().getStringExtra("PreOrder"));
            Call_Preorder();
        }else
        {
            new SweetAlertDialog(EmipayDetails.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Check your internet connectivity and try again")
                    .show();
        }

    }

    private void Call_Preorder() {


        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                preorderemipaymenturl, new JSONObject(PreorderParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismissWithAnimation();
                        System.out.println("cccccccc "+response.toString());
                        System.out.println("cccccccc "+new JSONObject(PreorderParams));


                        try {
                            JSONObject jsonResponse = new JSONObject(response.toString());
                            JSONArray jsonMain = jsonResponse.getJSONArray("orderdeliver");
                            int lengthJsonArr = jsonMain.length();
                            for (int i = 0; i < lengthJsonArr; i++) {


                                JSONObject jsonChild = jsonMain.getJSONObject(i);
                                String id = jsonChild.getString("id");
                                String productname = jsonChild.getString("productname");
                                String orderstatusname = jsonChild.getString("orderstatusname");
                                String orderstatus = jsonChild.getString("orderstatus");
                                String date = jsonChild.getString("date");
                                String ordernumber=jsonChild.getString("ordernumber");
                                String image= "https://dcbookstore.tk/"+jsonChild.getString("image");

                                }

                                } catch (JSONException e) {
                            e.printStackTrace();
                            // pDialog.dismiss();
                            System.out.println("ssssss " + e.toString());
                        }
                        catch (NullPointerException e) {
                            e.printStackTrace();
                            // pDialog.dismiss();
                            System.out.println("ssssss " + e.toString());
                        }



                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        });

// Adding request to request queue
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
