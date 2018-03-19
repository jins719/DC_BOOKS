package app.com.dc_books;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;

/**
 * Created by JINS on 10/31/2017.
 */

public class Profile extends AppCompatActivity {

    ImageView img_dp,img_cameraicon;
    TextInputEditText tile_name,tile_lastname,tile_email,tile_mobile,tile_oldpaw,tile_newpasw,tile_confirmpasw;
    TextInputLayout til_name,til_lastname,til_email,til_mobile,til_oldpasw,til_newpasw,til_confirmpasw;
    RadioGroup radioGroup;
    RadioButton rb_male,rb_female,rb_gender;
    String firstname,lastname,email,mobilenumber,gender_id,oldpasw,newpasw,confirmpasw;
    int NETCONNECTION;

    String profiledetail_url="http://www.level10boutique.com/admin/services/Appprofilelist";
    String profileupdate="http://www.level10boutique.com/admin/services/Appprofileedit";
    String password_update_url="http://www.level10boutique.com/admin/services/Appchangepassword";

    Map<String, String> ProfileDetailsParams = new HashMap<String, String>();
    Map<String, String> PasswordParams = new HashMap<String, String>();

    String appkey="TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc";
    String appsecurity="TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==";

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";
    String userID,image;
    Toolbar toolbar;
    Typeface font,font2;


    SweetAlertDialog image_upload;
    String responce;
    File compressedImageFile=null;
    int selectedId;

    TextView t_resetpassword,t_heading,t_logout,t_logout2;

    String login_type;

    CardView cardView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");

        img_dp=findViewById(R.id.imageView13);
        img_cameraicon=findViewById(R.id.imageView14);
        tile_name=findViewById(R.id.textinputedittext1);
        tile_lastname=findViewById(R.id.textinputedittext2);
        tile_email=findViewById(R.id.textinputedittext3);
        tile_mobile=findViewById(R.id.textinputedittext4);
        tile_oldpaw=findViewById(R.id.textinputedittext5);
        tile_newpasw=findViewById(R.id.textinputedittext6);
        tile_confirmpasw=findViewById(R.id.textinputedittext7);


        til_name=findViewById(R.id.textinputlayout1);
        til_lastname=findViewById(R.id.textinputlayout2);
        til_email=findViewById(R.id.textinputlayout3);
        til_mobile=findViewById(R.id.textinputlayout4);
        til_oldpasw=findViewById(R.id.TextInputLayout5);
        til_newpasw=findViewById(R.id.TextInputLayout6);
        til_confirmpasw=findViewById(R.id.TextInputLayout7);
        radioGroup=findViewById(R.id.radiogroup);
        rb_male=findViewById(R.id.radioButton);
        rb_female=findViewById(R.id.radioButton2);
        cardView=findViewById(R.id.cardview2);

        t_resetpassword=findViewById(R.id.textView38);
        t_heading=findViewById(R.id.imageView);
        t_logout=findViewById(R.id.textView43);
        t_logout2=findViewById(R.id.logout);


        img_dp.setVisibility(View.VISIBLE);


        tile_name.setTypeface(font);
        tile_lastname.setTypeface(font);
        tile_email.setTypeface(font);
        tile_mobile.setTypeface(font);
        tile_oldpaw.setTypeface(font);
        tile_newpasw.setTypeface(font);
        tile_confirmpasw.setTypeface(font);
        t_resetpassword.setTypeface(font);
        t_heading.setTypeface(font);
        t_logout.setTypeface(font2);
        t_logout2.setTypeface(font2);

