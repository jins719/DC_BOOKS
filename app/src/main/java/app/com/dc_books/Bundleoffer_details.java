package app.com.dc_books;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.relex.circleindicator.CircleIndicator;

public class Bundleoffer_details  extends AppCompatActivity {

    String ProductDetails_Url="https://dcbookstore.tk/api/product/details";

    Map<String, String> ProductParams = new HashMap<String, String>();

    String product_id;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    Typeface font,font2,font3;

    String userID;

    ArrayList<String> BannerImage = new ArrayList<>();

    ViewPager viewPager;
    CircleIndicator indicator;

    TextView t_title;

    TextView t_actualamount;

    TextView t_sellingamount;

    TextView t_offerdisclaimer;

    TextView t_offervalidity;

    TextView t_books;

    TextView T_price;

    TextView T_offerdeclaimer;

    TextView T_offerDetails;

    TextView T_book;

    Toolbar toolbar;

    ImageView ib_wishlist;

    Map<String, String> WishParams = new HashMap<String, String>();

    int WISHLIST_STATE;

    String Mywishlist_Url;

    int SearchlayoutVisibleState=0;

    RelativeLayout rtv_searchlayout;

    SearchView et_search;

    ImageButton img_account;

    String already_reviewed="false";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bundleoffer_details);


        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3= Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");

        if(getIntent().getExtras()!=null)
        {
            Bundle t=getIntent().getExtras();
            product_id=t.getString("ProdID");
        }


        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }



        t_title=findViewById(R.id.textView6);
        t_actualamount=findViewById(R.id.textView5);
        t_sellingamount=findViewById(R.id.textView8);
        t_offerdisclaimer=findViewById(R.id.textView46);
        t_offervalidity=findViewById(R.id.textView9);
        t_books=findViewById(R.id.bookname);
        T_price=findViewById(R.id.textView7);
        T_offerdeclaimer=findViewById(R.id.textView11);
        T_offerDetails=findViewById(R.id.textView10);
        T_book=findViewById(R.id.T_book);
        ib_wishlist=findViewById(R.id.button_previous);
        rtv_searchlayout=findViewById(R.id.relativeLayout2);
        et_search=findViewById(R.id.editText);
        et_search.clearFocus();
        et_search.setQueryHint("What are you looking for?");
        img_account=findViewById(R.id.account);

        t_actualamount.setPaintFlags(t_actualamount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        t_title.setTypeface(font3);
        t_actualamount.setTypeface(font2);
        t_sellingamount.setTypeface(font2);
        t_offerdisclaimer.setTypeface(font);
        t_offervalidity.setTypeface(font2);
        t_books.setTypeface(font);
        T_price.setTypeface(font3);
        T_offerdeclaimer.setTypeface(font3);
        T_offerDetails.setTypeface(font3);
        T_book.setTypeface(font3);


        viewPager=findViewById(R.id.viewPager);
        indicator =findViewById(R.id.indicator);

        int searchIconId = et_search.getContext().getResources().getIdentifier("android:id/search_button",null, null);
        ImageView searchIcon = (ImageView) et_search.findViewById(searchIconId);
        searchIcon.setImageResource(R.mipmap.searchicon);

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");

        ProductParams.put("appkey",MainActivity.appkey);
        ProductParams.put("appsecurity",MainActivity.appsecurity);
        ProductParams.put("product_id",product_id);
        ProductParams.put("user_id", userID);
        ProductParams.put("producttype", "4");


        et_search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent in=new Intent(Bundleoffer_details.this,Bundle_offer.class);
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


        ib_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WishParams.put("appkey",MainActivity.appkey);
                WishParams.put("appsecurity",MainActivity.appsecurity);
                WishParams.put("product_id",product_id);
                WishParams.put("producttype",String.valueOf(MainActivity.Brand_Type));
                WishParams.put("user_id", userID);



                if(!userID.equals("")) {

                    if (WISHLIST_STATE == 0) {
                        Mywishlist_Url = "https://dcbookstore.tk/api/wishlist/add";
                        ib_wishlist.setImageResource(R.mipmap.heartfill);
                        WISHLIST_STATE = 1;
                    } else {
                        Mywishlist_Url = "https://dcbookstore.tk/api/wishlist/remove";
                        ib_wishlist.setImageResource(R.mipmap.heart);
                        WISHLIST_STATE = 0;
                    }
                    MywishList();
                }
                else
                {
                    Intent intent=new Intent(Bundleoffer_details.this,LoginActivity.class);
                    intent.putExtra("Identifier","5");
                    startActivity(intent);

                }
            }

        });

        CallProductDetails();

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
    public void Add(View v)
    {

        Intent in=new Intent(Bundleoffer_details.this,Cart.class);
        in.putExtra("ProductID",product_id);
        in.putExtra("Qty","1");
        in.putExtra("OfferPrice","");
        in.putExtra("Producttype","4");
        in.putExtra("prepublicationid","");
        in.putExtra("offerid","");
        startActivity(in);

    }

    public void CallProductDetails()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        System.out.println("wowwwwwwwwwwww "+new JSONObject(ProductParams));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ProductDetails_Url, new JSONObject(ProductParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce--->"+response.toString());
                        // pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("products");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChild = jsonMain.getJSONObject(i);

                                t_title.setText(jsonChild.getString("title"));
                                t_actualamount.setText(jsonChild.getString("totalamount"));
                                t_sellingamount.setText("Sale: Rs. "+jsonChild.getString("sellingamount"));
                                BannerImage.add("http://dcbookstore.tk/"+jsonChild.getString("image"));
                                t_offerdisclaimer.setText(jsonChild.getString("disclaimer"));
                                t_offervalidity.setText("Offer valid till "+jsonChild.getString("startdate"));
                                t_books.setText(jsonChild.getString("books"));
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

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(Bundleoffer_details.this,BannerImage);
                        viewPager.setAdapter(mCustomPagerAdapter);
                        indicator.setViewPager(viewPager);


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

            Glide.with(Bundleoffer_details.this)
                    .load(BannerImages.get(position)).into(ImageView);



            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView((RelativeLayout) obj);
        }
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
                                new SweetAlertDialog(Bundleoffer_details.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Good job!")
                                        .setContentText(response.getString("result"))
                                        .show();
                            }
                            else
                            {
                                new SweetAlertDialog(Bundleoffer_details.this, SweetAlertDialog.SUCCESS_TYPE)
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

                Bundleoffer_details.this.finish();
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

        }
        else
        {
            img_account.setImageResource(R.mipmap.topicon04);

        }

    }



}
