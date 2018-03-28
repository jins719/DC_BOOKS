package app.com.dc_books;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by JINS on 10/12/2017.
 */

public class ProductDetails extends AppCompatActivity {

    String ProductDetails_Url="http://192.168.1.18:8080/dcbooks/api/product/details";
    String Reviewproduct_Url="http://192.168.1.18:8080/dcbooks/api/productreview/submit";
    String Mywishlist_Url;
    Map<String, String> ProductParams = new HashMap<String, String>();
    Map<String, String> ProductReviewParams = new HashMap<String, String>();
    Map<String, String> WishParams = new HashMap<String, String>();
    String appkey="aec2a0b15161ae445865b32bbefef972";
    String appsecurity="928e6af859edef918313aac98d5d48ee";

    TextView t_productname,t_actualprice,
            t_sellingprice,t_heading_productdetails,t_price;

    Typeface font,font2,font3;


    ViewPager viewPager;
    CircleIndicator indicator;

    ArrayList<String> BannerImage = new ArrayList<>();
    SearchView et_search;
    int SearchlayoutVisibleState=0;
    RelativeLayout rtv_searchlayout;
    String product_id,stockproductqty;
    Toolbar toolbar;

    ImageView ib_wishlist;
    int WISHLIST_STATE;
    String badgeimage;
    ImageView badgeview;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    String userID,sellingprice,specialdiscount;
    ImageButton img_account;

    RatingBar ratingBar,reviewrating;
    ImageButton add;
    CardView cardView2,writereview_cardview;

    String bookname,author,category,isbn,binding,publishingdate,publisher,edition,
            numberofpages,language,stockavilable,delivery,already_reviewed="false";
    
    TextView t_bookname,t_author,t_category,t_isbn,t_binding,t_publishingdate,t_publisher,t_edition,
            t_numberofpages,t_language,t_stockavilable,t_delivery,t_authorby,t_writereviewheading,
            t_productnameheading,t_productnaemreview,t_yourrating;

    TextView T_book,T_author,T_category,T_isbn,T_binding,T_Pubdate,T_pub,T_edition,T_nopages,T_language;

    TextInputLayout ti_title,ti_message;
    TextInputEditText te_title,te_message;

