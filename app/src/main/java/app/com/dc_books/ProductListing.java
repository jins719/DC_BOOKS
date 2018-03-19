package app.com.dc_books;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductListing extends AppCompatActivity {
    ArrayList<String> testArray=new ArrayList<>();
    ArrayList<ProductItems> serviceDemo=new ArrayList<>();
    ArrayList<ProductItems> ProductArray=new ArrayList<>();
    RecyclerView rv_product_listing;
    GridLayoutManager gridManager;
    TextView tv_ProductCategory;
    Map<String, String> ProductParams = new HashMap<>();
    Map<String, String> SortParams = new HashMap<>();
    Map<String, String> WishlistAddParams = new HashMap<>();
    Map<String, String> WishlistDeleteParams = new HashMap<>();
    Map<String, String> SearchParams = new HashMap<>();
    Map<String, String> FilterParams = new HashMap<>();
    int NETCONNECTION;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";
    Toolbar toolbar;
    String ip_head = "http://192.168.1.7:8080/dcbooks";
    String product_url = ip_head+"/api/product/list_by_category";
    String wishlist_add_url = ip_head+"admin/services/Appaddwishlist";
    String sort_url = ip_head+"admin/services/Appsearchsorting";
    String wishlist_delete_url = ip_head+"admin/services/Appdeletewishlist";

    String search_url =ip_head+"admin/services/Apphomesearchlist";
    String filter_url =ip_head+"admin/services/Appfilterresult";
    MyAdapter productAdapter;
    RelativeLayout subToolbar,subToolbarDivider,sortlayout;
    int SORTLAYOUT_STATE=1;
    TextView t_high_to_low,t_low_to_high,t_relevance;
    ImageView img_high_to_low,img_low_to_high,img_revelance;
    Typeface font2;
    ImageButton ib_profile;
    int SearchlayoutVisibleState=0;
    RelativeLayout rtv_searchlayout;

    SearchView et_search;
    String cat_id;
    String identifier;
    String login_status,prodCount;
    String user_id ,adpaterRefresh="0";
    boolean mIsReceiverRegistered = false;
    MyBroadcastReceiver mReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);

        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");

        Bundle extras = getIntent().getExtras();
        cat_id="0";
        identifier="none";
        String keyword = "none";
        String attribute_id = "none";
        String attributevalue_id = "none";

        if (extras != null) {
            cat_id = extras.getString("CatID");
            identifier= extras.getString("Identifier");
            keyword = extras.getString("Keyword");
            attribute_id = extras.getString("attrid");
            attributevalue_id = extras.getString("attrivalue");
        }
        subToolbar=findViewById(R.id.rl_sec_toolbar);
        subToolbarDivider=findViewById(R.id.divider2);

        Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        rv_product_listing=findViewById(R.id.rv_product_listing);
        ib_profile=findViewById(R.id.ib_profile);
        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();




        SharedPreferences s = getSharedPreferences(ProductListing.mp, 0);
        user_id = s.getString("User_id", "");

        loginCheck();

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }



        tv_ProductCategory=findViewById(R.id.tv_product_count);
        //tv_ProductCategory.setTypeface(custom_bold);
        gridManager = new GridLayoutManager(this,2);
        rv_product_listing.setLayoutManager(gridManager);
        sortlayout=findViewById(R.id.relativeLayout);
        t_high_to_low=findViewById(R.id.textView29);
        t_low_to_high=findViewById(R.id.textView27);
        img_high_to_low=findViewById(R.id.imageView8);
        img_low_to_high=findViewById(R.id.imageView6);
        t_relevance=findViewById(R.id.textView30);
        img_revelance=findViewById(R.id.imageView11);
        rtv_searchlayout=findViewById(R.id.relativeLayout2);
        t_relevance.setTypeface(font2);
        t_high_to_low.setTypeface(font2);
        t_low_to_high.setTypeface(font2);
        et_search=findViewById(R.id.editText);
        et_search.clearFocus();
        et_search.setQueryHint("What are you looking for?");
        int searchIconId = et_search.getContext().getResources().getIdentifier("android:id/search_button",null, null);
        ImageView searchIcon = (ImageView) et_search.findViewById(searchIconId);
        searchIcon.setImageResource(R.mipmap.searchicon);


        if(!getIntent().getExtras().getString("CatName").isEmpty())
        {
            tv_ProductCategory.setText(getIntent().getExtras().getString("CatName"));
        }


        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent in=new Intent(ProductListing.this,ProductListing.class);
                in.putExtra("Keyword",query);
                in.putExtra("Identifier","0");
                startActivity(in);
                if (identifier.equals("0")){
                    finish();
                }
                return false;
            }

        });

        ProductParams.put("user_id", user_id);
        Log.d("asfda",user_id);
        Log.d("asfda",wishlist_add_url);
        Log.d("asfda",wishlist_delete_url);
        Log.d("asfda",product_url);

        WishlistAddParams.put("user_id", user_id);
        WishlistAddParams.put("appkey", "TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc");
        WishlistAddParams.put("appsecurity", "TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==");
        WishlistDeleteParams.put("user_id", user_id);
        WishlistDeleteParams.put("appkey", "TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc");
        WishlistDeleteParams.put("appsecurity", "TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==");
        SortParams.put("appkey", "TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc");
        SortParams.put("appsecurity", "TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==");
        SortParams.put("category_id",cat_id);
        SortParams.put("user_id",user_id);
        FilterParams.put("appkey", "TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc");
        FilterParams.put("appsecurity", "TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==");
        FilterParams.put("category_id",cat_id);
        FilterParams.put("attribute_id",attribute_id);
        FilterParams.put("attributevalue_id",attributevalue_id);
        System.out.println("dfjkajfkdsakf"+FilterParams.toString());
