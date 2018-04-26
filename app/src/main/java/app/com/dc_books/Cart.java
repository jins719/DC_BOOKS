package app.com.dc_books;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class Cart extends AppCompatActivity {

    ImageButton ib_checkout;
    TextView tv_subtot_labl,tv_shipping_labl,tv_payamt_labl,tv_subtotal,tv_shipping,tv_paymentamt,tv_promo_txt,tv_toolbar_text;
    EditText et_promocode;
    RecyclerView rv_cartlist;
    Button bt_applypromo;
    int NETCONNECTION;
    Map<String, String> CartParams = new HashMap<>();
    Map<String, String> CartDeleteParams = new HashMap<>();
    Map<String, String> PromoCodeParams = new HashMap<>();
    Map<String, String> WishlistAddParams = new HashMap<>();
    Map<String, String> WishlistDeleteParams = new HashMap<>();
    Map<String, String> QtyEditParams = new HashMap<>();
    Map<String, String> GetQtyDtlsParams = new HashMap<>();

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";
    String ip_head = "http://www.level10boutique.com/";
    String cart_url ="";
//    String cart_url = ip_head+"admin/services/Appsavecartbag";
    String cart_delete_url ="http://athira-pc:8080/dcbooks/api/productshopping/remove_from_cart";
    String promocode_url ="http://athira-pc:8080/dcbooks/api/productshopping/promo_code";
    String wishlist_add_url = ip_head+"admin/services/Appaddwishlist";
    String wishlist_delete_url = ip_head+"admin/services/Appdeletewishlist";
    String get_qty_dtls_url ="http://athira-pc:8080/dcbooks/api/productshopping/save_bag_edit_list";
    String quantity_edit = "http://athira-pc:8080/dcbooks/api/productshopping/edit_cart";
    Toolbar toolbar;
    ArrayList<CartItems> CartArray=new ArrayList<>();
    String CategoryIds,ProductIds;
    LinearLayoutManager gridManager;
    MyAdapter CartAdapter;
    RelativeLayout rl_promo,rl_subtot;
    String login_status,totalproweight,subtotal,discount,voucher,discounttype,user_id,special_discount,offer_price,vouchercode,totalpayamt,passtotal;
    Integer getqty,stockqty;
    String refreshId = "0",currentStock;
    JSONArray resultsArray;
    String android_id;
    String prod_id="none";
    String Identifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        String stock_qty="none";
        special_discount="none";
        offer_price="none";

        if (extras != null) {
            prod_id = extras.getString("ProductID");
            stock_qty= extras.getString("Qty");
            offer_price = extras.getString("OfferPrice");
            Identifier = extras.getString("Identifier","none");
            special_discount = extras.getString("Specialdiscount");
        }

        SharedPreferences s = getSharedPreferences(ProductListing.mp, 0);
        user_id = s.getString("User_id", "");
        login_status = s.getString("Login_Status", "");
        Log.d("dskfjak",user_id);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Typeface custom_bold_main = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        Typeface custom_normal_main = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        ib_checkout=findViewById(R.id.ib_checkout);
        tv_subtotal=findViewById(R.id.tv_subtotal_value);
        tv_shipping_labl=findViewById(R.id.tv_shipping_charg);
        tv_shipping=findViewById(R.id.tv_shipping_status);
        tv_payamt_labl=findViewById(R.id.tv_pay_amt);
        tv_paymentamt=findViewById(R.id.tv_payamt_value);
        et_promocode=findViewById(R.id.et_promo_code);
        rv_cartlist=findViewById(R.id.rv_cart_listing);
        tv_promo_txt=findViewById(R.id.tv_promo_code);
        bt_applypromo=findViewById(R.id.bt_promo);
        tv_subtot_labl=findViewById(R.id.tv_sub_total);
        tv_toolbar_text=findViewById(R.id.tv_toolbarTxt);
        tv_paymentamt.setTypeface(custom_bold_main);
        tv_toolbar_text.setTypeface(custom_bold_main);
        tv_shipping_labl.setTypeface(custom_normal_main);
        tv_shipping.setTypeface(custom_bold_main);
        tv_payamt_labl.setTypeface(custom_bold_main);
        tv_subtot_labl.setTypeface(custom_normal_main);
        et_promocode.setTypeface(custom_normal_main);
        tv_promo_txt.setTypeface(custom_bold_main);
        bt_applypromo.setTypeface(custom_bold_main);
        tv_subtotal.setTypeface(custom_normal_main);
        rl_promo=findViewById(R.id.rl_promcard);
        rl_subtot=findViewById(R.id.cv_subtotal);
        rv_cartlist.setItemAnimator(null);



        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            if( Identifier.equals("7") ){
                cart_url = "http://athira-pc:8080/dcbooks/api/productshopping/show_cart";
                refreshId="1";
                CartParams.put("appkey", MainActivity.appkey);
                CartParams.put("appsecurity", MainActivity.appsecurity);
                CartParams.put("uniquedevice", android_id);
                Cart();
            }else {
                cart_url = "http://athira-pc:8080/dcbooks/api/productshopping/add_to_cart";
                CartParams.put("appkey", MainActivity.appkey);
                CartParams.put("appsecurity", MainActivity.appsecurity);
                CartParams.put("user_id", user_id);
                CartParams.put("product_id", prod_id);
                CartParams.put("productquantity", "1");
                CartParams.put("uniquedevice", android_id);
                CartParams.put("devicetype", "0");
                CartParams.put("discountproductprice", offer_price);
                CartParams.put("specialdiscount", special_discount);
                Cart();
            }
        }
        else
        {
            new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Check your internet connectivity and try again")
                    .show();
        }
        ViewCompat.setNestedScrollingEnabled(rv_cartlist, false);


        bt_applypromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_promocode.getText().toString().equals("")) {
                    et_promocode.setError("Enter a valid promo code.");
                } else {
                    isNetworkConnected();
                    if (NETCONNECTION == 1) {
                        CartParams.put("appkey", MainActivity.appkey);
                        CartParams.put("appsecurity", MainActivity.appsecurity);
                        PromoCodeParams.put("vouchercode", et_promocode.getText().toString());
                        PromoCodeParams.put("category_id", CategoryIds);
                        PromoCodeParams.put("product_id", ProductIds);
                        PromoCode();
                    } else {
                        new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("No internet")
                                .setContentText("Internet not available, Check your internet connectivity and try again")
                                .show();
                    }
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences s = getSharedPreferences(ProductListing.mp, 0);
        user_id = s.getString("User_id", "");
        login_status = s.getString("Login_Status", "");
    }

    public void check_out(View v)
    {
        if (login_status.equals("success")){
            isNetworkConnected();
            if (NETCONNECTION == 1) {
                if (totalpayamt==null){
                    passtotal= subtotal;
                }else {
                    passtotal= totalpayamt;
                }
                if (vouchercode==null){
                    vouchercode="null";
                }
                if (discount==null){

                    discount="null";

                }
                Intent in = new Intent(Cart.this,OrderSummary.class);
                in.putExtra("totalpayamount",passtotal);
                in.putExtra("vouchername",vouchercode);
                in.putExtra("voucheramount",discount);
                in.putExtra("subtotal",subtotal);
                in.putExtra("Weight",totalproweight);
                startActivity(in);
            }else {
                new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No internet")
                        .setContentText("Internet not available, Check your internet connectivity and try again")
                        .show();
            }
        }else {

            if (totalpayamt==null){
                passtotal= subtotal;
            }else {
                passtotal= totalpayamt;
            }
            if (vouchercode==null){
                vouchercode="null";
            }
            if (discount==null){

                discount="null";

            }

            Intent in=new Intent(Cart.this,LoginActivity.class);
            in.putExtra("Identifier","6");
            in.putExtra("totalpayamount",passtotal);
            in.putExtra("vouchername",vouchercode);
            in.putExtra("voucheramount",discount);
            in.putExtra("subtotal",subtotal);
            in.putExtra("Weight",totalproweight);
            startActivity(in);

        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
        ib_checkout.getBackground().setAlpha(255);
        ib_checkout.setEnabled(true);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<CartItems> values;
        Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        Typeface custom_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        //        Typeface custom_semi_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item,
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView tvProductTitle,tvProdSize,tvProdQty,tvProdSizeLbl,tvQtyLbl,tvPriceLbl,tvPricevalue;
            ImageView ivMainImage,ibEditButton,ibCloseButton,ivBadgeImage;

            //            public TextView txtFooter;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                tvProductTitle = v.findViewById(R.id.tv_main_title);

                tvProdSizeLbl=v.findViewById(R.id.tv_size_label);
                tvQtyLbl=v.findViewById(R.id.tv_qty_label);
                tvPriceLbl=v.findViewById(R.id.tv_price_label);

                tvProdQty=v.findViewById(R.id.tv_qty_placeholder);
                tvProdSize=v.findViewById(R.id.tv_size_placeholder);
                tvPricevalue=v.findViewById(R.id.tv_price_placeholder);

//                ibLikeButton=v.findViewById(R.id.ib_like_cart);
                ibEditButton=v.findViewById(R.id.ib_edit_cart);
                ibCloseButton=v.findViewById(R.id.ib_close_cart);

                ivMainImage=v.findViewById(R.id.iv_prodImg);
                ivBadgeImage=v.findViewById(R.id.iv_badgeimg);
                tvProductTitle.setTypeface(custom_regular);
                tvProdSizeLbl.setTypeface(custom_bold);
                tvProdQty.setTypeface(custom_bold);
                tvProdSize.setTypeface(custom_bold);
                tvQtyLbl.setTypeface(custom_bold);
                tvPriceLbl.setTypeface(custom_bold);
                tvPricevalue.setTypeface(custom_bold);
                if(getItemCount()<1){
                    ib_checkout.getBackground().setAlpha(40);
                    rl_promo.setVisibility(View.GONE);
                }else{
                    ib_checkout.getBackground().setAlpha(255);
                    rl_promo.setVisibility(View.VISIBLE);
                }

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }

        public void add(int position, CartItems item) {
            values.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            values.remove(position);
            notifyItemRemoved(position);
            CartAdapter.notifyDataSetChanged();
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<CartItems> myDataset) {
            rv_cartlist.invalidate();
            rv_cartlist .removeAllViewsInLayout();
            values = myDataset;
            notifyDataSetChanged();
            rv_cartlist.scheduleLayoutAnimation();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.cart_recycler_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            Log.d("safda","dfsaf");
            Log.d("adfasfdaf",String.valueOf(position));
            String prodTitle = values.get(position).getProdTitle();
            Log.d("adfasfdaf",prodTitle);
            String mainImgUrl = values.get(position).getMainImg();
            String badgeImgUrl=ip_head+values.get(position).getBadgeImg();
            String prodPrice = values.get(position).getOfferPric();
            String prodSize = values.get(position).getProdSize();
            final String prodQty = values.get(position).getProdQty();
            final String cartId = values.get(position).getCartId();
            final String prodId = values.get(position).getProdId();
            Log.d("prodidscollect",prodId);
            final String wishStatus = values.get(position).getWishStatus();
            final String stockQty= values.get(position).getProdStock();

            holder.tvProductTitle.setText(prodTitle);
            if(prodPrice.contains(".")) {
                holder.tvPricevalue.setText(getDecimal(prodPrice));
            }else {
                holder.tvPricevalue.setText(prodPrice);
            }
            holder.tvProdQty.setText(prodQty);
            holder.tvProdSize.setText(prodSize);

            Glide.with(Cart.this)
                    .load(mainImgUrl)
                    .into(holder.ivMainImage);
            Glide.with(Cart.this)
                    .load(badgeImgUrl)
                    .into(holder.ivBadgeImage);

            holder.ibCloseButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        SharedPreferences s = getSharedPreferences(Cart.mp, 0);
                        String login_status = s.getString("Login_Status", "");
                        isNetworkConnected();
                        if(NETCONNECTION==1) {
                            CartDeleteParams.put("appkey", MainActivity.appkey);
                            CartDeleteParams.put("appsecurity", MainActivity.appsecurity);
                            CartDeleteParams.put("id", values.get(position).getCartId());
//                                remove(position);
//                                if(getItemCount()<1){
//                                }
                                tv_toolbar_text.setText("MY BAG ("+getItemCount()+" Items)");
                                CartItemDelete();
                        }else {
                            new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("No internet")
                                    .setContentText("Internet not available, Check your internet connectivity and try again")
                                    .show();
                        }
                        return true;
                    }
                    return false;
                }
            });



            holder.ibEditButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        SharedPreferences s = getSharedPreferences(Cart.mp, 0);
                        String login_status = s.getString("Login_Status", "");
                        qtyEdit(prodQty,stockQty,cartId,prodId);


                        return true;
                    }
                    return false;
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

    public void Cart(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                cart_url, new JSONObject(CartParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Cart Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            if(refreshId.equals("0")){
                                resultsArray = jsonResponse.getJSONArray("appsavelist");
                            }else {
                                resultsArray = jsonResponse.getJSONArray("appbaglist");
                            }
                            String prodCount= String.valueOf(resultsArray.length())+" Products";
//                            tvProductCount.setText(prodCount);
                            tv_toolbar_text.setText("MY BAG ("+resultsArray.length()+" Items)");
                            if (resultsArray.length()<1){
                                rl_subtot.setVisibility(View.GONE);
                                ib_checkout.getBackground().setAlpha(40);
                                ib_checkout.setEnabled(false);
                                rl_promo.setVisibility(View.GONE);
                            }else {
                                rl_subtot.setVisibility(View.VISIBLE);
                            }
                            CartArray.clear();
                            for (int i=0; i<resultsArray.length(); i++){

                                String prodId=resultsArray.getJSONObject(i).getString("productid");;
                                String mainImg=resultsArray.getJSONObject(i).getString("productimage");
                                String prodTitle=resultsArray.getJSONObject(i).getString("productname");
                                String cartId=resultsArray.getJSONObject(i).getString("id");
                                String prodQty=resultsArray.getJSONObject(i).getString("selectedquantity");
                                String offerPric=resultsArray.getJSONObject(i).getString("sellingprice");
                                String wishStatus=resultsArray.getJSONObject(i).getString("wishliststatus");
                                String prodStock=resultsArray.getJSONObject(i).getString("productquantity");
                                //String prodSize=resultsArray.getJSONObject(i).getString("productsize");
                               // String badgeImg=resultsArray.getJSONObject(i).getString("badgename");
                                if (resultsArray.getJSONObject(i).has("subtotal")) {
                                    subtotal=resultsArray.getJSONObject(i).getString("subtotal");
                                }
                                if (resultsArray.getJSONObject(i).has("totalprodductweight")) {
                                    totalproweight=resultsArray.getJSONObject(i).getString("totalprodductweight");
                                }
                                Log.d("wishstatuser",wishStatus);
                                String categoryId=resultsArray.getJSONObject(i).getString("productcategory");
                                CartItems obj=new CartItems(
                                        prodId,
                                        "http://athira-pc:8080/dcbooks/"+mainImg,
                                        prodTitle,
                                        offerPric,
                                        cartId,
                                        wishStatus,
                                        prodQty,
                                        "",
                                        prodStock,
                                        "",
                                        ""
                                );
                                if (i==0){
                                    CategoryIds = categoryId;
                                }else {
                                    CategoryIds = CategoryIds+","+categoryId;
                                }
                                if(i==0){
                                    ProductIds = prodId;
                                }else {
                                    ProductIds = ProductIds +","+prodId;
                                }
                                CartArray.add(obj);
                            }
                            tv_subtotal.setText("RS. "+getDecimal(subtotal));
                            tv_paymentamt.setText("RS. "+getDecimal(subtotal));
                            gridManager = new LinearLayoutManager(Cart.this);
                            rv_cartlist.setLayoutManager(gridManager);
                            CartAdapter= new MyAdapter(CartArray);
                            rv_cartlist.swapAdapter(CartAdapter,false);
                            refreshId="0";


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

                System.out.println("Cart Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void CartItemDelete(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        System.out.println("json values"+ new JSONObject(CartDeleteParams));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                cart_delete_url, new JSONObject(CartDeleteParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Cart Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("result");

                            if(Status.equals("true"))
                            {
                                new SweetAlertDialog(Cart.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Removed")
                                        .setContentText(Message)
                                        .show();
//
                                cart_url ="http://athira-pc:8080/dcbooks/api/productshopping/show_cart";
                                CartParams.clear();
                                CartParams.put("appkey", MainActivity.appkey);
                                CartParams.put("appsecurity", MainActivity.appsecurity);
                                CartParams.put("uniquedevice",android_id);
                                refreshId="1";
                                Cart();
                            }
                            else
                            {
                                new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(Message)
                                        .show();
                            }
                            CartAdapter.notifyDataSetChanged();

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

                System.out.println("Cart Delete Responce---> "+error.toString());
            }
            //test
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void PromoCode(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
        //test
        System.out.println("dsjfkasjkdfjaks"+PromoCodeParams.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                promocode_url, new JSONObject(PromoCodeParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Promo Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("message");
                            if (jsonResponse.has("amount")) {
                                discount=jsonResponse.getString("amount");
                            }
                            if (jsonResponse.has("vouchercode")) {
                                voucher=jsonResponse.getString("vouchercode");
                            }
                            if (jsonResponse.has("discounttype")) {
                                discounttype=jsonResponse.getString("discounttype");
                            }
                            if(Status.equals("true"))
                            {
                                new SweetAlertDialog(Cart.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText(Message)
                                        .show();
                                if (discounttype.equals("1")){
                                    totalpayamt = String.valueOf(Double.valueOf(subtotal) - calculatePercentage(subtotal,discount));
                                }else {
                                    totalpayamt = String.valueOf(Double.valueOf(subtotal)- Double.valueOf(discount));
                                }
                                tv_paymentamt.setText("Rs. "+getDecimal(totalpayamt));
//                                Cart();
                                vouchercode=et_promocode.getText().toString();
                                CartAdapter.notifyDataSetChanged();
                                et_promocode.setText("");
                            }
                            else
                            {
                                new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(Message)
                                        .show();
                            }
                            CartAdapter.notifyDataSetChanged();

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

                System.out.println("Promo Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void WishlistAdd(){

//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(true);
//        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                wishlist_add_url, new JSONObject(WishlistAddParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();

                        System.out.println("Wishlist Add Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("result");

                            if(Status.equals("true"))
                            {
                                CartAdapter.notifyDataSetChanged();
//                                new SweetAlertDialog(Cart.this, SweetAlertDialog.SUCCESS_TYPE)
//                                        .setTitleText(Message)
//                                        .setContentText(Message)
//                                        .show();
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
                                new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(Message)
                                        .show();
                            }

                        } catch (JSONException e) {
                            Log.d("JSONExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();

                        }
                        catch (NullPointerException e)
                        {
                            Log.d("NullExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();

                System.out.println("Wishlist Add Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void WishlistDelete(){

//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(true);
//        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                wishlist_delete_url, new JSONObject(WishlistDeleteParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();

                        System.out.println("Wishlist Delete Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("result");

                            if(Status.equals("true"))
                            {
                                CartAdapter.notifyDataSetChanged();
//                                new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText(Status)
//                                        .setContentText(Message)
//                                        .show();
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
                                new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(Message)
                                        .show();
                            }

                        } catch (JSONException e) {
                            Log.d("JSONExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();

                        }
                        catch (NullPointerException e)
                        {
                            Log.d("NullExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();

                System.out.println("Wishlist Delete Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void QuantityEdit(){

//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(true);
//        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                quantity_edit, new JSONObject(QtyEditParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();

                        System.out.println("Quantity Edit Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("message");

                            if(Status.equals("true"))
                            {
                                CartAdapter.notifyDataSetChanged();
                                cart_url ="http://athira-pc:8080/dcbooks/api/productshopping/show_cart";
                                CartParams.clear();
                                CartParams.put("appkey", MainActivity.appkey);
                                CartParams.put("appsecurity", MainActivity.appsecurity);
                                CartParams.put("uniquedevice",android_id);
                                System.out.println("dsajfkjaksdjdfksaj"+CartParams.toString());
                                refreshId="1";
                                Cart();
                                new SweetAlertDialog(Cart.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText(Message)
                                        .show();
//

                            }
                            else
                            {
                                new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(Message)
                                        .show();
                            }

                        } catch (JSONException e) {
                            Log.d("JSONExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();

                        }
                        catch (NullPointerException e)
                        {
                            Log.d("NullExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();

                System.out.println("Wishlist Delete Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void getQtyDetails(){

//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(true);
//        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                get_qty_dtls_url, new JSONObject(GetQtyDtlsParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();

                        System.out.println("Quantity Edit Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            resultsArray = jsonResponse.getJSONArray("editbagviewlist");

                            currentStock = resultsArray.getJSONObject(0).getString("quantity");
                            System.out.println("akdfksa"+currentStock);

                        } catch (JSONException e) {
                            Log.d("JSONExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();

                        }
                        catch (NullPointerException e)
                        {
                            Log.d("NullExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();

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
                Cart.this.finish();
                ib_checkout.getBackground().setAlpha(255);
                ib_checkout.setEnabled(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CartItems {
        String prodId, mainImg, prodTitle, offerPric, cartId, wishStatus,prodQty,categoryId,prodStock,prodSize,badgeImg;

        public CartItems(String prodId, String mainImg, String prodTitle, String offerPric,String cartId,String wishStatus,String prodQty,String categoryId,String prodStock,String prodSize,String badgeImg) {
            this.prodId = prodId;
            this.mainImg = mainImg;
            this.prodTitle = prodTitle;
            this.offerPric = offerPric;
            this.cartId = cartId;
            this.wishStatus= wishStatus;
            this.prodQty=prodQty;
            this.categoryId=categoryId;
            this.prodStock=prodStock;
            this.prodSize=prodSize;
            this.badgeImg=badgeImg;
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

        public String getOfferPric() {
            return offerPric;
        }

        public String getCartId() {
            return cartId;
        }

        public String getWishStatus() {
            return wishStatus;
        }

        public String getProdQty() {
            return prodQty;
        }

        public void setWishStatus(String wishStatus) {
            this.wishStatus = wishStatus;
        }

        public String getProdStock() {
            return prodStock;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public String getProdSize() {
            return prodSize;
        }

        public String getBadgeImg() {
            return badgeImg;
        }
    }

    public void qtyEdit(String qty,String availqty,final String qtyid,final String prodId){

        getqty=Integer.valueOf(qty);

        isNetworkConnected();
        if (NETCONNECTION == 1) {
            GetQtyDtlsParams.put("appkey", MainActivity.appkey);
            GetQtyDtlsParams.put("appsecurity", MainActivity.appsecurity);
            GetQtyDtlsParams.put("id", qtyid);
            getQtyDetails();

        } else {
            new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Check your internet connectivity and try again")
                    .show();
        }

        Log.d("adfwqewrdxz",availqty);
        final Integer actualStock =Integer.valueOf(availqty);

        // Creating the AlertDialog with a custom xml layout (you can still use the default Android version)
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogview = inflater.inflate(R.layout.dialog_edit_qty, null);
        builder.setView(dialogview);
        TextView title = new TextView(this);
        // You Can Customise your Title here
        title.setText("Edit Quantity");
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 20, 10, 20);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        Typeface custom_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        final TextView text = dialogview.findViewById(R.id.tv_qty_edit);
        text.setTypeface(custom_regular);
        text.setText(String.valueOf(getqty));
        Button bt_plus = dialogview.findViewById(R.id.bt_plus);
        Button bt_minus = dialogview.findViewById(R.id.bt_minus);
        Button bt_apply = dialogview.findViewById(R.id.bt_qty_apply);
        bt_plus.setTypeface(custom_bold);
        bt_minus.setTypeface(custom_bold);
        bt_apply.setTypeface(custom_bold);
        title.setTypeface(custom_bold);
        builder.setCustomTitle(title);
        final AlertDialog ad = builder.show();
        bt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getqty >= 1) {
                    if (getqty < actualStock){
                        getqty = getqty + 1;
                    }
                    text.setText(String.valueOf(getqty));
                }else {
                    getqty=1;
                    text.setText(String.valueOf(getqty));
                }
            }
        });
        bt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getqty>1) {
                    getqty = getqty - 1;
                    text.setText(String.valueOf(getqty));
                }else {
                    getqty=1;
                    text.setText(String.valueOf(getqty));
                }
            }
        });
        bt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkConnected();
                if (NETCONNECTION == 1) {
                    QtyEditParams.put("appkey", MainActivity.appkey);
                    QtyEditParams.put("appsecurity", MainActivity.appsecurity);
                    QtyEditParams.put("id", qtyid);
                    QtyEditParams.put("product_id", prodId);
                    Log.d("vcsadasfds",String.valueOf(getqty));
                    QtyEditParams.put("productquantity", String.valueOf(getqty));
                    QtyEditParams.put("uniquedevice", android_id);
                    QtyEditParams.put("devicetype", "0");
                    QtyEditParams.put("stockproductquantity", currentStock);
                    QuantityEdit();
                    ad.dismiss();
                } else {
                    new SweetAlertDialog(Cart.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("No internet")
                            .setContentText("Internet not available, Check your internet connectivity and try again")
                            .show();
                }
            }
        });
    }
    public double calculatePercentage(String amt,String perc){
        double amount = Double.parseDouble(amt);
        double percamt = Double.parseDouble(perc);
        double res = (amount / 100.0f) * percamt;
        return res;
    }
    public String getDecimal(String value){
        String pass;
        DecimalFormat df = new DecimalFormat("#0.00");
        pass = String.valueOf(df.format(Double.parseDouble(value)));
        return pass;
    }
}
