package app.com.dc_books;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

/**
 * Created by JINS on 10/13/2017.
 */

public class OrderSummary extends AppCompatActivity {

    Typeface font,font2,font3;
    String addresslist_url="https://dcbookstore.tk/api/user/address_list";
    String orderitems_url="https://dcbookstore.tk/api/order/order_summary_list";
    String addressdelete_url="https://dcbookstore.tk/api/user/delete_address";
    String deliverycharge_url="https://dcbookstore.tk/api/productshopping/delivery_charge";
    String voucherurl="https://dcbookstore.tk/api/order/apply_voucher";
    String Buyurl="https://dcbookstore.tk/api/order/buy";
    String Afterpayment_url="https://dcbookstore.tk/api/order/completepayment";
    String Clear_cart="https://dcbookstore.tk/api/productshopping/clear_cart";

    Map<String, String> AddressParams = new HashMap<String, String>();
    Map<String, String> OrderParams = new HashMap<String, String>();
    Map<String, String> AddresDeleteParams = new HashMap<String, String>();
    Map<String, String> DeliveryParams = new HashMap<String, String>();
    Map<String, String> VoucherParams = new HashMap<String, String>();
    Map<String, String> Buyparams = new HashMap<String, String>();
    Map<String, String> PaymentCompleteParams = new HashMap<String, String>();
    Map<String, String> ClearCartparams = new HashMap<String, String>();

    String appkey="TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc";
    String appsecurity="TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==";

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    String userID;

    ArrayList<String>Name=new ArrayList<>();
    ArrayList<String>Address=new ArrayList<>();
    ArrayList<String>AddressID=new ArrayList<>();
    ArrayList<ItemData> arraylist = new ArrayList<ItemData>();
    ArrayList<ItemData> arraylist2 = new ArrayList<ItemData>();


    private ArrayList<ItemData> AddressList = null;

    ArrayList<String>OrderID=new ArrayList<>();
    ArrayList<String>OrderItemName=new ArrayList<>();
    ArrayList<String>OrderQty=new ArrayList<>();
    ArrayList<String>OrderItemImages=new ArrayList<>();
    ArrayList<String>OrderItemPrice=new ArrayList<>();
    
    RecyclerView rlv_addresslist,rlv_orderitems;
    MyAdapter mAdapter;

    MyAdapter2 mAdapter2;
    CardView cardView2;

    TextView t_subtotal,t_subtotalvalue,t_deliverycharge,t_deliverychargevalue
            ,t_payamount,t_payamountvalue,t_Heading;

    int NETCONNECTION;
    Toolbar toolbar;
    int remove_position;

    String selectaddressid="";

    SharedPreferences s;
    TextView t_pricedetails;

    TextInputLayout ti_vouchercode;
    TextInputLayout ti_pin;

    TextInputEditText te_vouchercode;
    TextInputEditText te_pin;

    String voucheramount="",voucherid="";

    double Delivery_charge=0;

    String phone,email,name;
    String android_id;
    String cart_id;
    String deliverycharge;
    String SpMerchantRefNo;
    String totalweight;


    JsonObjectRequest jsonObjReq;