//        MyAdapter productAdapter=new MyAdapter(serviceDemo);
//        rv_product_listing.setAdapter(productAdapter);

        if (identifier.equals("0")){
            subToolbar.setVisibility(View.GONE);
            subToolbarDivider.setVisibility(View.GONE);
            SearchParams.put("keywords", keyword);
            Log.d("sdakfjskla",keyword);
            SearchParams.put("appkey", "TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc");
            SearchParams.put("appsecurity", "TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==");
            isNetworkConnected();
            if(NETCONNECTION==1)
            {
                Search();
            }
            else
            {
                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No internet")
                        .setContentText("Internet not available, Check your internet connectivity and try again")
                        .show();
            }
        }else if(identifier.equals("10")){
            subToolbar.setVisibility(View.GONE);
            subToolbarDivider.setVisibility(View.GONE);
            isNetworkConnected();
            if(NETCONNECTION==1)
            {
                Filter();
            }
            else
            {
                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No internet")
                        .setContentText("Internet not available, Check your internet connectivity and try again")
                        .show();
            }
        }else{
            subToolbar.setVisibility(View.VISIBLE);
            subToolbarDivider.setVisibility(View.VISIBLE);
            ProductParams.put("category_id", cat_id);
            ProductParams.put("limit", "50");
            ProductParams.put("appkey", MainActivity.appkey);
            ProductParams.put("appsecurity", MainActivity.appsecurity);
            isNetworkConnected();
            if(NETCONNECTION==1)
            {
                Product();
            }
            else
            {
                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No internet")
                        .setContentText("Internet not available, Check your internet connectivity and try again")
                        .show();
            }
        }

        t_low_to_high.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                img_low_to_high.setVisibility(View.VISIBLE);
                img_high_to_low.setVisibility(View.INVISIBLE);
                img_revelance.setVisibility(View.INVISIBLE);
                SortParams.put("sortamount","2");
                System.out.println("sorkdjfkasjdfk"+SortParams.toString());
                sortlayout.setVisibility(View.GONE);
                SORTLAYOUT_STATE=1;
                Sort();

                return false;
            }
        });
        t_high_to_low.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                img_low_to_high.setVisibility(View.INVISIBLE);
                img_high_to_low.setVisibility(View.VISIBLE);
                img_revelance.setVisibility(View.INVISIBLE);
                SortParams.put("sortamount","1");
                System.out.println("sorkdjfkasjdfk"+SortParams.toString());
                sortlayout.setVisibility(View.GONE);
                SORTLAYOUT_STATE=1;
                Sort();

                return false;
            }
        });
        t_relevance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                img_low_to_high.setVisibility(View.INVISIBLE);
                img_high_to_low.setVisibility(View.INVISIBLE);
                img_revelance.setVisibility(View.VISIBLE);
                sortlayout.setVisibility(View.GONE);
                SORTLAYOUT_STATE=1;
                adpaterRefresh="1";
                Product();

                return false;
            }
        });

        sortlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sortlayout.setVisibility(View.INVISIBLE);


    }
    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences s = getSharedPreferences(ProductListing.mp, 0);
        login_status = s.getString("Login_Status", "");
        loginCheck();

        if (!mIsReceiverRegistered) {
            if (mReceiver == null)
                mReceiver = new MyBroadcastReceiver();
            registerReceiver(mReceiver, new IntentFilter("YourIntentAction"));
            mIsReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsReceiverRegistered) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
            mIsReceiverRegistered = false;
        }

        // Other onPause() code here

    }

    public void search(View v) {


        if (SearchlayoutVisibleState == 0) {


            rtv_searchlayout.setVisibility(View.VISIBLE);
            SearchlayoutVisibleState = 1;

        } else {
            rtv_searchlayout.setVisibility(View.GONE);
            SearchlayoutVisibleState = 0;


        }
    }
    public void cart(View v)
    {
        Intent in=new Intent(this,Cart.class);
        in.putExtra("Identifier","7");
        startActivity(in);
    }
    //Profile onclick
    public void account(View v)
    {
        if( user_id.equals("") ) {
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

    /*public void sort(View view){


    }*/
    public void filter(View view){
        Intent in=new Intent(ProductListing.this,Filter.class);
        in.putExtra("CatID",cat_id);
        startActivity(in);
        overridePendingTransition(R.anim.slide_in, R.anim.hold);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<ProductItems> values;
        Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        Typeface custom_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        //        Typeface custom_semi_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item,
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView tvProduct,tvProdActualPric,tvProdOfferPric;
            ImageButton ibLikeButton;
            ImageView ivMainImage,ivBadge;

            //            public TextView txtFooter;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                tvProduct = v.findViewById(R.id.tv_product_title);
                tvProdActualPric=v.findViewById(R.id.tv_prod_actual_pric);
                tvProdOfferPric=v.findViewById(R.id.tv_prod_offer_pric);
                ibLikeButton=v.findViewById(R.id.ib_like);
                ivMainImage=v.findViewById(R.id.iv_main_img);
                ivBadge=v.findViewById(R.id.iv_badge);
//                txtFooter = (TextView) v.findViewById(R.id.secondLine);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in =new Intent(ProductListing.this,ProductDetails.class);
                        in.putExtra("ProdID",values.get(getAdapterPosition()).getProdId());
                        startActivity(in);
                    }
                });
            }
        }

        public void add(int position, ProductItems item) {
            values.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            values.remove(position);
            notifyItemRemoved(position);
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<ProductItems> myDataset) {
            rv_product_listing.invalidate();
            rv_product_listing.removeAllViewsInLayout();
            RecyclerView.ItemAnimator animator = rv_product_listing.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
                rv_product_listing.getItemAnimator().setChangeDuration(0);
            }
            values = myDataset;
            notifyDataSetChanged();
            rv_product_listing.scheduleLayoutAnimation();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.product_list_recycler_item, parent, false);
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
            String mainImgUrl = values.get(position).getMainImg();
            String prodActPric = values.get(position).getActualPric();
            String prodOfferPric = values.get(position).getOfferPric();
            String badgeImgUrl = ip_head+values.get(position).getBadge();
            final String prodId = values.get(position).getProdId();
            final String wishStatus = values.get(position).getWishStatus();

            holder.tvProduct.setText(prodTitle);
            holder.tvProduct.setTypeface(custom_regular);
            holder.tvProdActualPric.setText("Rs. "+getDecimal(prodActPric));
            holder.tvProdActualPric.setTypeface(custom_bold);
            holder.tvProdActualPric.setPaintFlags(holder.tvProdActualPric.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvProdOfferPric.setText("Rs. "+getDecimal(prodOfferPric));
            holder.tvProdOfferPric.setTypeface(custom_bold);
            if(wishStatus.equals("1")){
                holder.ibLikeButton.setImageResource(R.mipmap.heartfill);
            }else {
                holder.ibLikeButton.setImageResource(R.mipmap.heart);
            }


            Glide.with(ProductListing.this)
                    .load(mainImgUrl)
                    .into(holder.ivMainImage);
            Glide.with(ProductListing.this)
                    .load(badgeImgUrl)
                    .into(holder.ivBadge);
            holder.ibLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isNetworkConnected();
                    if(NETCONNECTION==1)
                    {
                        if (login_status.equals("success")) {
                            if (wishStatus.equals("1")) {
                                WishlistDeleteParams.put("product_id", prodId);
                                holder.ibLikeButton.setImageResource(R.mipmap.heart);
                                ProductArray.get(position).setWishStatus("0");
                                WishlistDelete(position);
                            } else {
                                WishlistAddParams.put("product_id", prodId);
                                holder.ibLikeButton.setImageResource(R.mipmap.heartfill);
                                ProductArray.get(position).setWishStatus("1");
                                //test
                                WishlistAdd(position);
                            }
                        }else{
                            Intent in=new Intent(ProductListing.this,LoginActivity.class);
                            startActivity(in);
                        }
                    }else {
                        new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("No internet")
                                .setContentText("Internet not available, Check your internet connectivity and try again")
                                .show();
                    }
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


    public void Product(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                product_url, new JSONObject(ProductParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Product Responce--->"+response.toString());

                        try {
                            if (adpaterRefresh.equals("1")){
                                ProductArray.clear();
                            }
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = jsonResponse.getJSONArray("products");
                            if(resultsArray.length()<1){

                                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No products")
                                        .setContentText("No products available in this category.")
                                        .show();
                            }else {

                            }
                            if(resultsArray.length()<=1){
                                prodCount= String.valueOf(resultsArray.length())+" Product";
                            }else {
                                prodCount = String.valueOf(resultsArray.length()) + " Products";
                            }

                            for (int i=0; i<resultsArray.length(); i++){
                                String prodId=resultsArray.getJSONObject(i).getString("id");
                                String mainImg=resultsArray.getJSONObject(i).getString("image");
                                String prodTitle=resultsArray.getJSONObject(i).getString("title");
                                String prodActPric=resultsArray.getJSONObject(i).getString("actual_price");
                                String prodOffrPric=resultsArray.getJSONObject(i).getString("selling_price");
                                String wishStatus=resultsArray.getJSONObject(i).getString("wishlist");
                              //  String badge=resultsArray.getJSONObject(i).getString("badgename");
                                ProductItems obj=new ProductItems(
                                        prodId,
                                        "http://192.168.1.7:8080/dcbooks/"+mainImg,
                                        prodTitle,
                                        prodActPric,
                                        prodOffrPric,
                                        wishStatus,
                                        ""
                                );
                                ProductArray.add(obj);
                            }
                            adpaterRefresh="0";
                            productAdapter=new MyAdapter(ProductArray);
                            rv_product_listing.setAdapter(productAdapter);



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

                System.out.println("Login Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void Search(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                search_url, new JSONObject(SearchParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Search Responce---> "+response.toString());

                        try {
                            ProductArray.clear();
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = jsonResponse.getJSONArray("Homekeysearch");

                            if(resultsArray.length()<1){


                                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No products")
                                        .setContentText("No product found matching the given keyword.")
                                        .show();
                            }else {


                            }
//                            String prodCount= String.valueOf(resultsArray.length())+" Products";
//                            tv_ProductCategory.setText(prodCount);
                            //Test
                            for (int i=0; i<resultsArray.length(); i++){
                                String prodId=resultsArray.getJSONObject(i).getString("id");
                                String mainImg=resultsArray.getJSONObject(i).getString("image");
                                String prodTitle=resultsArray.getJSONObject(i).getString("title");
                                String prodActPric=resultsArray.getJSONObject(i).getString("actualprice");
                                String prodOffrPric=resultsArray.getJSONObject(i).getString("sellingprice");
                                String wishStatus=resultsArray.getJSONObject(i).getString("wishliststatus");
                                String badge=resultsArray.getJSONObject(i).getString("badgename");
                                ProductItems obj=new ProductItems(
                                        prodId,
                                        mainImg,
                                        prodTitle,
                                        prodActPric,
                                        prodOffrPric,
                                        wishStatus,
                                        badge
                                );
                                ProductArray.add(obj);
                            }
                            Log.d("dsjfkaads",ProductArray.toString());
                            productAdapter=new MyAdapter(ProductArray);
                            rv_product_listing.setAdapter(productAdapter);

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

    public void Sort(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                sort_url, new JSONObject(SortParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Sort Responce---> "+response.toString());

                        try {
                            ProductArray.clear();
//                            rv_product_listing.getRecycledViewPool().clear();
//                            productAdapter.notifyDataSetChanged();
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = jsonResponse.getJSONArray("productsortlist");
                            String prodCount= String.valueOf(resultsArray.length())+" Products";
                           // tv_ProductCategory.setText(prodCount);
                            for (int i=0; i<resultsArray.length(); i++){
                                String prodId=resultsArray.getJSONObject(i).getString("id");
                                String mainImg=resultsArray.getJSONObject(i).getString("image");
                                String prodTitle=resultsArray.getJSONObject(i).getString("title");
                                String prodActPric=resultsArray.getJSONObject(i).getString("actualprice");
                                String prodOffrPric=resultsArray.getJSONObject(i).getString("sellingprice");
                                String wishStatus=resultsArray.getJSONObject(i).getString("wishliststatus");
                                String badge=resultsArray.getJSONObject(i).getString("badgename");
                                ProductItems obj=new ProductItems(
                                        prodId,
                                        mainImg,
                                        prodTitle,
                                        prodActPric,
                                        prodOffrPric,
                                        wishStatus,
                                        badge
                                );
                                ProductArray.add(obj);
                            }
                            productAdapter=new MyAdapter(ProductArray);
                            rv_product_listing.swapAdapter(productAdapter, false);
//                            rv_product_listing.setAdapter(productAdapter);
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

                System.out.println("Sort Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void Filter(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
        System.out.println("fkasjfdkseede"+FilterParams.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                filter_url, new JSONObject(FilterParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Filter Responce---> "+response.toString());

                        try {
                            ProductArray.clear();
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = jsonResponse.getJSONArray("filterresultlist");
                            if(resultsArray.length()<1){

                                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No products")
                                        .setContentText("No product found matching the given filter criteria.")
                                        .show();
                            }else {
                               
                            }
//                            String prodCount= String.valueOf(resultsArray.length())+" Products";
//                            tv_ProductCategory.setText(prodCount);
                            //Test
                            for (int i=0; i<resultsArray.length(); i++){
                                String prodId=resultsArray.getJSONObject(i).getString("id");
                                String mainImg=resultsArray.getJSONObject(i).getString("image");
                                String prodTitle=resultsArray.getJSONObject(i).getString("title");
                                String prodActPric=resultsArray.getJSONObject(i).getString("actualprice");
                                String prodOffrPric=resultsArray.getJSONObject(i).getString("sellingprice");
                                String wishStatus=resultsArray.getJSONObject(i).getString("wishliststatus");
                                String badge=resultsArray.getJSONObject(i).getString("badgename");
                                ProductItems obj=new ProductItems(
                                        prodId,
                                        mainImg,
                                        prodTitle,
                                        prodActPric,
                                        prodOffrPric,
                                        wishStatus,
                                        badge
                                );
                                ProductArray.add(obj);
                            }
                            Log.d("dsjfkaads",ProductArray.toString());
                            productAdapter=new MyAdapter(ProductArray);
                            rv_product_listing.swapAdapter(productAdapter,false);

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

                System.out.println("Filter Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void WishlistAdd(final int key){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                wishlist_add_url, new JSONObject(WishlistAddParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Wishlist Add Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("result");

                            if(Status.equals("true"))
                            {
                                productAdapter.notifyItemChanged(key);
                                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Added")
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
                                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
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

                System.out.println("Wishlist Add Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void WishlistDelete(final int key){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                wishlist_delete_url, new JSONObject(WishlistDeleteParams),
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
                                productAdapter.notifyItemChanged(key);
                                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
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
                                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
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
                if (identifier.equals("10")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    ProductListing.this.finish();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public class ProductItems{
        String prodId,mainImg,prodTitle,actualPric,offerPric,wishStatus,badge;

        public ProductItems(String prodId,String mainImg, String prodTitle, String actualPric, String offerPric,String wishStatus,String badge) {
            this.prodId = prodId;
            this.mainImg = mainImg;
            this.prodTitle = prodTitle;
            this.actualPric = actualPric;
            this.offerPric = offerPric;
            this.wishStatus = wishStatus;
            this.badge = badge;
        }

        public void setWishStatus(String wishStatus) {
            this.wishStatus = wishStatus;
        }

        public String getMainImg() {
            return mainImg;
        }

        public String getProdTitle() {
            return prodTitle;
        }

        public String getActualPric() {
            return actualPric;
        }

        public String getOfferPric() {
            return offerPric;
        }

        public String getWishStatus() {
            return wishStatus;
        }

        public String getProdId() {
            return prodId;
        }

        public String getBadge() {
            return badge;
        }
    }
    public String getDecimal(String value){
        String pass;
        DecimalFormat df = new DecimalFormat("#0.00");
        pass = String.valueOf(df.format(Double.parseDouble(value)));
        return pass;
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

    private void updateUI(Intent intent) {
        // Do what you need to do
//        loginCheck();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    }
}
