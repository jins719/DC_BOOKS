package app.com.dc_books;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
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

/**
 * Created by JINS on 10/13/2017.
 */

public class OrderSummary extends AppCompatActivity {

    Typeface font,font2,font3;
    String addresslist_url="http://www.level10boutique.com/admin/services/Appuseraddresslist";
    String orderitems_url="http://www.level10boutique.com/admin/services/Appordersummarylist";
    String addressdelete_url="http://www.level10boutique.com/admin/services/Appuseraddressdelete";
    String deliverycharge_url="http://www.level10boutique.com/admin/services/Appdelivercharge";

    Map<String, String> AddressParams = new HashMap<String, String>();
    Map<String, String> OrderParams = new HashMap<String, String>();
    Map<String, String> AddresDeleteParams = new HashMap<String, String>();
    Map<String, String> DeliveryParams = new HashMap<String, String>();

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
    ArrayList<String>OrderItemSize=new ArrayList<>();
    
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

       /* if (scroller != null) {

            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i("", "Scroll DOWN");
                        cardView2.setVisibility(View.GONE);
                    }
                    if (scrollY < oldScrollY) {
                        Log.i("", "Scroll UP");
                        cardView2.setVisibility(View.GONE);
                    }

                    if (scrollY == 0) {
                        Log.i("", "TOP SCROLL");
                        cardView2.setVisibility(View.VISIBLE);


                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                       // cardView2.setVisibility(View.GONE);

                    }
                }
            });
        }*/


        //Back button set in toolbar

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
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        OrderParams.put("appkey",appkey);
        OrderParams.put("appsecurity",appsecurity);
        OrderParams.put("uniquedevice",android_id);
        OrderParams.put("user_id",userID);
        OrderParams.put("subtotalproduct",getIntent().getExtras().getString("subtotal"));
        OrderParams.put("totalpayamount",getIntent().getExtras().getString("totalpayamount"));
        OrderParams.put("vouchername",getIntent().getExtras().getString("vouchername"));
        OrderParams.put("voucheramount",getIntent().getExtras().getString("voucheramount"));
        OrderParams.put("totalprodductweight",getIntent().getExtras().getString("Weight"));
        call_OrderItems();






    }
    public void add_address(View v)
    {
        Intent in=new Intent(this,AddAddress.class);
        startActivity(in);
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

    public class ItemData {

        String address_id,name,address;
        String order_id,orderitem_name,order_qty,orderitem_image,orderitem_size;
        public ItemData(String AddressID,String Name,String Address){

            this.address_id = AddressID;
            this.name = Name;
            this.address = Address;
        }
        public ItemData(String OrderID,String OrderItemName,String OrderQty,String OrderImage,String OrderitemSize){

            this.order_id = OrderID;
            this.orderitem_name = OrderItemName;
            this.order_qty = OrderQty;
            this.orderitem_image = OrderImage;
            this.orderitem_size=OrderitemSize;
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
        public String getorderitem_size()
        {
            return this.orderitem_size;
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

                            DeliveryParams.put("appkey",appkey);
                            DeliveryParams.put("appsecurity",appsecurity);
                            DeliveryParams.put("id",selectaddressid);
                            DeliveryParams.put("totalproductweight",getIntent().getExtras().getString("Weight"));

                            System.out.println("Responce"+getIntent().getExtras().getString("Weight"));
                            DeliveryCharge();
                        }
                        else
                        {
                            selectaddressid="";
                            t_deliverychargevalue.setText("FREE");
                            t_deliverycharge.setVisibility(View.GONE);
                            t_deliverychargevalue.setVisibility(View.GONE);
                            DecimalFormat df = new DecimalFormat("#0.00");
                            t_payamountvalue.setText(String.valueOf(df.format(Double.parseDouble(t_subtotalvalue.getText().toString()))));
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

            AddresDeleteParams.put("appkey",appkey);
            AddresDeleteParams.put("appsecurity",appsecurity);
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
        OrderItemSize.clear();
        arraylist2.clear();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                orderitems_url, new JSONObject(OrderParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce"+response.toString());
                        System.out.println("json"+new JSONObject(OrderParams));
                        pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("Appordersummarytview");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {

                                JSONObject jsonChild = jsonMain.getJSONObject(i);

                                OrderID.add(jsonChild.getString("id"));
                                OrderItemName.add(jsonChild.getString("title"));
                                OrderQty.add(jsonChild.getString("productquantity"));
                                OrderItemSize.add(jsonChild.getString("productsize"));
                                OrderItemImages.add("http://www.level10boutique.com/"+jsonChild.getString("image"));


                                DecimalFormat df = new DecimalFormat("#0.00");

                                t_payamountvalue.setText(String.valueOf(df.format(Double.parseDouble(jsonChild.getString("totalpayamount")))));
                                t_subtotalvalue.setText(String.valueOf(df.format(Double.parseDouble(jsonChild.getString("subtotalproduct")))));


                                ItemData itemsData = new ItemData(OrderID.get(i), OrderItemName.get(i),OrderQty.get(i),OrderItemImages.get(i),OrderItemSize.get(i));
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

            viewHolder.t_size.setText(OrderItemList.get(position).getorderitem_size());

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
               // pDialog.dismiss();
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                deliverycharge_url, new JSONObject(DeliveryParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce---> "+response.toString());
                        pDialog.dismissWithAnimation();

                        try {
                            DecimalFormat df = new DecimalFormat("#0.00");

                            t_deliverychargevalue.setText(String.valueOf(df.format(Double.parseDouble(response.getString("Delivercharge")))));

                            double payamount;
                            double deliverycharge;
                            double totalpayamount;

                            payamount=Double.parseDouble(t_subtotalvalue.getText().toString());
                            deliverycharge=Double.parseDouble(t_deliverychargevalue.getText().toString());
                            totalpayamount=payamount+deliverycharge;

                            t_payamountvalue.setText(String.valueOf(df.format(totalpayamount)));

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
                // pDialog.dismiss();
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
            AddressParams.put("appkey",appkey);
            AddressParams.put("appsecurity",appsecurity);
            AddressParams.put("user_id",userID);
            Call_AddresList();




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
