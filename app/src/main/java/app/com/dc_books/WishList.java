package app.com.dc_books;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class WishList extends AppCompatActivity {

    RecyclerView rv_wish_list;
    Map<String, String> WishListParams = new HashMap<>();
    Map<String, String> WishListDeleteParams = new HashMap<>();
    String ip_head = "http://www.level10boutique.com/";
    String wish_list_url = ip_head + "admin/services/Appwishlistview";
    String wish_list_remove_url = ip_head + "admin/services/Appdeletewishlist";
    ArrayList<WishListItems> WishListArray = new ArrayList<>();
    TextView tv_wishListTitle;
    MyAdapter wishlistAdapter;
    LinearLayoutManager layoutManager;
    Toolbar toolbar;
    int NETCONNECTION;
    public static final String mp = "";
    String user_id;
    RelativeLayout rl_searchLayout;
    int SearchlayoutVisibleState = 0;
    SearchView et_search;
    ImageButton ib_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        Typeface custom_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        rv_wish_list = findViewById(R.id.rv_wish_list);
        tv_wishListTitle=findViewById(R.id.tv_WishListTitle);
        tv_wishListTitle.setTypeface(custom_bold);
        rl_searchLayout=findViewById(R.id.relativeLayout2);
        et_search = findViewById(R.id.editText);
        ib_profile=findViewById(R.id.ib_profile);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");

        et_search.setQueryHint("What are you looking for?");
        et_search.setIconified(false);
        int searchIconId = et_search.getContext().getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = (ImageView) et_search.findViewById(searchIconId);
        searchIcon.setImageResource(R.mipmap.searchicon);

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginCheck();
        SharedPreferences s = getSharedPreferences(WishList.mp, 0);
         user_id = s.getString("User_id", "");

        WishListParams.put("appkey", "TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc");
        WishListParams.put("appsecurity", "TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==");
        WishListParams.put("user_id", user_id);

        WishListDeleteParams.put("appkey", "TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc");
        WishListDeleteParams.put("appsecurity", "TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==");
        WishListDeleteParams.put("user_id", user_id);

        et_search.clearFocus();
        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent in = new Intent(WishList.this, ProductListing.class);
                in.putExtra("Keyword", query);
                in.putExtra("Identifier", "0");
                startActivity(in);

                return false;
            }

        });

        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            WishList();
        }
        else
        {
            new SweetAlertDialog(WishList.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Check your internet connectivity and try again")
                    .show();
        }
    }
    public void cart(View v)
    {
        Intent in=new Intent(this,Cart.class);
        in.putExtra("Identifier", "7");
        startActivity(in);
    }
    //Profile onclick
    public void account(View v)
    {
        if(user_id.equals("")) {
            Intent in=new Intent(this,LoginActivity.class);
            in.putExtra("Identifier","2");
            startActivity(in);
        }
        else
        {
            Intent in=new Intent(this,Profile.class);
            startActivity(in);
        }

    }

    public void search(View v){

        if (SearchlayoutVisibleState == 0) {


            rl_searchLayout.setVisibility(View.VISIBLE);
            SearchlayoutVisibleState = 1;

        } else {
            rl_searchLayout.setVisibility(View.GONE);
            SearchlayoutVisibleState = 0;


        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<WishListItems> values;
        Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        Typeface custom_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");

        //        Typeface custom_semi_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item,
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView tvProdTitle, tvProdPrice;
            ImageButton ibCloseButton;
            ImageView ivMainImage;
            Button btBuy;

            //            public TextView txtFooter;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                tvProdTitle = v.findViewById(R.id.tv_product_title);
                tvProdPrice = v.findViewById(R.id.tv_product_price);
                ivMainImage = v.findViewById(R.id.iv_MainImage);
                ibCloseButton = v.findViewById(R.id.ib_close);
                btBuy = v.findViewById(R.id.bt_buy_now);
//                txtFooter = (TextView) v.findViewById(R.id.secondLine);
                btBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(WishList.this, ProductDetails.class);
                        in.putExtra("ProdID", values.get(getAdapterPosition()).getProdId());
                        startActivity(in);
                    }
                });
            }
        }

        public void add(int position, WishListItems item) {
            values.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            values.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<WishListItems> myDataset) {
            values = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.wish_list_recycler_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            String prodTitle = values.get(position).getProdTitle();
            String mainImgUrl = ip_head + values.get(position).getMainImg();
            String prodPrice = values.get(position).getProdPrice();
            String status=values.get(position).getPstatus();
            String stock=values.get(position).getPstock();
            final String prodId = values.get(position).getProdId();

            holder.tvProdTitle.setText(prodTitle);
            holder.tvProdTitle.setTypeface(custom_regular);
            holder.tvProdPrice.setText("Rs. "+prodPrice);
            holder.tvProdPrice.setTypeface(custom_bold);
            holder.btBuy.setTypeface(custom_bold);

            if(stock.equals("1")&&status.equals("1")){
                holder.btBuy.setText("BUY NOW");
            }else if(stock.equals("0")&&status.equals("1")){
                holder.btBuy.setText("OUT OF STOCK");
                holder.btBuy.setClickable(false);
            }else{
                holder.btBuy.setText("NOT AVAILABLE");
                holder.btBuy.setClickable(false);
            }

            Glide.with(WishList.this)
                    .load(mainImgUrl)
                    .into(holder.ivMainImage);

            holder.ibCloseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WishListDeleteParams.put("product_id", values.get(position).getProdId());
                    WishlistDelete();
                    remove(position);
             }
            });