    double totoalpayamount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordersummary);


        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        s = getSharedPreferences(LoginActivity.mp, 0);
        String address_count=s.getString("UserAddress","");



        if(address_count.equals("0"))
        {
            Intent in=new Intent(this,AddAddress.class);
            startActivity(in);
        }


        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");

        rlv_addresslist=findViewById(R.id.recycleview);
        rlv_orderitems=findViewById(R.id.recycleview2);
        t_subtotal=findViewById(R.id.textView19);
        t_subtotalvalue=findViewById(R.id.textView18);
        t_deliverycharge=findViewById(R.id.textView20);
        t_deliverychargevalue=findViewById(R.id.textView21);
        t_payamount=findViewById(R.id.textView22);
        t_payamountvalue=findViewById(R.id.textView23);
        t_Heading=findViewById(R.id.testview01);
        t_pricedetails=findViewById(R.id.textView30);

        ti_vouchercode=findViewById(R.id.ti_vouchercode);
        ti_pin=findViewById(R.id.ti_pin);

        te_vouchercode=findViewById(R.id.te_vouchercode);
        te_pin=findViewById(R.id.te_pin);




        t_subtotal.setTypeface(font);
        t_subtotalvalue.setTypeface(font);
        t_deliverycharge.setTypeface(font);
        t_deliverychargevalue.setTypeface(font);
        t_payamount.setTypeface(font2);
        t_payamountvalue.setTypeface(font2);
        t_Heading.setTypeface(font2);
        t_pricedetails.setTypeface(font2);

        t_deliverycharge .setVisibility(View.GONE);
        t_deliverychargevalue .setVisibility(View.GONE);

        NestedScrollView scroller = (NestedScrollView) findViewById(R.id.nestedscrollview);
        cardView2=findViewById(R.id.cardview3);
        cardView2.setVisibility(View.VISIBLE);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        ClearCartparams.put("appkey",MainActivity.appkey);
        ClearCartparams.put("appsecurity",MainActivity.appsecurity);
        ClearCartparams.put("uniquedevice",android_id);

        OrderParams.put("appkey",MainActivity.appkey);
        OrderParams.put("appsecurity",MainActivity.appsecurity);
        OrderParams.put("uniquedevice",android_id);
        OrderParams.put("user_id",userID);
        OrderParams.put("subtotalproduct",getIntent().getExtras().getString("subtotal"));
        OrderParams.put("totalpayamount",getIntent().getExtras().getString("Weight"));
        OrderParams.put("vouchername",getIntent().getExtras().getString("vouchername"));
        OrderParams.put("voucheramount",getIntent().getExtras().getString("voucheramount"));
        OrderParams.put("totalprodductweight",getIntent().getExtras().getString("Weight"));
        call_OrderItems();

        }

   public void payment(View v)
   {
       isNetworkConnected();
       if(NETCONNECTION==1)
       {
           if(AddressID.size()==0)
           {
               new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.ERROR_TYPE)
                       .setTitleText("Please add a delivery address")
                       .show();
           }
           else if(selectaddressid.equals(""))
           {
               new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.ERROR_TYPE)
                       .setTitleText("Please select a delivery address")
                       .show();
           }
           else
           {

               isNetworkConnected();
               if(NETCONNECTION==1) {
                   Buyparams.put("appkey", MainActivity.appkey);
                   Buyparams.put("appsecurity", MainActivity.appsecurity);
                   Buyparams.put("uniquedevice", android_id);
                   Buyparams.put("user_id", userID);
                   Buyparams.put("orginalcarttotal", t_subtotalvalue.getText().toString());
                   Buyparams.put("carttotal", t_payamountvalue.getText().toString());
                   Buyparams.put("UseraddressesID", selectaddressid);
                   Buyparams.put("voucheramount", voucheramount);
                   Buyparams.put("voucherid", voucherid);
                   Buyparams.put("deliveryamount", deliverycharge);
                   Buyparams.put("orginal", t_subtotalvalue.getText().toString());
                   Buyparams.put("cart_id", cart_id);
                   Call_paymentwebservice();
               }else
               {
                   new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.ERROR_TYPE)
                       .setTitleText("No internet")
                       .setContentText("Internet not available, Cross check your internet connectivity and try again")
                       .show();

               }

               }
       }
       else
       {
           new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.ERROR_TYPE)
                   .setTitleText("No internet")
                   .setContentText("Internet not available, Cross check your internet connectivity and try again")
                   .show();
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
                PaymentCompleteParams.put("totalamount",t_payamountvalue.getText().toString());
                PaymentCompleteParams.put("transactionid",transactionid);
                PaymentCompleteParams.put("user_id",userID);
                PaymentCompleteParams.put("paymentmode_id","app-"+orderid);
                PaymentCompleteParams.put("PaymentMode","Online Payment");
                PaymentCompleteParams.put("uniquedevice",android_id);
                PaymentCompleteParams.put("SpMerchantRefNo",SpMerchantRefNo);
                Call_afterpayment();
            }

            @Override
            public void onFailure(int code, String reason) {

                System.out.print("payment response Failed:"+reason.toString());
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();

                PaymentCompleteParams.put("appkey",appkey);
                PaymentCompleteParams.put("appsecurity",appsecurity);
                PaymentCompleteParams.put("paystatus","failed");
                PaymentCompleteParams.put("ResponseCode","1");
                PaymentCompleteParams.put("ResponseMessage","Online Payment Failed");
                PaymentCompleteParams.put("PaymentID","failed");
                PaymentCompleteParams.put("MerchantRefNo", String.valueOf(System.currentTimeMillis()));
                PaymentCompleteParams.put("paymenttype","Online Payment");
                PaymentCompleteParams.put("totalamount",t_payamountvalue.getText().toString());
                PaymentCompleteParams.put("transactionid","failed");
                PaymentCompleteParams.put("user_id",userID);
                PaymentCompleteParams.put("paymentmode_id","failed");
                PaymentCompleteParams.put("PaymentMode","Online Payment");
                PaymentCompleteParams.put("uniquedevice",android_id);
                PaymentCompleteParams.put("SpMerchantRefNo",SpMerchantRefNo);
                Call_afterpayment();


            }
        };
    }
    public void apply(View v)
    {

        ti_pin.setError(null);
        ti_vouchercode.setError(null);
        if(te_vouchercode.getText().toString().equals(""))
        {
            ti_vouchercode.setError("Enter valid voucher");

        }else if(te_pin.getText().toString().equals(""))
        {
            ti_pin.setError("Enter valid pin");
        }
        else
        {
            isNetworkConnected();
            if(NETCONNECTION==1)
            {
                VoucherParams.put("appkey",MainActivity.appkey);
                VoucherParams.put("appsecurity",MainActivity.appsecurity);
                VoucherParams.put("code",te_vouchercode.getText().toString());
                VoucherParams.put("pin",te_pin.getText().toString());

                call_applyVoucher();
            }
            else
            {
                new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No internet")
                        .setContentText("Internet not available, Cross check your internet connectivity and try again")
                        .show();
            }
        }
    }
    public void add_address(View v)
    {
        Intent in=new Intent(this,AddAddress.class);
        startActivity(in);
    }
    public void Call_paymentwebservice()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        System.out.println("Buy params"+new JSONObject(Buyparams));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Buyurl, new JSONObject(Buyparams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Buy Responce"+response.toString());
                        pDialog.dismiss();

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            if(jsonResponse.getString("status").equals("true"))
                            {
                                SpMerchantRefNo=jsonResponse.getString("SpMerchantRefNo");

                                if(t_payamountvalue.getText().toString().equals("0.00"))
                                {
                                    PaymentCompleteParams.put("appkey",MainActivity.appkey);
                                    PaymentCompleteParams.put("appsecurity",MainActivity.appsecurity);
                                    PaymentCompleteParams.put("paystatus","success");
                                    PaymentCompleteParams.put("ResponseCode","0");
                                    PaymentCompleteParams.put("ResponseMessage","Online Payment Success");
                                    PaymentCompleteParams.put("PaymentID","");
                                    PaymentCompleteParams.put("MerchantRefNo",String.valueOf(System.currentTimeMillis()));
                                    PaymentCompleteParams.put("paymenttype","Online Payment");
                                    PaymentCompleteParams.put("totalamount",t_payamountvalue.getText().toString());
                                    PaymentCompleteParams.put("transactionid",String.valueOf(System.currentTimeMillis()));
                                    PaymentCompleteParams.put("user_id",userID);
                                    PaymentCompleteParams.put("paymentmode_id","app-"+String.valueOf(System.currentTimeMillis()));
                                    PaymentCompleteParams.put("PaymentMode","Online Payment");
                                    PaymentCompleteParams.put("uniquedevice",android_id);
                                    PaymentCompleteParams.put("SpMerchantRefNo",SpMerchantRefNo);
                                    Call_afterpayment();
                                }else
                                {
                                    callInstamojoPay(email,phone,t_payamountvalue.getText().toString(),"DC BOOKS",name);
                                }

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
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    public void Call_afterpayment()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        System.out.println("Buy afterpayment"+new JSONObject(PaymentCompleteParams));

         jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Afterpayment_url, new JSONObject(PaymentCompleteParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Buy afterpayment"+response.toString());
                        pDialog.dismiss();

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            if(jsonResponse.getString("status").equals("true"))
                            {
                               Call_ClearCart();

                                Intent in=new Intent(OrderSummary.this,TransactionActivity.class);
                                in.putExtra("State","Success");
                                in.putExtra("Amount",t_payamountvalue.getText().toString());
                                startActivity(in);
                            }else
                            {
                                Intent in=new Intent(OrderSummary.this,TransactionActivity.class);
                                in.putExtra("State","Failed");
                                in.putExtra("Amount",t_payamountvalue.getText().toString());
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
                System.out.println("Error Responce---> "+error.toString());

            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    public void Call_ClearCart()
    {

        System.out.println("Buy params"+new JSONObject(Buyparams));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Clear_cart, new JSONObject(ClearCartparams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Buy Responce"+response.toString());

                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    public void Call_AddresList()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        AddressID.clear();
        Name.clear();
        Address.clear();
        arraylist.clear();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                addresslist_url, new JSONObject(AddressParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce"+response.toString());

                        pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("useraddresslist");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {

                                JSONObject jsonChild = jsonMain.getJSONObject(i);
                                String firstname=jsonChild.getString("firstname");
                                String address=jsonChild.getString("totaladdress");
                                AddressID.add(jsonChild.getString("id"));
                                Name.add(firstname);
                                Address.add(address);

                                ItemData itemsData = new ItemData(AddressID.get(i), Name.get(i),Address.get(i));
                                arraylist.add(itemsData);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        } catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }




                        rlv_addresslist.setLayoutManager(new LinearLayoutManager(OrderSummary.this));
                        mAdapter = new MyAdapter(OrderSummary.this, arraylist);
                        rlv_addresslist.setAdapter(mAdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void call_applyVoucher()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                voucherurl, new JSONObject(VoucherParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce"+response.toString());
                        pDialog.dismiss();

                        te_pin.getText().clear();
                        te_vouchercode.getText().clear();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            if(jsonResponse.getString("status").equals("true"))
                            {

                                voucheramount=jsonResponse.getString("amount");
                                DecimalFormat df = new DecimalFormat("#0.00");
                                if(Double.parseDouble(voucheramount)>Double.parseDouble(t_payamountvalue.getText().toString()))
                                {
                                    t_payamountvalue.setText("0.00");
                                    totoalpayamount=totoalpayamount-Double.parseDouble(voucheramount);
                                }
                                else
                                {
                                    totoalpayamount=totoalpayamount-Double.parseDouble(voucheramount);
                                    t_payamountvalue.setText(String.valueOf(df.format(totoalpayamount)));
                                }

                                te_pin.getText().clear();
                                te_vouchercode.getText().clear();


                                new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Applied gift voucher!")
                                        .show();
                            }
                            else
                            {
                                new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Failed!")
                                        .setContentText(jsonResponse.getString("message"))
                                        .show();

                            }





                        } catch (JSONException e) {
                            e.printStackTrace();

                        } catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }

                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public class ItemData {

        String address_id,name,address;
        String order_id,orderitem_name,order_qty,orderitem_image,orderitem_price;
        public ItemData(String AddressID,String Name,String Address){

            this.address_id = AddressID;
            this.name = Name;
            this.address = Address;
        }
        public ItemData(String OrderID,String OrderItemName,String OrderQty,String OrderImage,String OrderitemPrice){

            this.order_id = OrderID;
            this.orderitem_name = OrderItemName;
            this.order_qty = OrderQty;
            this.orderitem_image = OrderImage;
            this.orderitem_price=OrderitemPrice;
        }


        public String getaddress_id()
        {
            return this.address_id;
        }

        public String getname()
        {
            return this.name;
        }

        public String getaddress()
        {
            return this.address;
        }
        public String getorder_id()
        {
            return this.order_id;
        }
        public String getorderitem_name()
        {
            return this.orderitem_name;
        }
        public String getorder_qty()
        {
            return this.order_qty;
        }
        public String getorderitem_image()
        {
            return this.orderitem_image;
        }
        public String getorderitem_price()
        {
            return this.orderitem_price;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        ViewHolder viewHolder;
        Activity a;

        public MyAdapter(FragmentActivity activity, ArrayList<ItemData> ItemsData) {
            AddressList = ItemsData;
            a = activity;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.addresslist_adapter, null);
            viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            viewHolder.t_name.setTypeface(font3);
            viewHolder.t_address.setTypeface(font);

            viewHolder.t_name.setText(AddressList.get(position).getname());
            viewHolder.t_address.setText(AddressList.get(position).getaddress());

            if(selectaddressid.equals(AddressList.get(position).getaddress_id()))
            {
                viewHolder.checkBox.setChecked(true);
                t_deliverycharge .setVisibility(View.VISIBLE);
                t_deliverychargevalue .setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.checkBox.setChecked(false);
            }



            viewHolder.relativeLayout.setVisibility(View.VISIBLE);



        }

        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView t_name,t_address;
            RelativeLayout relativeLayout;
            ImageButton bt_close,imgbutton_edit;
            CheckBox checkBox;



            @SuppressLint("WrongViewCast")
            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                t_name =itemLayoutView.findViewById(R.id.textView12);
                t_address=itemLayoutView.findViewById(R.id.textView17);
                relativeLayout=itemLayoutView.findViewById(R.id.relativeLayout);
                bt_close=itemLayoutView.findViewById(R.id.imageButton4);
                imgbutton_edit=itemLayoutView.findViewById(R.id.imageButton2);
                checkBox=itemLayoutView.findViewById(R.id.checkBox);

                itemLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });


                bt_close.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_UP){

                            delete(getAdapterPosition(),AddressList.get(getAdapterPosition()).getaddress_id());


                            return true;
                        }
                        return false;
                    }
                });

               imgbutton_edit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent in=new Intent(a,AdddressEdit.class);
                       in.putExtra("AddressID",AddressList.get(getAdapterPosition()).getaddress_id());
                       startActivity(in);
                   }
               });

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(checkBox.isChecked())
                        {
                            selectaddressid=AddressList.get(getAdapterPosition()).getaddress_id();
                            mAdapter = new MyAdapter(OrderSummary.this, arraylist);
                            rlv_addresslist.setAdapter(mAdapter);

                            DeliveryParams.put("appkey",MainActivity.appkey);
                            DeliveryParams.put("appsecurity",MainActivity.appsecurity);
                            DeliveryParams.put("id",selectaddressid);
                            DeliveryParams.put("totalproductweight",totalweight);
                            DeliveryCharge();

                            System.out.println("Responce"+getIntent().getExtras().getString("Weight"));

                        }
                        else
                        {
                            selectaddressid="";
                            t_deliverychargevalue.setText("FREE");
                            t_deliverycharge.setVisibility(View.GONE);
                            t_deliverychargevalue.setVisibility(View.GONE);
                            DecimalFormat df = new DecimalFormat("#0.00");

                            totoalpayamount=totoalpayamount-Delivery_charge;

                            if(totoalpayamount>0)
                            {
                                t_payamountvalue.setText(String.valueOf(df.format(totoalpayamount)));
                            }
                            else
                            {
                                t_payamountvalue.setText("0.00");
                            }

                            }
                    }
                });




            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return AddressList.size();
        }

        public void delete(int position,String ID) {

            remove_position=position;

            AddresDeleteParams.put("appkey",MainActivity.appkey);
            AddresDeleteParams.put("appsecurity",MainActivity.appsecurity);
            AddresDeleteParams.put("id",ID);
            Removeaddres();
        }
    }


    // Order items
    public void call_OrderItems()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        OrderID.clear();
        OrderItemName.clear();
        OrderQty.clear();
        OrderItemImages.clear();
        OrderItemPrice.clear();
        arraylist2.clear();

        System.out.println("json"+new JSONObject(OrderParams));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                orderitems_url, new JSONObject(OrderParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce"+response.toString());

                        pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            phone=jsonResponse.getString("mobile");
                            email=jsonResponse.getString("email");
                            name=jsonResponse.getString("name");
                            totalweight=jsonResponse.getString("totalproductweight");

                            JSONArray jsonMain = jsonResponse.getJSONArray("Appordersummarytview");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {

                                JSONObject jsonChild = jsonMain.getJSONObject(i);

                                cart_id=jsonChild.getString("mycartId");

                                OrderID.add(jsonChild.getString("id"));
                                OrderItemName.add(jsonChild.getString("title"));
                                OrderQty.add(jsonChild.getString("productquantity"));
                                OrderItemPrice.add(jsonChild.getString("orginalsellingprice"));
                                OrderItemImages.add("http://dcbookstore.tk/"+jsonChild.getString("image"));


                                DecimalFormat df = new DecimalFormat("#0.00");

                                //total_amountpaying=String.valueOf(df.format(Double.parseDouble(jsonChild.getString("totalpayamount"))));
                                t_payamountvalue.setText(String.valueOf(df.format(Double.parseDouble(jsonChild.getString("totalpayamount")))));
                                totoalpayamount=Double.parseDouble(jsonChild.getString("totalpayamount"));
                                t_subtotalvalue.setText(String.valueOf(df.format(Double.parseDouble(jsonChild.getString("subtotalproduct")))));


                                ItemData itemsData = new ItemData(OrderID.get(i), OrderItemName.get(i),OrderQty.get(i),OrderItemImages.get(i),OrderItemPrice.get(i));
                                arraylist2.add(itemsData);

                                }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        } catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }


                        rlv_orderitems.setLayoutManager(new LinearLayoutManager(OrderSummary.this));
                        mAdapter2 = new MyAdapter2(OrderSummary.this, arraylist2);
                        rlv_orderitems.setAdapter(mAdapter2);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
     public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
        private ArrayList<ItemData> OrderItemList = null;
        ViewHolder viewHolder;
        Activity a;

        public MyAdapter2(FragmentActivity activity, ArrayList<ItemData> ItemsData) {
            OrderItemList = ItemsData;
            a = activity;
        }

        @Override
        public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ordersummary_adapter, null);
            viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            viewHolder.t_itemname.setTypeface(font);
            viewHolder.t_qty.setTypeface(font3);
            viewHolder.t_size.setTypeface(font3);

            viewHolder.t_itemname.setText(OrderItemList.get(position).getorderitem_name());
            viewHolder.t_qty.setText(OrderItemList.get(position).getorder_qty());

            viewHolder.t_size.setText(OrderItemList.get(position).getorderitem_price());

            Glide.with(OrderSummary.this)
                    .load(OrderItemList.get(position).getorderitem_image()).into(viewHolder.img_itemimage);

            if(position==OrderID.size()-1)
            {
                viewHolder.relativeLayout.setVisibility(View.INVISIBLE);
            }


        }

        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView t_itemname,t_qty,t_size;
            ImageView img_itemimage;
            RelativeLayout relativeLayout;




            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                t_itemname =itemLayoutView.findViewById(R.id.textView24);
                t_qty=itemLayoutView.findViewById(R.id.textView26);
                t_size=itemLayoutView.findViewById(R.id.textView44);
                img_itemimage=itemLayoutView.findViewById(R.id.imageView4);
                relativeLayout=itemLayoutView.findViewById(R.id.recycleview);


                itemLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                    }
                });


            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return OrderItemList.size();
        }
    }
    public void Removeaddres()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                addressdelete_url, new JSONObject(AddresDeleteParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce---> "+response.toString());
                        pDialog.dismissWithAnimation();

                        try {

                            if (response.getString("status").equals("true")) {

                                DecimalFormat df = new DecimalFormat("#0.00");
                                t_deliverychargevalue.setText("FREE");
                                t_payamountvalue.setText(String.valueOf(df.format(Double.parseDouble(t_subtotalvalue.getText().toString()))));



                                SweetAlertDialog AlertDialog= new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.SUCCESS_TYPE);
                                AlertDialog.setCancelable(false);
                                AlertDialog.setTitleText("Removed")
                                        .setContentText(response.getString("result"))
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                try {
                                                    AddressList.remove(remove_position);
                                                    AddressID.remove(remove_position);
                                                    Address.remove(remove_position);
                                                    mAdapter.notifyItemRemoved(remove_position);

                                                    if(AddressID.size()==0)
                                                    {
                                                        edit.putString("UserAddress","0");
                                                        edit.commit();
                                                    }

                                                    sDialog.dismissWithAnimation();
                                                }catch (IndexOutOfBoundsException e)
                                                {
                                                    sDialog.dismissWithAnimation();
                                                }
                                            }
                                        }).show();



                            }
                            else
                            {
                                new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops")
                                        .setContentText(response.getString("result"))
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        }).show();
                            }
                        }catch (JSONException E)
                        {
                            E.printStackTrace();
                        }catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    public void DeliveryCharge()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        System.out.println("wowwwwwwwwww "+DeliveryParams.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                deliverycharge_url, new JSONObject(DeliveryParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce---> "+response.toString());
                        pDialog.dismissWithAnimation();

                        try {
                            DecimalFormat df = new DecimalFormat("#0.00");

                            deliverycharge=response.getString("Delivercharge");

                            t_deliverychargevalue.setText(String.valueOf(df.format(Double.parseDouble(response.getString("Delivercharge")))));

                            double deliverycharge=0;
                            Delivery_charge=Double.parseDouble(response.getString("Delivercharge"));
                            deliverycharge=Double.parseDouble(t_deliverychargevalue.getText().toString());
                            totoalpayamount=totoalpayamount+deliverycharge;

                            if(totoalpayamount>0)
                            {
                                t_payamountvalue.setText(String.valueOf(df.format(totoalpayamount)));
                            }
                            else
                            {
                                t_payamountvalue.setText("0.00");
                            }



                        }catch (JSONException E)
                        {
                            E.printStackTrace();
                        }catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    @Override
    public void onResume(){
        super.onResume();

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");


        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            AddressParams.put("appkey",MainActivity.appkey);
            AddressParams.put("appsecurity",MainActivity.appsecurity);
            AddressParams.put("user_id",userID);
            Call_AddresList();


            if(!selectaddressid.equals(""))
            {
                DeliveryParams.put("appkey",MainActivity.appkey);
                DeliveryParams.put("appsecurity",MainActivity.appsecurity);
                DeliveryParams.put("id",selectaddressid);
                DeliveryParams.put("totalproductweight",totalweight);
                DeliveryCharge();
            }







        }
        else
        {
            new SweetAlertDialog(OrderSummary.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Cross check your internet connectivity and try again")
                    .show();
        }




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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:

                OrderSummary.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
