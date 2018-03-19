package app.com.dc_books;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by JINS on 11/21/2017.
 */

public class AdddressEdit extends AppCompatActivity {
    EditText et_name,et_pincode,et_city,et_address,et_phone;
    String name,pincode,city,selectedstate,address,phone,userID,selectedcountry;
    TextInputLayout til_name,til_pincode,til_city,til_address,til_phone;
    Spanned span_name,span_pincode,span_city,span_province,span_address,span_phone;
    Toolbar toolbar;
    int NETCONNECTION;
    Typeface font,font2,font3;

    String state_url="http://www.level10boutique.com/admin/services/Appstates";
    String country_url="http://www.level10boutique.com/admin/services/Appcountry";
    String addaddress_url;
    String address_url="http://www.level10boutique.com/admin/services/Appuseraddresseditlist";
    Map<String, String> StateParams = new HashMap<String, String>();
    Map<String, String> DistrictParams = new HashMap<String, String>();
    Map<String, String> UserParams = new HashMap<String, String>();
    Map<String, String> AddressParams = new HashMap<String, String>();

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    String appkey="TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc";
    String appsecurity="TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==";

    ArrayList<String> CountryID=new ArrayList<>();
    ArrayList<String>CountryName=new ArrayList<>();

    ArrayList<String>StateID=new ArrayList<>();
    ArrayList<String>StateName=new ArrayList<>();

    Spinner spr_contryname,spr_state;
    TextView t_state,t_country;
    TextView t_Heading;

    ImageButton b_addaddress;
    int Editstatus=1;


    String country_id,state_id;
    SweetAlertDialog pDialog;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");


        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");

        //EditText declaration



        et_name=(EditText)findViewById(R.id.editText1);
        et_pincode=(EditText)findViewById(R.id.editText2);
        et_city=(EditText)findViewById(R.id.editText3);
        et_address=(EditText)findViewById(R.id.editText10);
        et_phone=(EditText)findViewById(R.id.editText6);
        t_country=findViewById(R.id.textview4);
        spr_contryname=findViewById(R.id.spinner);
        spr_state=findViewById(R.id.spinner2);
        constraintLayout=findViewById(R.id.contraint);


        t_state=findViewById(R.id.textView28);
        t_Heading=findViewById(R.id.testview01);
        b_addaddress=findViewById(R.id.button);

        t_state.setTypeface(font);
        t_country.setTypeface(font);
        t_Heading.setTypeface(font2);

            t_Heading.setText("EDIT ADDRESS");
            b_addaddress.setImageResource(R.mipmap.updateaddressbutton);





        // TextInputLayout declaration

        til_name=(TextInputLayout)findViewById(R.id.textInputLayout);
        til_pincode=(TextInputLayout)findViewById(R.id.textInputLayout7);
        til_city=(TextInputLayout)findViewById(R.id.textInputLayout8);
        til_address=(TextInputLayout)findViewById(R.id.TextInputLayout10);
        til_phone=(TextInputLayout)findViewById(R.id.textInputLayout6);


        //Hint set font
        til_name.setTypeface(font);
        til_pincode.setTypeface(font);
        til_city.setTypeface(font);
        til_address.setTypeface(font);
        til_phone.setTypeface(font);


        //Hint set edittext

        et_name.setTypeface(font);
        et_pincode.setTypeface(font);
        et_city.setTypeface(font);
        et_address.setTypeface(font);
        et_phone.setTypeface(font);



        // Mandatory declaration

        span_name=(Html.fromHtml("Name<sup>*</sup>"));
        span_pincode=(Html.fromHtml("Pin code<sup>*</sup>"));
        span_city=(Html.fromHtml("City<sup>*</sup>"));
        span_address=(Html.fromHtml("Address<sup>*</sup>"));
        span_phone=(Html.fromHtml("Mobile Number<sup>*</sup>"));
        span_province=(Html.fromHtml("Province<sup>*</sup>"));

        //Add hint to the TextInputLayout

        til_name.setHint(span_name);
        til_pincode.setHint(span_pincode);
        til_city.setHint(span_city);
        til_address.setHint(span_address);
        til_phone.setHint(span_phone);