//            holder.txtFooter.setText("Footer: " + name);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return values.size();
        }

    }

    public void WishList(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                wish_list_url, new JSONObject(WishListParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Search Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = jsonResponse.getJSONArray("wishviewlist");
//                            String prodCount= String.valueOf(resultsArray.length())+" Products";
//                            tvProductCount.setText(prodCount);
                            //Test
                            if(resultsArray.length()<1) {
                                new SweetAlertDialog(WishList.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Empty")
                                        .setContentText("Your wishlist is empty")
                                        .show();
                            }
                            for (int i=0; i<resultsArray.length(); i++){
                                String id=resultsArray.getJSONObject(i).getString("id");
                                String prodId=resultsArray.getJSONObject(i).getString("product_id");
                                String mainImg=resultsArray.getJSONObject(i).getString("image");
                                String prodTitle=resultsArray.getJSONObject(i).getString("productname");
                                String prodPrice=resultsArray.getJSONObject(i).getString("sellingprice");
                                String status=resultsArray.getJSONObject(i).getString("status");
                                String stock=resultsArray.getJSONObject(i).getString("stock");
                                WishListItems obj=new WishListItems(
                                        id,
                                        prodId,
                                        mainImg,
                                        prodTitle,
                                        prodPrice,
                                        status,
                                        stock
                                );
                                WishListArray.add(obj);
                            }
                            Log.d("dsjfkaads",WishListArray.toString());
                            layoutManager = new LinearLayoutManager(WishList.this);
                            rv_wish_list.setLayoutManager(layoutManager);
                            wishlistAdapter=new MyAdapter(WishListArray);
                            rv_wish_list.setAdapter(wishlistAdapter);

//                            String Status=jsonResponse.getString("status");
//                            String Message=jsonResponse.getString("result");
//
//                            if(Status.equals("true"))
//                            {

//                                et_login_email.getText().clear();
//                                et_login_pass.getText().clear();

//                                edit.putString("Username",jsonResponse.getString("name"));
//                                edit.putString("User_id",jsonResponse.getString("userId"));
//                                edit.putString("Login_Status","success");
//                                edit.putString("Firsttime","YES");
//                                edit.commit();
//
//                                Intent in=new Intent(ProductListing.this,MainActivity.class);
//                                startActivity(in);
//                                finish();
//
//                            }
//                            else
//                            {
//                                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("Oops...")
//                                        .setContentText(Message)
//                                        .show();
//                            }

                        } catch (JSONException e) {
                            Log.d("JSONExceptionLogin",e.toString());
                            e.printStackTrace();
                            pDialog.dismiss();

                        }
                        catch (NullPointerException e)
                        {
                            Log.d("NullExceptionLogin",e.toString());
                            e.printStackTrace();
                            pDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                System.out.println("Search Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void WishlistDelete(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                wish_list_remove_url, new JSONObject(WishListDeleteParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Wishlist Delete Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("result");

                            if(Status.equals("true"))
                            {
                                new SweetAlertDialog(WishList.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Removed")
                                        .setContentText(Message)
                                        .show();

//                                et_login_email.getText().clear();
//                                et_login_pass.getText().clear();

//                                edit.putString("Username",jsonResponse.getString("name"));
//                                edit.putString("User_id",jsonResponse.getString("userId"));
//                                edit.putString("Login_Status","success");
//                                edit.putString("Firsttime","YES");
//                                edit.commit();
//
//                                Intent in=new Intent(ProductListing.this,MainActivity.class);
//                                startActivity(in);
//                                finish();

                            }
                            else
                            {
                                new SweetAlertDialog(WishList.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(Message)
                                        .show();
                            }

                        } catch (JSONException e) {
                            Log.d("JSONExceptionLogin",e.toString());
                            e.printStackTrace();
                            pDialog.dismiss();

                        }
                        catch (NullPointerException e)
                        {
                            Log.d("NullExceptionLogin",e.toString());
                            e.printStackTrace();
                            pDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                System.out.println("Wishlist Delete Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public class WishListItems{
        String id,prodId,mainImg,prodTitle,prodPrice,pstock,pstatus;

        public WishListItems(String id,String prodId, String mainImg, String prodTitle, String prodPrice,String pstatus,String pstock) {
            this.id = id;
            this.prodId = prodId;
            this.mainImg = mainImg;
            this.prodTitle = prodTitle;
            this.prodPrice = prodPrice;
            this.pstatus=pstatus;
            this.pstock=pstock;
        }

        public String getProdId() {
            return prodId;
        }

        public String getMainImg() {
            return mainImg;
        }

        public String getProdTitle() {
            return prodTitle;
        }

        public String getProdPrice() {
            return prodPrice;
        }

        public String getId() {
            return id;
        }

        public String getPstock() {
            return pstock;
        }

        public String getPstatus() {
            return pstatus;
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

                WishList.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void loginCheck(){
        SharedPreferences s = getSharedPreferences(ProductListing.mp, 0);
        user_id = s.getString("User_id", "");
        if (!(user_id.equals(""))){
            ib_profile.setImageResource(R.mipmap.topicon04);
        }else {
            ib_profile.setImageResource(R.mipmap.topiconaccount);
        }
    }
}
