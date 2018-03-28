package app.com.dc_books;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout rl_create_header,rl_login_header,rl_create_body,rl_login_body,rl_create_container,rl_login_container;
    CardView cv_login_main, cv_create_main;
    TextView tv_forgot,tv_login_subtext,tv_create_subtext,tv_welcome;
    RadioButton rb_create,rb_login;
    Button bt_continue,bt_login;
    ScrollView sv_scroll;
    EditText et_firstName,et_lastName,et_email,et_pass,et_confrm_pass,et_login_email,et_login_pass;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    Toolbar toolbar;
    public static final String mp = "";
    int NETCONNECTION;
    String ip_head = "http://192.168.1.18:8080/dcbooks/";
    String login_url= "http://192.168.1.18:8080/dcbooks/api/user/login";
    String register_url= "http://192.168.1.18:8080/dcbooks/api/user/register";
    String forgot_password_url="http://192.168.1.18:8080/dcbooks/api/user/forgot_password";
    Map<String, String> LoginParams = new HashMap<>();
    Map<String, String> RegisterParams = new HashMap<>();
    Map<String, String> ForgotPassParams = new HashMap<>();
    public  String email,psw,reg_email,firstnme,lastnme,reg_pass,reg_confrmpass;
    String identifier;
    Bundle extras;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 9001;
    CallbackManager callbackManager;
    ImageButton loginButton;
    String device_id="";

    String appkey="aec2a0b15161ae445865b32bbefef972";
    String appsecurity="928e6af859edef918313aac98d5d48ee";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginButton = findViewById(R.id.bt_fblogin);

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        toolbar=findViewById(R.id.toolbar);
        sv_scroll=findViewById(R.id.sv_main);
        ImageButton bt_google_login = findViewById(R.id.bt_google_login);
        et_firstName=findViewById(R.id.et_first_name);
        et_lastName=findViewById(R.id.et_last_name);
        et_email=findViewById(R.id.et_email);
        et_pass=findViewById(R.id.et_password);
        et_confrm_pass=findViewById(R.id.et_confirm_pass);
        et_login_email=findViewById(R.id.et_login_email);
        et_login_pass=findViewById(R.id.et_login_pass);
        rb_create=findViewById(R.id.rb_create);
        rb_login=findViewById(R.id.rb_login);
        rl_login_header=findViewById(R.id.rl_login_child1);
        rl_login_body=findViewById(R.id.rl_login_child2);
        rl_create_header=findViewById(R.id.rl_create_child1);
        rl_create_body=findViewById(R.id.rl_create_child2);
        rl_create_container=findViewById(R.id.rl_cv_create_container);
        rl_login_container=findViewById(R.id.rl_cv_login_container);
        cv_login_main =findViewById(R.id.cv_login);
        cv_create_main =findViewById(R.id.cv_create);
        bt_continue=findViewById(R.id.bt_continue);
        bt_login=findViewById(R.id.bt_login);
        tv_forgot=findViewById(R.id.tv_forgot);
        tv_create_subtext=findViewById(R.id.tv_create_subtext);
        tv_login_subtext=findViewById(R.id.tv_login_subtext);
        tv_welcome=findViewById(R.id.tv_welcome);
        rb_login.setChecked(true);
        final Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        final Typeface custom_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        bt_continue.setTypeface(custom_bold);
        bt_login.setTypeface(custom_bold);
        tv_forgot.setTypeface(custom_regular);
        rb_login.setTypeface(custom_bold);
        rb_create.setTypeface(custom_regular);
        tv_login_subtext.setTypeface(custom_regular);
        tv_create_subtext.setTypeface(custom_regular);
        tv_welcome.setTypeface(custom_bold);
        et_firstName.setTypeface(custom_regular);
        et_lastName.setTypeface(custom_regular);
        et_email.setTypeface(custom_regular);
        et_pass.setTypeface(custom_regular);
        et_confrm_pass.setTypeface(custom_regular);
        et_login_email.setTypeface(custom_regular);
        et_login_pass.setTypeface(custom_regular);

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        device_id=s.getString("Token","0");

        if(device_id.equals("0"))
        {
            Toast.makeText(getApplication(),device_id,Toast.LENGTH_SHORT).show();
            device_id = FirebaseInstanceId.getInstance().getToken();
            edit.putString("Token",device_id);
            edit.commit();
        }


        ForgotPassParams.put("appkey", appkey);
        ForgotPassParams.put("appsecurity",appsecurity);
        toolbar.setTitle("");


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacebookRegister();
            }
        });

        extras = getIntent().getExtras();
        identifier ="0";

        if (extras != null) {
            identifier = extras.getString("Identifier");
        }

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String token= FirebaseInstanceId.getInstance().getToken();
        Log.d("fkjsakfjak",token.toString());
        sv_scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        sv_scroll.setFocusable(true);
        sv_scroll.setFocusableInTouchMode(true);
        sv_scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        bt_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                updateUI(account);
            }
        });

        GradientDrawable bgShape = (GradientDrawable)rl_create_container.getBackground();
        bgShape.setColor(Color.parseColor("#e5e5e5"));
        if(rb_create.isChecked()){
            rl_create_body.setVisibility(View.VISIBLE);
            rl_login_body.setVisibility(View.GONE);
            rl_login_header.setBackgroundColor(Color.parseColor("#f2f2f2"));
        }else {
            rl_login_body.setVisibility(View.VISIBLE);
            rl_create_body.setVisibility(View.GONE);
            rl_create_header.setBackgroundColor(Color.parseColor("#f2f2f2"));
        }

        rb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rb_create.isChecked()){
                    rb_create.setChecked(false);
                }
                rl_create_body.setVisibility(View.GONE);
                rl_login_body.setVisibility(View.VISIBLE);
                rl_create_header.setBackgroundColor(Color.parseColor("#f2f2f2"));
                rl_login_header.setBackgroundColor(Color.parseColor("#ffffff"));
                GradientDrawable bgShape = (GradientDrawable)rl_create_container.getBackground();
                bgShape.setColor(Color.parseColor("#e5e5e5"));
                rb_login.setTypeface(custom_bold);
                rb_create.setTypeface(custom_regular);

            }
        });

        rb_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rb_login.isChecked()){
                    rb_login.setChecked(false);
                }
                rl_login_body.setVisibility(View.GONE);
                rl_create_body.setVisibility(View.VISIBLE);
                rl_login_header.setBackgroundColor(Color.parseColor("#f2f2f2"));
                rl_create_header.setBackgroundColor(Color.parseColor("#ffffff"));
                GradientDrawable bgShape = (GradientDrawable)rl_login_container.getBackground();
                bgShape.setColor(Color.parseColor("#e5e5e5"));
                rb_login.setTypeface(custom_regular);
                rb_create.setTypeface(custom_bold);

            }
        });

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstnme = et_firstName.getText().toString().trim();
                lastnme = et_lastName.getText().toString().trim();
                reg_email = et_email.getText().toString().trim();
                reg_pass = et_pass.getText().toString().trim();
                reg_confrmpass = et_confrm_pass.getText().toString().trim();
                rl_create_body.clearFocus();
                if(firstnme.equals("")){
                    et_firstName.requestFocus();
                    et_firstName.setError("Please enter first name");
                }else if(lastnme.equals("")) {
                    et_lastName.requestFocus();
                    et_lastName.setError("Please enter last name");
                }else if(!reg_email.matches(String.valueOf(android.util.Patterns.EMAIL_ADDRESS))) {
                    et_email.requestFocus();
                    et_email.setError("Please enter valid email id");
                } else if (reg_pass.equals("")) {
                    et_pass.requestFocus();
                    et_pass.setError("Your passwords must match and must be at least 6 characters long.");
                } else if (!(reg_pass.equals(reg_confrmpass))) {
                    et_confrm_pass.requestFocus();
                    et_confrm_pass.setError("Your passwords must match and must be at least 6 characters long.");
                } else {


                    RegisterParams.put("firstname", firstnme);
                    RegisterParams.put("lastname", lastnme);
                    RegisterParams.put("email", reg_email);
                    RegisterParams.put("password", reg_pass);
                    RegisterParams.put("appkey",MainActivity.appkey);
                    RegisterParams.put("appsecurity",MainActivity.appsecurity);


                    isNetworkConnected();
                    if(NETCONNECTION==1)
                    {
                        Register();
                    }
                    else
                    {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("No internet")
                                .setContentText("Internet not available, Check your internet connectivity and try again")
                                .show();
                    }


                }
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_login_email.getText().toString().trim();
                psw = et_login_pass.getText().toString().trim();

                if (!email.matches(String.valueOf(android.util.Patterns.EMAIL_ADDRESS))) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Invalid Email")
                            .setContentText("Please enter valid email id")
                            .show();

                } else if (psw.equals("")) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Invalid Password")
                            .setContentText("Please enter password")
                            .show();
                } else {

                    LoginParams.put("email", email);
                    LoginParams.put("password", psw);
                    LoginParams.put("appkey",appkey);
                    LoginParams.put("appsecurity",appsecurity);
                    LoginParams.put("deviceid", device_id);
                    LoginParams.put("devicetype", "Android");
                    LoginParams.put("applogintype", "DcbooksApp");


                    isNetworkConnected();
                    if(NETCONNECTION==1)
                    {
                        Login();
                    }
                    else
                    {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("No internet")
                                .setContentText("Internet not available, Check your internet connectivity and try again")
                                .show();
                    }


                }
            }
        });

    }


    private void updateUI( @Nullable GoogleSignInAccount account) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void FacebookRegister() {

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }



    public void forgotpassword(View view){
        // Creating the AlertDialog with a custom xml layout (you can still use the default Android version)
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogview = inflater.inflate(R.layout.dialog_forgot_password, null);
        builder.setView(dialogview);
        TextView title = new TextView(this);
        // You Can Customise your Title here
        title.setText("Forgot Password");
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 20, 10, 20);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        Typeface custom_regular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        final EditText text = dialogview.findViewById(R.id.et_forgpass);
        text.setTypeface(custom_regular);
        Button button = dialogview.findViewById(R.id.bt_forgsub);
        button.setTypeface(custom_bold);
        title.setTypeface(custom_bold);
        builder.setCustomTitle(title);
        final AlertDialog ad = builder.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkConnected();
                if(NETCONNECTION==1)
                {
                    if(!text.getText().toString().matches(String.valueOf(android.util.Patterns.EMAIL_ADDRESS))) {
                        text.setError("Please enter valid email id");
                    }else {
                        ForgotPassParams.put("email", text.getText().toString());
                        ForgotPassword();
                        ad.dismiss();
                    }
                }else {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("No internet")
                            .setContentText("Internet not available, Check your internet connectivity and try again")
                            .show();
                }
            }
        });
    }

    public void Login(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        System.out.println("posting data "+new JSONObject(LoginParams));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                login_url, new JSONObject(LoginParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Login Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("result");


                            if(Status.equals("true"))
                            {
                                String userimage=jsonResponse.getString("profile_image");
                                edit.putString("Username",email);
                                edit.putString("User_id",jsonResponse.getString("user_id"));
                                edit.putString("UserImage", ip_head+userimage);
                                edit.putString("EmailID",email);
                                edit.putString("Login_Status","success");
                                edit.putString("Firsttime","YES");
                                edit.commit();
                                et_login_email.getText().clear();
                                et_login_pass.getText().clear();

                                if(identifier.equals("2")){
                                    Intent in=new Intent(LoginActivity.this,Profile.class);
                                    startActivity(in);
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    LoginActivity.this.finish();
                                }else if(identifier.equals("3")){
                                    Intent in=new Intent(LoginActivity.this,WishList.class);
                                    startActivity(in);
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    LoginActivity.this.finish();
                                }else if(identifier.equals("4")){
                                    Intent in=new Intent(LoginActivity.this,OrderList.class);
                                    startActivity(in);
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    LoginActivity.this.finish();
                                }else if(identifier.equals("5")) {
                                   /* Intent in=new Intent(LoginActivity.this,ProductDetails.class);
                                    startActivity(in);*/
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    LoginActivity.this.finish();
                                }else if(identifier.equals("6")) {

                                    Intent in=new Intent(LoginActivity.this,OrderSummary.class);
                                    in.putExtra("totalpayamount",extras.getString("totalpayamount"));
                                    in.putExtra("vouchername",extras.getString("vouchername"));
                                    in.putExtra("voucheramount",extras.getString("voucheramount"));
                                    in.putExtra("subtotal",extras.getString("subtotal"));
                                    in.putExtra("Weight",extras.getString("Weight"));
                                    startActivity(in);
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    LoginActivity.this.finish();
                                }
                                else{
                                    Intent in=new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(in);
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                    LoginActivity.this.finish();
                                    }
                            }
                            else
                            {
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
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

                System.out.println("Login Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void Register(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                register_url, new JSONObject(RegisterParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Registration Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("message");

                            if(Status.equals("true"))
                            {

                                et_firstName.getText().clear();
                                et_lastName.getText().clear();
                                et_email.getText().clear();
                                et_pass.getText().clear();
                                et_confrm_pass.getText().clear();

                                rl_login_body.setVisibility(View.VISIBLE);
                                rl_create_body.setVisibility(View.GONE);
                                rl_create_header.setBackgroundColor(Color.parseColor("#f2f2f2"));
                                rl_login_header.setBackgroundColor(Color.parseColor("#ffffff"));
                                rb_create.setChecked(false);
                                rb_login.setChecked(true);

//                                edit.putString("Username",jsonResponse.getString("name"));
//                                edit.putString("User_id",jsonResponse.getString("id"));
//                                edit.putString("Login_Status","success");
//                                edit.putString("Firsttime","YES");
//                                edit.commit();
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Registration Success")
                                        .setContentText(Message)
                                        .show();
//                                Intent in=new Intent(LoginActivity.this,MainActivity.class);
//                                startActivity(in);
//                                finish();

                            }
                            else
                            {
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(Message)
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();

                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
                            pDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                System.out.println("Registration Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void ForgotPassword(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                forgot_password_url, new JSONObject(ForgotPassParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();

                        System.out.println("Forgot Password Responce---> "+response.toString());

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            String Status=jsonResponse.getString("status");
                            String Message=jsonResponse.getString("result");

                            if(Status.equals("true"))
                            {
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success")
                                        .setContentText(Message)
                                        .show();
                            }
                            else
                            {
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
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

                System.out.println("Forgot Password Responce---> "+error.toString());
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

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                LoginActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);

            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            JSONObject jsonResponse = object;



                            try {
                                email = jsonResponse.getString("email");
                                String firstname = jsonResponse.getString("first_name");
                                String lastname = jsonResponse.getString("last_name");
                                String  uniqid = jsonResponse.getString("id");

                                LoginParams.put("appkey",MainActivity.appkey);
                                LoginParams.put("appsecurity",MainActivity.appsecurity);
                                LoginParams.put("email",email );
                                LoginParams.put("uniqueid",uniqid);
                                LoginParams.put("deviceid", device_id);
                                LoginParams.put("devicetype", "Android");
                                LoginParams.put("applogintype", "Facebook");
                                LoginParams.put("firstname", firstname);
                                LoginParams.put("lastname", lastname);

                                login_url= "http://192.168.1.18:8080/dcbooks/api/user/social_login";


                                isNetworkConnected();
                                if(NETCONNECTION==1)
                                {
                                    Login();
                                }
                                else
                                {
                                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("No internet")
                                            .setContentText("Internet not available, Check your internet connectivity and try again")
                                            .show();
                                }
                            }catch (NullPointerException e) {
                                Log.d("JSONExceptionLogin",e.toString());
                                e.printStackTrace();

                            }catch (JSONException e) {
                                Log.d("JSONExceptionLogin",e.toString());
                                e.printStackTrace();
                            }
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        intent.putExtra("userProfile", json_object.toString());
//                        startActivity(intent);
                        }

                    });
            Bundle permission_param = new Bundle();
            permission_param.putString("fields", "id,name,email,first_name,last_name,picture.width(120).height(120)");
            request.setParameters(permission_param);
            request.executeAsync();
        }
    }

    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {

        //If the login succeed
        if (result.isSuccess()) {

            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

             email = acct.getEmail();
            String firstname = acct.getDisplayName();
            String lastname = acct.getFamilyName();
            String uniqid = acct.getId();

            LoginParams.put("appkey",MainActivity.appkey);
            LoginParams.put("appsecurity",MainActivity.appsecurity);
            LoginParams.put("email",email );
            LoginParams.put("uniqueid",uniqid);
            LoginParams.put("deviceid", device_id);
            LoginParams.put("devicetype", "Android");
            LoginParams.put("firstname", firstname);
            LoginParams.put("lastname", lastname);
            LoginParams.put("applogintype", "Gmail");

            login_url= "http://192.168.1.18:8080/dcbooks/api/user/social_login";


            isNetworkConnected();
            if(NETCONNECTION==1)
            {
                Login();
            }
            else
            {
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No internet")
                        .setContentText("Internet not available, Check your internet connectivity and try again")
                        .show();
            }
        }
    }
}
