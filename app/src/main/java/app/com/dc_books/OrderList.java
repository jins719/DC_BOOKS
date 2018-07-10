package app.com.dc_books;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by JINS on 10/31/2017.
 */

public class OrderList extends AppCompatActivity {

    TextView t_header;
    Typeface font,font2,font3;
    Map<String, String> OrderParams = new HashMap<String, String>();
    RecyclerView rcv_orderlist;
    Toolbar toolbar;
    int NETCONNECTION;
    int SearchlayoutVisibleState=0;
    RelativeLayout rtv_searchlayout;
    SearchView et_search;



    String userID;
    LinearLayoutManager layoutManager;

    ArrayList<ItemData> arraylist = new ArrayList<ItemData>();
    MyAdapter mAdapter;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    ImageButton img_account;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderlist);

        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");

        rcv_orderlist=findViewById(R.id.recycleview);
        rtv_searchlayout=findViewById(R.id.relativeLayout2);
        t_header=findViewById(R.id.textView31);
        t_header.setTypeface(font3);


        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");

        et_search=findViewById(R.id.editText);
        img_account=findViewById(R.id.account);
        et_search.setQueryHint("What are you looking for?");
        et_search.setIconified(false);
        int searchIconId = et_search.getContext().getResources().getIdentifier("android:id/search_button",null, null);
        ImageView searchIcon = (ImageView) et_search.findViewById(searchIconId);
        searchIcon.setImageResource(R.mipmap.searchicon);

        rtv_searchlayout.setVisibility(View.GONE);


        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            OrderParams.put("appkey",MainActivity.appkey);
            OrderParams.put("appsecurity",MainActivity.appsecurity);
            OrderParams.put("user_id",userID);


            Call_OrderList();
        }else
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Cross check your internet connectivity and try again")
                    .show();
        }

        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent in=new Intent(OrderList.this,ProductListing.class);
                in.putExtra("Keyword",query);
                in.putExtra("Identifier","0");
                startActivity(in);

                rtv_searchlayout.setVisibility(View.GONE);
                SearchlayoutVisibleState = 0;
                et_search.clearFocus();
                et_search.setQuery("", false);

                return false;
            }

        });


    }
    public void search(View v)
    {


        if(SearchlayoutVisibleState==0)
        {


            rtv_searchlayout.setVisibility(View.VISIBLE);
            SearchlayoutVisibleState=1;

        }
        else
        {
            rtv_searchlayout.setVisibility(View.GONE);
            SearchlayoutVisibleState=0;



        }


    }
    //Cart onclick
    public void cart(View v)
    {
        Intent in=new Intent(this,Cart.class);
        in.putExtra("Identifier", "7");
        startActivity(in);
    }
    //Profile onclick
    public void account(View v)
    {
        if(userID.equals("")) {
            Intent intent01 = new Intent(OrderList.this, LoginActivity.class);
            intent01.putExtra("Identifier", "2");
            startActivity(intent01);
        }
        else
        {
            Intent in=new Intent(this,Profile.class);
            startActivity(in);
        }
    }

    private void Call_OrderList() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://dcbookstore.tk/api/order/order_listing", new JSONObject(OrderParams),
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
                                String ordernumber=jsonChild.getString("ordernumber");
                                String image= "https://dcbookstore.tk/"+jsonChild.getString("image");

                                ItemData itemsData = new ItemData(id,productname,orderstatusname,orderstatus,date,image,ordernumber);
                                arraylist.add(itemsData);

                            }
                            if(lengthJsonArr==0)
                            {
                                new SweetAlertDialog(OrderList.this, SweetAlertDialog.ERROR_TYPE)
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


                        layoutManager = new LinearLayoutManager(OrderList.this);
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

        String id,productname,orderstatusname,orderstatus,date,image,orderid;
        public ItemData(String ID,String ProductName,String Orderstatusname,String Orderstatus,String Date,String Image,String Ordernumber){

            this.id = ID;
            this.productname = ProductName;
            this.orderstatusname = Orderstatusname;
            this.orderstatus=Orderstatus;
            this.date = Date;
            this.image = Image;
            this.orderid=Ordernumber;
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
        public String getorderid()
        {
            return this.orderid;
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
                    .inflate(R.layout.orderlist_adapter, null);
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
                //viewHolder.date.setTextColor(Color.parseColor("#008000"));
            }
            else if(orderlist.get(position).getorderstatus().equals("1"))
            {
                viewHolder.orderstatus.setTextColor(Color.parseColor("#008000"));
               // viewHolder.date.setTextColor(Color.parseColor("#ff0000"));
            }

            viewHolder.productname.setText(orderlist.get(position).getproductname());
            viewHolder.orderstatus.setText(orderlist.get(position).getorderstatusname());
            viewHolder.date.setText(orderlist.get(position).getdate());
            Glide.with(OrderList.this)
                    .load(orderlist.get(position).getimage()).into(viewHolder.product_img);




        }

        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView productname,orderstatus,date;
            public ImageView product_img;
            public Button ViewOrder;



            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                productname =itemLayoutView.findViewById(R.id.textView37);
                orderstatus=itemLayoutView.findViewById(R.id.textView39);
                date=itemLayoutView.findViewById(R.id.textView40);
                product_img=itemLayoutView.findViewById(R.id.imageView15);
                ViewOrder=itemLayoutView.findViewById(R.id.button8);

                itemLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent in=new Intent(OrderList.this,OrderlistDetails.class);
                        in.putExtra("Ordernumber",orderlist.get(getAdapterPosition()).getorderid());
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

                OrderList.this.finish();
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
        userID=s.getString("User_id","");


        if(userID.equals(""))
        {
            img_account.setImageResource(R.mipmap.topiconaccount);
        }
        else
        {
            img_account.setImageResource(R.mipmap.topicon04);
        }

    }


}
