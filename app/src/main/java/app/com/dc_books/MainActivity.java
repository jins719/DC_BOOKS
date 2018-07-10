package app.com.dc_books;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import app.com.dc_books.Expand.ShopByCategory;


public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    RelativeLayout rtv_searchlayout;
    int SearchlayoutVisibleState = 0;
    SearchView et_search;
    Typeface font;
    FrameLayout framelayout;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";
    String Name, Email, User_ID, user_image;
    RelativeLayout mainLayout;
    BroadcastReceiver mReceiver;
    int NETCONNECTION=1;
    Snackbar snackbar;
    ImageButton img_account;

    public static String appkey="aec2a0b15161ae445865b32bbefef972";
    public static String appsecurity="928e6af859edef918313aac98d5d48ee";

    public static int Brand_Type;
    public static int Brand_Type_Flag;
    public static String languageType;

    Spinner sp_languagetype;
    String [] Languages={"Malayalam Books","English Books"};
    FragmentManager fragmentManager;
    Fragment home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        rtv_searchlayout = findViewById(R.id.relativeLayout2);
        et_search = findViewById(R.id.editText);
        framelayout = findViewById(R.id.framelayout);
        mainLayout= findViewById(R.id.mainlayout);
        img_account=findViewById(R.id.account);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
        }
        else
        {
            try {
                ProviderInstaller.installIfNeeded(getApplicationContext());
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, null, null);
                SSLEngine engine = sslContext.createSSLEngine();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }



        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");


        //Font set edittext
        et_search.setQueryHint("What are you looking for?");
        et_search.setIconified(false);
        int searchIconId = et_search.getContext().getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = (ImageView) et_search.findViewById(searchIconId);
        searchIcon.setImageResource(R.mipmap.searchicon);


        //et_search.setTypeface(font);


        final ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        rtv_searchlayout.setVisibility(View.GONE);


        Setup_navigation();
        setup_navigation_spinnerview();



        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent in = new Intent(MainActivity.this, ProductListing.class);
                in.putExtra("Keyword", query);
                in.putExtra("Identifier", "0");
                in.putExtra("Identifier", "0");
                startActivity(in);

                rtv_searchlayout.setVisibility(View.GONE);
                SearchlayoutVisibleState = 0;
                et_search.clearFocus();
                et_search.setQuery("", false);


                return false;
            }

        });




      /*  fragmentManager = getFragmentManager();
        home = new Home();
        fragmentManager.beginTransaction().replace(R.id.framelayout, home).commit();
        Brand_Type=1;*/







    }
    // setup navigation spinner view
    private void setup_navigation_spinnerview()
    {
        View headerview = navigationView.getHeaderView(0);
        sp_languagetype=headerview.findViewById(R.id.language);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Languages);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_languagetype.setAdapter(dataAdapter);

        sp_languagetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.WHITE);

                try {

                    if (i == 0) {
                        languageType = "1";
                        Brand_Type = 1;
                        Brand_Type_Flag = Brand_Type;
                        fragmentManager = getFragmentManager();
                        home = new Home();
                        fragmentManager.beginTransaction().replace(R.id.framelayout, home).commit();
                        mDrawerLayout.closeDrawers();
                    } else {
                        languageType = "2";
                        Brand_Type = 1;
                        Brand_Type_Flag = Brand_Type;
                        fragmentManager = getFragmentManager();
                        home = new Home();
                        fragmentManager.beginTransaction().replace(R.id.framelayout, home).commit();
                        mDrawerLayout.closeDrawers();
                    }
                }catch (IllegalStateException e)
                {

                }



            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void Setup_navigation() {


        if (navigationView != null) {
            setupDrawerContent(navigationView);
            View headerview = navigationView.getHeaderView(0);

            Menu m = navigationView.getMenu();
            for (int i=0;i<m.size();i++) {
                MenuItem mi = m.getItem(i);
                //for aapplying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu!=null && subMenu.size() >0 ) {
                    for (int j=0; j <subMenu.size();j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }
                }
                //the method we have create in activity
                applyFontToMenuItem(mi);
            }


            sp = getSharedPreferences(mp, 0);
            edit = sp.edit();
            SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
            Name = s.getString("Username", "");
            Email = s.getString("EmailID", "");
            User_ID = s.getString("User_id", "");
            user_image = s.getString("UserImage", "");




            RelativeLayout header = headerview.findViewById(R.id.relativelayout);
            ImageView profileimage = headerview.findViewById(R.id.imageView12);
            TextView t_name = headerview.findViewById(R.id.textView35);
            TextView t_profileletter = headerview.findViewById(R.id.textView34);
            TextView t_login = headerview.findViewById(R.id.textView36);

            t_name.setTypeface(font);
            t_login.setTypeface(font);

            if (Name.equals("")) {

                t_name.setText("Hi Guest");
                t_login.setText("Login");
                img_account.setImageResource(R.mipmap.topiconaccount);
            } else {
                t_name.setText("Hi "+Name);
                t_login.setText("");
                img_account.setImageResource(R.mipmap.topicon04);




                    Picasso.with(MainActivity.this)
                            .load(user_image)
                            .error(R.mipmap.avtar)
                            .transform(new CircleTransform())
                            .into(profileimage);




            }

            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (User_ID.equals("")) {
                        Intent in = new Intent(MainActivity.this, LoginActivity.class);
                        in.putExtra("Identifier", "2");
                        startActivity(in);
                        mDrawerLayout.closeDrawers();
                    } else {
                        Intent in = new Intent(MainActivity.this, Profile.class);
                        in.putExtra("Identifier", "2");
                        startActivity(in);
                        mDrawerLayout.closeDrawers();
                    }
                }
            });
        }
    }
    private void applyFontToMenuItem(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    //Search onclick
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
    public void cart(View v) {
        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            Intent in = new Intent(this, Cart.class);
            in.putExtra("Identifier", "7");
            startActivity(in);
        }
    }

    //Profile onclick
    public void account(View v) {
        if (User_ID.equals("")) {
            Intent in = new Intent(this, LoginActivity.class);
            in.putExtra("Identifier", "2");
            startActivity(in);
        } else {
            Intent in = new Intent(this, Profile.class);
            startActivity(in);
        }

    }


    void setupDrawerContent(final NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        fragmentManager = getFragmentManager();
                        Fragment home = new Home();
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                Brand_Type=1;
                                Brand_Type_Flag=Brand_Type;
                                fragmentManager.beginTransaction().replace(R.id.framelayout, home).commit();
                                break;
                            case R.id.giftandtoys:
                                Brand_Type=2;
                                Brand_Type_Flag=Brand_Type;
                                fragmentManager.beginTransaction().replace(R.id.framelayout, home).commit();
                                break;
                            case R.id.shop_category:
                                Intent in=new Intent(MainActivity.this,ShopByCategory.class);
                                startActivity(in);
                                mDrawerLayout.closeDrawers();

                                break;
                            case R.id.wishlist:
                                if(User_ID.equals(""))
                                {
                                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                                    intent.putExtra("Identifier","3");
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent=new Intent(MainActivity.this,WishList.class);
                                    startActivity(intent);
                                }

                                break;
                            case R.id.order:
                                if(User_ID.equals("")) {
                                    Intent intent01 = new Intent(MainActivity.this, LoginActivity.class);
                                    intent01.putExtra("Identifier", "4");
                                    startActivity(intent01);
                                }
                                else
                                {
                                    Intent intent=new Intent(MainActivity.this,OrderList.class);
                                    startActivity(intent);
                                }
                                break;
                            case R.id.notification:
                                Intent intent02=new Intent(MainActivity.this,NotificationList.class);
                                startActivity(intent02);
                                break;
                            case R.id.about:
                                Intent intent03=new Intent(MainActivity.this,AboutUs.class);
                                startActivity(intent03);
                                break;
                            case R.id.contactus:
                                Intent intent04=new Intent(MainActivity.this,ContactUs.class);
                                startActivity(intent04);
                                break;
                            case R.id.shipping_and_privacy:
                                Intent intent05=new Intent(MainActivity.this,ShippingAndPrivacy.class);
                                startActivity(intent05);
                                break;
                            case R.id.preorder:
                                Brand_Type=6;
                                Intent i=new Intent(MainActivity.this,Bundle_offer.class);
                                i.putExtra("CatID","0");
                                i.putExtra("CatName","Pre order");
                                i.putExtra("Identifier","1");
                                startActivity(i);
                                break;
                            case R.id.top10:
                                Brand_Type=7;
                                Intent top100=new Intent(MainActivity.this,Bundle_offer.class);
                                top100.putExtra("CatID","0");
                                top100.putExtra("CatName","DC Top 100");
                                top100.putExtra("Identifier","1");
                                startActivity(top100);
                                break;

                            case R.id.bundle:
                                Brand_Type=4;
                                Intent bundle=new Intent(MainActivity.this,Bundle_offer.class);
                                bundle.putExtra("CatID","0");
                                bundle.putExtra("CatName","Bundle offers");
                                bundle.putExtra("Identifier","1");
                                startActivity(bundle);
                                break;
                            case R.id.award_winners:
                                Brand_Type=5;
                                Intent award_winners=new Intent(MainActivity.this,Bundle_offer.class);
                                award_winners.putExtra("CatID","0");
                                award_winners.putExtra("CatName","Award winners");
                                award_winners.putExtra("Identifier","1");
                                startActivity(award_winners);
                                break;
                            case R.id.email_gift_voucher:
                                Brand_Type=8;
                                Intent emailgiftvoucher=new Intent(MainActivity.this,Email_Giftvoucher_List.class);
                                emailgiftvoucher.putExtra("CatID","0");
                                emailgiftvoucher.putExtra("CatName","Email Gift Voucher");
                                emailgiftvoucher.putExtra("Identifier","1");
                                startActivity(emailgiftvoucher);
                                break;



                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        Name=s.getString("Username","");
        Email=s.getString("EmailID","");
        User_ID=s.getString("User_id","");
        Setup_navigation();



        IntentFilter intentFilter = new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE");
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {




                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {


                        if(NETCONNECTION==0)
                        {

                            try {


                                snackbar = Snackbar
                                        .make(mainLayout, "Internet connected", Snackbar.LENGTH_SHORT);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_btn_bg_pressed_color));
                                snackbar.setActionTextColor(Color.WHITE);
                                snackbar.show();
                                NETCONNECTION = 1;

                                FragmentManager fragmentManager = getFragmentManager();
                                Fragment home = new Home();
                                fragmentManager.beginTransaction().replace(R.id.framelayout, home).commit();
                            }catch (RuntimeException e)
                            {

                            }
                        }

                    } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                        NETCONNECTION=0;
                        snackbar = Snackbar
                                .make(mainLayout, "Sorry! Internet not connected", Snackbar.LENGTH_INDEFINITE);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.Red));
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                    }
                }
            }
        };


        try {
            this.registerReceiver(mReceiver, intentFilter);
        }catch (Exception e)
        {

        }
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {

            Bitmap bitmap = null;

            try {
                int size = Math.min(source.getWidth(), source.getHeight());

                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;

                Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                if (squaredBitmap != source) {
                    source.recycle();
                }

                bitmap = Bitmap.createBitmap(size, size, source.getConfig());

                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                BitmapShader shader = new BitmapShader(squaredBitmap,
                        BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);

                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);

                squaredBitmap.recycle();
            }catch (Exception e)
            {
            }
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
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


}