    //Network status
    int NETCONNECTION;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productdetails);


        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3= Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");

        if(getIntent().getExtras()!=null)
        {
            Bundle t=getIntent().getExtras();
            product_id=t.getString("ProdID");
        }

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");

        t_productname=findViewById(R.id.textView6);
        t_actualprice=findViewById(R.id.textView5);
        t_sellingprice=findViewById(R.id.textView8);
        t_heading_productdetails=findViewById(R.id.textView10);
        t_price=findViewById(R.id.textView7);


        t_bookname=findViewById(R.id.bookname);
        t_author=findViewById(R.id.author);
        t_category=findViewById(R.id.category);
        t_isbn=findViewById(R.id.isbn);
        t_binding=findViewById(R.id.binding);
        t_publishingdate=findViewById(R.id.publishingdate);
        t_publisher=findViewById(R.id.publisher);
        t_edition=findViewById(R.id.edition);
        t_numberofpages=findViewById(R.id.numberofpages) ;
        t_language=findViewById(R.id.language);
        t_stockavilable=findViewById(R.id.stockavilable);
        t_delivery=findViewById(R.id.delivery);
        t_authorby=findViewById(R.id.textView45);
        t_writereviewheading=findViewById(R.id.writereviewheading);
        t_productnameheading=findViewById(R.id.productnameheading);
        t_productnaemreview=findViewById(R.id.productnaemreview);
        t_yourrating=findViewById(R.id.yourrating);

        T_book=findViewById(R.id.T_book);
        T_author=findViewById(R.id.T_author);
        T_category=findViewById(R.id.T_category);
        T_isbn=findViewById(R.id.T_isbn);
        T_binding=findViewById(R.id.T_binding);
        T_Pubdate=findViewById(R.id.T_Pubdate);
        T_pub=findViewById(R.id.T_pub);
        T_edition=findViewById(R.id.T_edition);
        T_nopages=findViewById(R.id.T_nopages);
        T_language=findViewById(R.id.T_language);

        ti_title=findViewById(R.id.ti_title);
        ti_message=findViewById(R.id.ti_message);
        te_title=findViewById(R.id.te_title);
        te_message=findViewById(R.id.te_message);

        writereview_cardview=findViewById(R.id.writereview_cardview);


        ratingBar=findViewById(R.id.ratingbar);
        reviewrating=findViewById(R.id.ratingbar2);
        viewPager=findViewById(R.id.viewPager);
        indicator =findViewById(R.id.indicator);
        rtv_searchlayout=findViewById(R.id.relativeLayout2);
        et_search=findViewById(R.id.editText);
        ib_wishlist=findViewById(R.id.button_previous);
        et_search.clearFocus();
        et_search.setQueryHint("What are you looking for?");
        badgeview=findViewById(R.id.imageView2);
        img_account=findViewById(R.id.account);
        cardView2=findViewById(R.id.cardview2);
        cardView2.setVisibility(View.INVISIBLE);



        int searchIconId = et_search.getContext().getResources().getIdentifier("android:id/search_button",null, null);
        ImageView searchIcon = (ImageView) et_search.findViewById(searchIconId);
        searchIcon.setImageResource(R.mipmap.searchicon);
        add=findViewById(R.id.button);
        add.setEnabled(false);

        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Font set all textview and edittext
        t_productname.setTypeface(font);
        t_productnaemreview.setTypeface(font);
        t_productnameheading.setTypeface(font3);
        t_actualprice.setTypeface(font3);
        t_sellingprice.setTypeface(font3);
        t_yourrating.setTypeface(font);
        t_heading_productdetails.setTypeface(font3);
        t_price.setTypeface(font3);
        te_title.setTypeface(font);
        te_message.setTypeface(font);

        t_bookname.setTypeface(font);
        t_author.setTypeface(font);
        t_category.setTypeface(font);
        t_isbn.setTypeface(font);
        t_binding.setTypeface(font);
        t_publishingdate.setTypeface(font);
        t_publisher.setTypeface(font);
        t_edition.setTypeface(font);
        t_numberofpages.setTypeface(font);
        t_language.setTypeface(font);
        t_stockavilable.setTypeface(font);
        t_delivery.setTypeface(font);
        t_authorby.setTypeface(font);
        t_writereviewheading.setTypeface(font);
        t_productnameheading.setTypeface(font);
        t_productnaemreview.setTypeface(font);
        t_yourrating.setTypeface(font);

        T_book.setTypeface(font3);
        T_author.setTypeface(font3);
        T_category.setTypeface(font3);
        T_isbn.setTypeface(font3);
        T_binding.setTypeface(font3);
        T_Pubdate.setTypeface(font3);
        T_pub.setTypeface(font3);
        T_edition.setTypeface(font3);
        T_nopages.setTypeface(font3);
        T_language.setTypeface(font3);




        t_actualprice.setPaintFlags(t_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);




        ProductParams.put("appkey",appkey);
        ProductParams.put("appsecurity",appsecurity);
        ProductParams.put("product_id",product_id);
        ProductParams.put("user_id", userID);



        CallProductDetails();




        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent in=new Intent(ProductDetails.this,ProductListing.class);
                in.putExtra("Keyword",query);
                in.putExtra("Identifier","0");
                startActivity(in);

                return false;
            }

        });

        ib_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WishParams.put("appkey",appkey);
                WishParams.put("appsecurity",appsecurity);
                WishParams.put("product_id",product_id);
                WishParams.put("user_id", userID);



                if(!userID.equals("")) {

                    if (WISHLIST_STATE == 0) {
                        Mywishlist_Url = "http://192.168.1.18:8080/dcbooks/api/wishlist/add";
                        ib_wishlist.setImageResource(R.mipmap.heartfill);
                        WISHLIST_STATE = 1;
                    } else {
                        Mywishlist_Url = "http://192.168.1.18:8080/dcbooks/api/wishlist/remove";
                        ib_wishlist.setImageResource(R.mipmap.heart);
                        WISHLIST_STATE = 0;
                    }
                    MywishList();
                }
                else
                {
                    Intent intent=new Intent(ProductDetails.this,LoginActivity.class);
                    intent.putExtra("Identifier","5");
                    startActivity(intent);

                }
            }

        });

        if(userID.equals(""))
        {
            img_account.setImageResource(R.mipmap.topiconaccount);

        }
        else
        {
            img_account.setImageResource(R.mipmap.topicon04);
        }

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
    public void Review(View v)
    {
        ti_title.setError(null);
        ti_message.setError(null);

        ProductReviewParams.clear();
        ProductReviewParams.put("appkey",appkey);
        ProductReviewParams.put("appsecurity",appsecurity);
        ProductReviewParams.put("product_id",product_id);
        ProductReviewParams.put("user_id", userID);
        ProductReviewParams.put("title",te_title.getText().toString());
        ProductReviewParams.put("message",te_message.getText().toString());
        ProductReviewParams.put("rating",String.valueOf(reviewrating.getRating()));
        isNetworkConnected();

        if(te_title.getText().toString().equals(""))
        {
            ti_title.setError("Please enter convenient title");
        }
        else if(te_message.getText().toString().equals(""))
        {
            ti_message.setError("enter your feedback");
        }
        else if(NETCONNECTION==0)
        {
        new SweetAlertDialog(ProductDetails.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("No internet")
                .setContentText("Internet not available, Cross check your internet connectivity and try again")
                .show();
        }
        else
        {
            ReviewWebservice();
        }
    }
    public void Add(View v)
    {

        Intent in=new Intent(ProductDetails.this,Cart.class);
        in.putExtra("ProductID",product_id);
        in.putExtra("Qty",stockproductqty);
        in.putExtra("OfferPrice",sellingprice);
        startActivity(in);

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
        if(userID.equals(""))
        {
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

    // Webservice call categories
    public void CallProductDetails()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ProductDetails_Url, new JSONObject(ProductParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce---> "+response.toString());
                      // pDialog.dismiss();
                        add.setEnabled(true);

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("products");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChild = jsonMain.getJSONObject(i);
                                String productid=jsonChild.getString("id");
                                String image=jsonChild.getString("image");
                                sellingprice=jsonChild.getString("selling_price");
                                bookname=jsonChild.getString("title");
                                String code=jsonChild.getString("code");
                                String actualprice=jsonChild.getString("actual_price");
                                isbn=jsonChild.getString("isbn");
                                //stock=jsonChild.getString("stock");
                                binding=jsonChild.getString("binding");
                                language=jsonChild.getString("language");
                                author=jsonChild.getString("author");
                                publisher=jsonChild.getString("publisher");
                                publishingdate=jsonChild.getString("publishing_date");
                                edition=jsonChild.getString("edition");
                                numberofpages=jsonChild.getString("number_of_pages");
                                language=jsonChild.getString("language");
                                stockavilable=jsonChild.getString("stock");
                                delivery=jsonChild.getString("delivered_in");
                                category=jsonChild.getString("category");
                                already_reviewed=jsonChild.getString("reviewed_already");
                                String rating=jsonChild.getString("average_rating");
                                String wishlist=jsonChild.getString("wishlist");
                                if(wishlist.equals("true"))
                                {
                                    ib_wishlist.setImageResource(R.mipmap.heartfill);
                                    WISHLIST_STATE=1;
                                }
                                else
                                {
                                    ib_wishlist.setImageResource(R.mipmap.heart);
                                    WISHLIST_STATE=0;
                                }
                                t_bookname.setText(bookname);
                                t_author.setText(author);
                                t_category.setText(author);
                                t_isbn.setText(isbn);
                                t_binding.setText(binding);
                                t_publishingdate.setText(publishingdate);
                                t_publisher.setText(publisher);
                                t_edition.setText(edition);
                                t_numberofpages.setText(numberofpages);
                                t_language.setText(language);
                                t_stockavilable.setText(stockavilable);
                                t_delivery.setText(delivery);
                                t_authorby.setText("By : "+author);
                                t_category.setText(category);
                                t_productnaemreview.setText(bookname);
                                ratingBar.setRating(Float.parseFloat(rating));
                                //set enable set false
                                ratingBar.setEnabled(false);
                                DecimalFormat df = new DecimalFormat("#0.00");
                                t_productname.setText(bookname);
                                t_actualprice.setText(String.valueOf(df.format(Double.parseDouble(actualprice))));
                                t_sellingprice.setText("Sale: Rs. "+String.valueOf(df.format(Double.parseDouble(sellingprice))));
                                BannerImage.add("http://192.168.1.18:8080/dcbooks/"+image);
                                Glide.with(ProductDetails.this)
                                        .load(badgeimage).into(badgeview);
                                //Already Reviewed
                                if(already_reviewed.equals("true"))
                                {
                                    writereview_cardview.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("gallery");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChild = jsonMain.getJSONObject(i);
                                String image=jsonChild.getString("image");
                                if(!image.equals(""))
                                {
                                    BannerImage.add("http://192.168.1.18:8080/dcbooks/"+image);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(ProductDetails.this,BannerImage);
                        viewPager.setAdapter(mCustomPagerAdapter);
                        indicator.setViewPager(viewPager);

                        cardView2.setVisibility(View.VISIBLE);
                        pDialog.dismiss();

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

    public void MywishList()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Mywishlist_Url, new JSONObject(WishParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("hellllllllll "+response.toString());
                        System.out.println("hellllllllll "+ new JSONObject(WishParams));
                         pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                             if(jsonResponse.getString("status").equals("true"))
                             {
                                 new SweetAlertDialog(ProductDetails.this, SweetAlertDialog.SUCCESS_TYPE)
                                         .setTitleText("Good job!")
                                         .setContentText(response.getString("result"))
                                         .show();
                             }
                             else
                             {
                                 new SweetAlertDialog(ProductDetails.this, SweetAlertDialog.SUCCESS_TYPE)
                                         .setTitleText("Good job!")
                                         .setContentText(response.getString("result"))
                                         .show();
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
                pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    // Write Review Webservice Call
    public void ReviewWebservice()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Reviewproduct_Url, new JSONObject(ProductReviewParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            if(jsonResponse.getString("status").equals("true"))
                            {
                                new SweetAlertDialog(ProductDetails.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Good job!")
                                        .setContentText(response.getString("result"))
                                        .show();

                                    writereview_cardview.setVisibility(View.GONE);
                            }
                            else
                            {
                                new SweetAlertDialog(ProductDetails.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Failed!")
                                        .setContentText(response.getString("result"))
                                        .show();
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
                pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }});
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }


    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<String> BannerImages = new ArrayList<String>();
        android.widget.ImageView ImageView;

        public CustomPagerAdapter(Context context, ArrayList<String> BannerImages) {
            mContext = context;

            this.BannerImages = BannerImages;
            mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return BannerImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = mLayoutInflater.inflate(R.layout.bannerimage_adapter, container, false);
            ImageView =view.findViewById(R.id.imageView7);

            Glide.with(ProductDetails.this)
                    .load(BannerImages.get(position)).into(ImageView);



            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView((RelativeLayout) obj);
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

                ProductDetails.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            writereview_cardview.setVisibility(View.GONE);
        }
        else
        {
            img_account.setImageResource(R.mipmap.topicon04);
            if(already_reviewed.equals("true"))
            {
                writereview_cardview.setVisibility(View.GONE);
            }
            else
            {
                writereview_cardview.setVisibility(View.VISIBLE);
            }

        }

    }

    //Check internet connection
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
