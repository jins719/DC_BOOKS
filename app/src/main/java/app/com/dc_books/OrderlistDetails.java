package app.com.dc_books;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
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

public class OrderlistDetails extends AppCompatActivity {

    Map<String, String> OrderParams = new HashMap<String, String>();
    RecyclerView rcv_orderlist;
    Toolbar toolbar;
    ArrayList<ItemData> arraylist = new ArrayList<ItemData>();
    MyAdapter mAdapter;
    String userID;
    LinearLayoutManager layoutManager;
    Typeface font,font2,font3;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderlistdetails);

        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");

        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        rcv_orderlist=findViewById(R.id.recycleview);

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");

        OrderParams.put("appkey",MainActivity.appkey);
        OrderParams.put("appsecurity",MainActivity.appsecurity);
        OrderParams.put("user_id",userID);
        OrderParams.put("ordernumber",getIntent().getStringExtra("Ordernumber"));
        Call_OrderList();

    }

    private void Call_OrderList() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://dcbookstore.tk/api/order/order_details", new JSONObject(OrderParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("cccccccc "+response.toString());


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
                                String price = jsonChild.getString("sellingprice");
                                String image= "https://dcbookstore.tk/"+jsonChild.getString("image");

                                ItemData itemsData = new ItemData(id,productname,orderstatusname,orderstatus,date,image,price);
                                arraylist.add(itemsData);

                            }
                            if(lengthJsonArr==0)
                            {
                                new SweetAlertDialog(OrderlistDetails.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Order list is empty")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                //finish();
                                            }
                                        })
                                        .show();
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


                        layoutManager = new LinearLayoutManager(OrderlistDetails.this);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rcv_orderlist.setLayoutManager(layoutManager);
                        mAdapter = new MyAdapter(arraylist);
                        rcv_orderlist.setAdapter(mAdapter);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcv_orderlist.getContext(), layoutManager.getOrientation());
                        rcv_orderlist.addItemDecoration(dividerItemDecoration);
                        rcv_orderlist.setItemAnimator(new DefaultItemAnimator());


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // pDialog.dismiss();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");


    }
    public class ItemData {

        String id,productname,orderstatusname,orderstatus,date,image,price;
        public ItemData(String ID,String ProductName,String Orderstatusname,String Orderstatus,String Date,String Image,String Price){

            this.id = ID;
            this.productname = ProductName;
            this.orderstatusname = Orderstatusname;
            this.orderstatus=Orderstatus;
            this.date = Date;
            this.image = Image;
            this.price = Price;

        }
        public String getid()
        {
            return this.id;
        }
        public String getproductname()
        {
            return this.productname;
        }
        public String getorderstatusname()
        {
            return this.orderstatusname;
        }
        public String getdate()
        {
            return this.date;
        }
        public String getimage()
        {
            return this.image;
        }
        public String getorderstatus()
        {
            return this.orderstatus;
        }
        public String getprice()
        {
            return this.price;
        }


    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<ItemData> orderlist = null;
        MyAdapter.ViewHolder viewHolder;
        Activity a;

        public MyAdapter(ArrayList<ItemData> ItemsData) {
            orderlist = ItemsData;
            //  a = activity;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.orderlistdetails_adapter, null);
            viewHolder = new MyAdapter.ViewHolder(itemLayoutView);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int position) {


            viewHolder.productname.setTypeface(font2);
            viewHolder.orderstatus.setTypeface(font);
            viewHolder.date.setTypeface(font);

            String aa=orderlist.get(position).getorderstatusname();
            System.out.println("AAAAAAAAAAA"+aa);

            if(orderlist.get(position).getorderstatus().equals("4"))
            {
                viewHolder.orderstatus.setTextColor(Color.parseColor("#008000"));
              //  viewHolder.date.setTextColor(Color.parseColor("#008000"));
            }
            else if(orderlist.get(position).getorderstatus().equals("1"))
            {
                viewHolder.orderstatus.setTextColor(Color.parseColor("#008000"));
               // viewHolder.date.setTextColor(Color.parseColor("#008000"));
                viewHolder.view.setVisibility(View.GONE);

            }
            else
            {
                viewHolder.orderstatus.setTextColor(Color.parseColor("#008000"));
                //viewHolder.date.setTextColor(Color.parseColor("#ff0000"));
                viewHolder.view.setVisibility(View.GONE);
            }

            DecimalFormat df = new DecimalFormat("#0.00");


            viewHolder.productname.setText(orderlist.get(position).getproductname());
            viewHolder.orderstatus.setText(orderlist.get(position).getorderstatusname());
            viewHolder.date.setText(orderlist.get(position).getdate());
            viewHolder.price.setText("Rs. "+ String.valueOf(df.format(Double.parseDouble(orderlist.get(position).getprice()))));
            Glide.with(OrderlistDetails.this)
                    .load(orderlist.get(position).getimage()).into(viewHolder.product_img);




        }

        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView productname,orderstatus,date,price;
            public ImageView product_img;
            public Button view;



            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                productname =itemLayoutView.findViewById(R.id.textView37);
                orderstatus=itemLayoutView.findViewById(R.id.textView39);
                date=itemLayoutView.findViewById(R.id.textView40);
                price=itemLayoutView.findViewById(R.id.textView73);
                product_img=itemLayoutView.findViewById(R.id.imageView15);
                view=itemLayoutView.findViewById(R.id.button8);


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent in=new Intent(OrderlistDetails.this,OrderlistDetails.class);
                        startActivity(in);
                    }
                });
            }
        }// Return the size of your itemsData (invoked by the layout manager)

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return orderlist.size();
        }
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

                OrderlistDetails.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