        tile_oldpaw.setHint("OLD PASSWORD");
        tile_newpasw.setHint("NEW PASSWORD");
        tile_confirmpasw.setHint("CONFIRM PASSWORD");


        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        userID=s.getString("User_id","");


        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        img_cameraicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        img_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(Profile.this,Full_Image_View.class);
                in.putExtra("path","http://www.level10boutique.com/"+image);
                startActivity(in);
            }
        });




        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            ProfileDetailsParams.put("appkey",appkey);
            ProfileDetailsParams.put("appsecurity",appsecurity);
            ProfileDetailsParams.put("id", userID);
            Call_ProfileDetails();
        }else
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Cross check your internet connectivity and try again")
                    .show();
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {


        }
        else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, 0);
        }

    }
    public void updateprofile(View v)
    {
        firstname=tile_name.getText().toString();
        lastname=tile_lastname.getText().toString();
        mobilenumber=tile_mobile.getText().toString();
        email=tile_email.getText().toString();
        til_name.setError(null);
        til_lastname.setError(null);
        til_email.setError(null);
        til_mobile.setError(null);
        selectedId=radioGroup.getCheckedRadioButtonId();

        if(firstname.equals(""))
        {
            til_name.setError("Enter first name");
        }else if(lastname.equals(""))
        {
            til_lastname.setError("Enter last name");
        }
        else if(!email.matches(String.valueOf(android.util.Patterns.EMAIL_ADDRESS)))
        {
            til_email.setError("Enter valid email address");
        }
        else if(selectedId==-1)
        {
            Toast.makeText(getApplicationContext(), "Please choose gender", Toast.LENGTH_SHORT).show();
        }
        else if(mobilenumber.length() < 7 || mobilenumber.length() > 13)
        {
            til_mobile.setError("Enter valid mobile number");
        }
        else
        {
            rb_gender=(RadioButton) findViewById(selectedId);
            if(rb_gender.getText().toString().equals("MALE")){

                gender_id="1";
            }
            else
            {
                gender_id="0";
            }


            isNetworkConnected();
            if(NETCONNECTION==1)
            {

               new executeMultipartPost().execute();
            }else
            {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No internet")
                        .setContentText("Internet not available, Cross check your internet connectivity and try again")
                        .show();
            }
        }

    }
    public void password_update(View v)
    {
        oldpasw=tile_oldpaw.getText().toString();
        newpasw=tile_newpasw.getText().toString();
        confirmpasw=tile_confirmpasw.getText().toString();
        til_oldpasw.setError(null);
        til_newpasw.setError(null);
        til_confirmpasw.setError(null);

        if(oldpasw.equals(""))
        {
            til_oldpasw.setError("Enter old password");
        }else if(newpasw.equals(""))
        {
            til_newpasw.setError("Enter new password");
        }else if(confirmpasw.equals(""))
        {
            til_confirmpasw.setError("Enter confirm password");
        }
        else if(!newpasw.equals(confirmpasw))
        {
            til_confirmpasw.setError("Password mismatch");
        }
        else
        {
            isNetworkConnected();
            if(NETCONNECTION==1)
            {
                PasswordParams.put("appkey",appkey);
                PasswordParams.put("appsecurity",appsecurity);
                PasswordParams.put("userId", userID);
                PasswordParams.put("passphrase",oldpasw);
                PasswordParams.put("password", newpasw);
               Call_passwordchange();
            }else
            {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("No internet")
                        .setContentText("Internet not available, Cross check your internet connectivity and try again")
                        .show();
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){

                    Uri selectedImage = imageReturnedIntent.getData();

                    try {
                        compressedImageFile = new Compressor(this).compressToFile(new File(getRealPathFromURI(selectedImage.toString())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                    Picasso.with(Profile.this)
                            .load(compressedImageFile)
                            .placeholder(R.mipmap.avtar)
                            .error(R.mipmap.avtar)
                            .transform(new CircleTransform())// Uri of the picture
                            .into(img_dp);




                }
                break;
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
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    public void Call_ProfileDetails()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                profiledetail_url, new JSONObject(ProfileDetailsParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce--->"+response.toString());
                        // pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("userslist");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChild = jsonMain.getJSONObject(i);
                                String firstname = jsonChild.getString("firstname");
                                String lastname = jsonChild.getString("lastname");
                                String email = jsonChild.getString("email");
                                String gender = jsonChild.getString("gender");
                                String mobile = jsonChild.getString("mobile");
                                image = jsonChild.getString("image");
                                login_type=jsonChild.getString("applogintype");

                                if(login_type.equals("S"))
                                {
                                    cardView.setVisibility(View.GONE);
                                    t_logout2.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    cardView.setVisibility(View.VISIBLE);
                                    t_logout2.setVisibility(View.GONE);
                                }
                                tile_email.setEnabled(false);





                                    Picasso.with(Profile.this)
                                            .load("http://www.level10boutique.com/"+image)
                                            .placeholder(R.mipmap.avtar)
                                            .error(R.mipmap.avtar)
                                            .transform(new CircleTransform())
                                            .into(img_dp);

                                    edit.putString("UserImage","http://www.level10boutique.com/"+image);
                                    edit.commit();

                                    System.out.println("image full path"+"http://www.level10boutique.com/"+image);


                                tile_name.setText(firstname);
                                tile_lastname.setText(lastname);
                                tile_email.setText(email);
                                tile_mobile.setText(mobile);

                                if(gender.equals("0"))
                                {
                                    rb_female.setChecked(true);
                                }
                                else if(gender.equals("1"))
                                {
                                    rb_male.setChecked(true);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
                        }




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
    public class executeMultipartPost extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();

            image_upload = new SweetAlertDialog(Profile.this, SweetAlertDialog.PROGRESS_TYPE);
            image_upload.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            image_upload.setTitleText("Loading");
            image_upload.setCancelable(true);
            image_upload.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                HttpClient client = new DefaultHttpClient();
                HttpPost postMethod = new HttpPost(profileupdate);



                MultipartEntity entity = new MultipartEntity();
                if(compressedImageFile!=null)
                {
                    File file = new File(getRealPathFromURI(compressedImageFile.getAbsolutePath()));
                    FileBody contentFile = new FileBody(file);
                    entity.addPart("image", contentFile);
                }
                StringBody appkey_android = new StringBody(appkey);
                StringBody appsecurity_android = new StringBody(appsecurity);
                StringBody UserID = new StringBody(userID);
                StringBody FirstName = new StringBody(firstname);
                StringBody LastName = new StringBody(lastname);
                StringBody Email = new StringBody(email);
                StringBody Mobile = new StringBody(mobilenumber);
                StringBody GenderID = new StringBody(gender_id);
                entity.addPart("appkey",appkey_android);
                entity.addPart("appsecurity",appsecurity_android);
                entity.addPart("id", UserID);
                entity.addPart("firstname", FirstName);
                entity.addPart("lastname", LastName);
                entity.addPart("email", Email);
                entity.addPart("gender", GenderID);
                entity.addPart("mobile", Mobile);
                postMethod.setEntity(entity);
                HttpResponse response=client.execute(postMethod);
                responce = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "Responce" + responce);
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("LLLLLLLLLLLL" +e.toString());
                image_upload.dismiss();
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jsonResponse = null;
            image_upload.dismiss();
            System.out.println("LLLLLLLLLLLL" +result.toString());
            try {
                jsonResponse = new JSONObject(responce.toString());

                String Status=jsonResponse.getString("status");

                if(Status.equals("true"))
                {
                    edit.putString("EmailID",email);
                    edit.commit();

                    new SweetAlertDialog(Profile.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Profile updated")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();


                                }
                            })
                            .show();
                }
                else
                {
                    new SweetAlertDialog(Profile.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(jsonResponse.getString("result"))
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            Log.v("log_tag", "LLLLL" + responce);
        }
    }

    public void Call_passwordchange()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                password_update_url, new JSONObject(PasswordParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce--->"+response.toString());
                        // pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));

                            if(jsonResponse.getString("status").equals("true"))
                            {

                                new SweetAlertDialog(Profile.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Password updated")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();


                                            }
                                        })
                                        .show();
                            }
                            else
                            {
                                new SweetAlertDialog(Profile.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(jsonResponse.getString("result"))
                                        .show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
                        }




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

                Profile.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void logout(View v)
    {
        edit.putString("Username","");
        edit.putString("User_id","");
        edit.putString("EmailID","");
        edit.putString("Login_Status","fail");
        edit.putString("Firsttime","NO");
        edit.putString("UserImage","");
        edit.commit();
        finish();
    }
}