        //Back button set in toolbar

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        isNetworkConnected();
        if(NETCONNECTION==0)
        {
            new SweetAlertDialog(AdddressEdit.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Cross check your internet connectivity and try again")
                    .show();
        }
        else
        {
            AddressParams.put("appkey",appkey);
            AddressParams.put("appsecurity",appsecurity);
            AddressParams.put("id",getIntent().getExtras().getString("AddressID"));
            Call_Address();

        }


        spr_contryname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedcountry=CountryID.get(i);

                if(i==0)
                {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.Gray));
                }
                else
                {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                }



                StateParams.put("appkey",appkey);
                StateParams.put("appsecurity",appsecurity);
                StateParams.put("countryId",selectedcountry);
                State_List();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spr_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedstate=StateID.get(i);

                if(i==0)
                {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.Gray));
                }
                else
                {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    // Add address onclick function

    public void Add(View v)
    {
        name=et_name.getText().toString();
        pincode=et_pincode.getText().toString();
        city=et_city.getText().toString();
        address=et_address.getText().toString();
        phone=et_phone.getText().toString();


        til_name.setError(null);
        til_pincode.setError(null);
        til_city.setError(null);
        til_address.setError(null);
        til_phone.setError(null);


        isNetworkConnected();

        if(name.equals(""))
        {
            til_name.setError("Enter name");

        }
        else if(pincode.equals(""))
        {
            til_pincode.setError("Enter pin code");
        }
        else if(selectedcountry.equals("0"))
        {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "You must select country", Snackbar.LENGTH_SHORT);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.Red));
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
        }
        else if(selectedstate.equals("0"))
        {
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, "You must select state", Snackbar.LENGTH_SHORT);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.Red));
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();
        }

        else if(city.equals(""))
        {
            til_city.setError("Enter city");
        }
        else if(!phone.matches(String.valueOf(Patterns.PHONE)))
        {
            til_phone.setError("Invalid mobile number");
        }
        else if(address.equals(""))
        {
            til_address.setError("Enter address");
        }
        else if(NETCONNECTION==0)
        {
            new SweetAlertDialog(AdddressEdit.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Cross check your internet connectivity and try again")
                    .show();
        }
        else
        {

                addaddress_url="http://www.level10boutique.com/admin/services/Appuseraddressedit";
                UserParams.clear();
                UserParams.put("appkey",appkey);
                UserParams.put("appsecurity",appsecurity);
                UserParams.put("id",getIntent().getExtras().getString("AddressID"));
                UserParams.put("user_id",userID);
                UserParams.put("state_id",selectedstate);
                UserParams.put("country_id",selectedcountry);
                UserParams.put("firstname",name);
                UserParams.put("lastname","");
                UserParams.put("address1",address);
                UserParams.put("city",city);
                UserParams.put("pincode",pincode);
                UserParams.put("mobile",phone);
                UserAddAddress();


        }

    }

    //Country webservice call

    private void Country_List() {

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        CountryID.add("0");
        CountryName.add("--Select country--");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                country_url, new JSONObject(DistrictParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce"+response.toString());
                        //pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("Countries");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {

                                JSONObject jsonChild = jsonMain.getJSONObject(i);

                                String contry_id=jsonChild.getString("id");
                                String contry_name=jsonChild.getString("name");

                                CountryID.add(contry_id);
                                CountryName.add(contry_name);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdddressEdit.this, android.R.layout.simple_spinner_item, CountryName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spr_contryname.setAdapter(adapter);
                        spr_contryname.setSelection(CountryID.indexOf(country_id));




                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                System.out.println("UserList Responce---> "+error.toString());


            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    //State webservice call
    private void State_List() {

       /* final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();*/

        StateID.clear();
        StateName.clear();
        StateID.add("0");
        StateName.add("--Select state--");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                state_url, new JSONObject(StateParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce"+response.toString());
                          pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("States");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {

                                JSONObject jsonChild = jsonMain.getJSONObject(i);

                                String state_id=jsonChild.getString("id");
                                String state_name=jsonChild.getString("name");

                                StateID.add(state_id);
                                StateName.add(state_name);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }



                        if(Editstatus==1)
                        {
                            Editstatus=0;
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdddressEdit.this, android.R.layout.simple_spinner_item, StateName);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spr_state.setAdapter(adapter);
                            spr_state.setSelection(StateID.indexOf(state_id));

                        }
                        else
                        {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdddressEdit.this, android.R.layout.simple_spinner_item, StateName);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spr_state.setAdapter(adapter);
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                  pDialog.dismiss();
                System.out.println("UserList Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    //USER ADD ADDRESS
    private void UserAddAddress() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                addaddress_url, new JSONObject(UserParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce"+response.toString());
                        pDialog.dismiss();
                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            if(jsonResponse.getString("status").equals("true"))
                            {
                                et_name.getText().clear();
                                et_address.getText().clear();
                                et_phone.getText().clear();
                                et_city.getText().clear();
                                et_pincode.getText().clear();
                                spr_contryname.setSelection(0);
                                spr_state.setSelection(0);

                                new SweetAlertDialog(AdddressEdit.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Good job!")
                                        .setContentText(jsonResponse.getString("message"))
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        }).show();
                            }
                            else
                            {
                                new SweetAlertDialog(AdddressEdit.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(jsonResponse.getString("message"))
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
                new SweetAlertDialog(AdddressEdit.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(error.toString())
                        .show();

                System.out.println("UserList Responce---> "+error.toString());


            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    public void Call_Address()
    {
       /* final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();*/

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                address_url, new JSONObject(AddressParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Address responce"+response.toString());

                       //  pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("useraddresseditlist");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {

                                JSONObject jsonChild = jsonMain.getJSONObject(i);

                                String firstname=jsonChild.getString("firstname");
                                country_id=jsonChild.getString("country_id");
                                String address=jsonChild.getString("address1");
                                String city=jsonChild.getString("city");
                                String pincode=jsonChild.getString("pincode");
                                String mobile=jsonChild.getString("mobile");
                                state_id=jsonChild.getString("state_id");




                                et_name.setText(firstname);
                                et_pincode.setText(pincode);
                                et_city.setText(city);
                                et_address.setText(address);
                                et_phone.setText(mobile);




                                DistrictParams.put("appkey",appkey);
                                DistrictParams.put("appsecurity",appsecurity);
                                Country_List();




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        } catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    // Toolbar Back button

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:

                AdddressEdit.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Phone Back button

    public void onBackPressed()
    {
        AdddressEdit.this.finish();
    }

    //Internet connection check function

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
