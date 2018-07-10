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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class GiftandToys extends AppCompatActivity {

    ViewPager viewPager;
    CircleIndicator indicator;

    TextView t_nameoftoy,t_barnd,t_price,t_sale,t_Model;
    TextView t_titleofproductheading,t_productdec;
    TextView t_productdetailsheading,t_title,t_category,t_code,t_brand,t_model,t_sku,t_size;

    TextView t_headingtitle,t_headingcategory,t_headingcode,t_headingbrand,t_headingmodel,
                t_headingsku,t_headingsize;

    Typeface font,font2,font3;

    String ProductDetails_Url="https://dcbookstore.tk/api/product/details";
    String Mywishlist_Url;

    Map<String, String> ProductParams = new HashMap<String, String>();
    Map<String, String> WishParams = new HashMap<String, String>();

    String product_id;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    String userID;

    Toolbar toolbar;

    ImageView ib_wishlist;

    ArrayList<String>BannerImage=new ArrayList<>();

    int WISHLIST_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giftandtoys);

        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3= Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");


        viewPager=findViewById(R.id.viewPager);
        indicator =findViewById(R.id.indicator);

        t_nameoftoy=findViewById(R.id.textView6);
        t_barnd=findViewById(R.id.textView45);
        t_price=findViewById(R.id.textView5);
        t_sale=findViewById(R.id.textView8);
        t_Model=findViewById(R.id.t_model);

        t_titleofproductheading=findViewById(R.id.textView11);
        t_productdec=findViewById(R.id.textView46);

        t_productdetailsheading=findViewById(R.id.textView13);
        t_title=findViewById(R.id.textView14);
        t_category=findViewById(R.id.textView48);
        t_code=findViewById(R.id.textView50);
        t_brand=findViewById(R.id.textView52);
        t_model=findViewById(R.id.textView54);
        t_sku=findViewById(R.id.textView56);
        t_size=findViewById(R.id.textView58);

        t_headingtitle=findViewById(R.id.textView47);
        t_headingcategory=findViewById(R.id.textView49);
        t_headingcode=findViewById(R.id.textView51);
        t_headingbrand=findViewById(R.id.textView53);
        t_headingmodel=findViewById(R.id.textView55);
        t_headingsku=findViewById(R.id.textView57);
        t_headingsize=findViewById(R.id.textView59);

        ib_wishlist=findViewById(R.id.button_previous);


        t_nameoftoy.setTypeface(font);
        t_barnd.setTypeface(font);
        t_price.setTypeface(font);
        t_sale.setTypeface(font);
        t_Model.setTypeface(font);
        t_titleofproductheading.setTypeface(font3);
        t_productdetailsheading.setTypeface(font3);
        t_title.setTypeface(font);
        t_category.setTypeface(font);
        t_code.setTypeface(font);
        t_brand.setTypeface(font);
        t_model.setTypeface(font);
        t_sku.setTypeface(font);
        t_size.setTypeface(font);

        t_headingtitle.setTypeface(font3);
        t_headingcategory.setTypeface(font3);
        t_headingcode.setTypeface(font3);
        t_headingbrand.setTypeface(font3);
        t_headingmodel.setTypeface(font3);
        t_headingsku.setTypeface(font3);
        t_headingsize.setTypeface(font3);


        if(getIntent().getExtras()!=null)
        {
            Bundle t=getIntent().getExtras();
            product_id=t.getString("ProdID");
        }

        // Read values SharedPreferences
        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");
        //Back arrow set toolbar
        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Add and remove wish list button onclick
        ib_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WishParams.put("appkey",MainActivity.appkey);
                WishParams.put("appsecurity",MainActivity.appsecurity);
                WishParams.put("product_id",product_id);
                WishParams.put("producttype","2");
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
                    Intent intent=new Intent(GiftandToys.this,LoginActivity.class);
                    intent.putExtra("Identifier","5");
                    startActivity(intent);

                }
            }

        });


        ProductParams.put("appkey",MainActivity.appkey);
        ProductParams.put("appsecurity",MainActivity.appsecurity);
        ProductParams.put("product_id",product_id);
        ProductParams.put("user_id", userID);
        ProductParams.put("producttype","2");


        CallProductDetails();

    }
    public void Add(View v)
    {
        Intent in=new Intent(GiftandToys.this,Cart.class);
        in.putExtra("ProductID",product_id);
        in.putExtra("Producttype",String.valueOf(MainActivity.Brand_Type));
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

                                BannerImage.add("http://dcbookstore.tk/"+jsonChild.getString("image"));

                                t_nameoftoy.setText(jsonChild.getString("title"));
                                t_barnd.setText("Brand : "+jsonChild.getString("brand"));
                                t_Model.setText("Model : "+jsonChild.getString("model"));
                                DecimalFormat df = new DecimalFormat("#0.00");
                                t_price.setText(String.valueOf(df.format(Double.parseDouble(jsonChild.getString("actual_price")))));
                                t_price.setPaintFlags(t_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                t_sale.setText("Sale: Rs. "+String.valueOf(df.format(Double.parseDouble(jsonChild.getString("selling_price")))));
                                t_productdec.setText(jsonChild.getString("description"));
                                t_title.setText(jsonChild.getString("title"));
                                t_category.setText(jsonChild.getString("category"));
                                t_code.setText(jsonChild.getString("code"));
                                t_brand.setText(jsonChild.getString("brand"));
                                t_model.setText(jsonChild.getString("model"));
                                t_sku.setText(jsonChild.getString("sku"));
                                t_size.setText(jsonChild.getString("length")+" "+jsonChild.getString("unit")+","
                                +jsonChild.getString("breadth")+" "+jsonChild.getString("unit")+","+jsonChild.getString("width")+" "+jsonChild.getString("unit"));
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

                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
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
                                    BannerImage.add("http://dcbookstore.tk/"+image);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(GiftandToys.this,BannerImage);
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

            Glide.with(GiftandToys.this)
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

                GiftandToys.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                                new SweetAlertDialog(GiftandToys.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Good job!")
                                        .setContentText(response.getString("result"))
                                        .show();
                            }
                            else
                            {
                                new SweetAlertDialog(GiftandToys.this, SweetAlertDialog.SUCCESS_TYPE)
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


}
