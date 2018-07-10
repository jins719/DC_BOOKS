package app.com.dc_books;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationDetails extends AppCompatActivity {

    String notificationdetails_url="https://dcbookstore.tk/api/notifications/view_notification";
    Map<String, String> NotificationDetailsParams = new HashMap<String, String>();
    TextView t_title,t_description,t_main_title;
    ImageView iv_notification_image;
    Button bt_viewproduct;
    Typeface font1,font2,font3;
    Toolbar toolbar;
    int NETCONNECTION;
    String notification_id ,productid;


    String selectlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);



        if(getIntent().getExtras()!=null)
        {
            Bundle t=getIntent().getExtras();
            notification_id=t.getString("notification_id");
        }

        font1 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3= Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");


        t_title=findViewById(R.id.tv_title);
        t_description=findViewById(R.id.tv_description);
        t_main_title=findViewById(R.id.tv_main_title);
        iv_notification_image=findViewById(R.id.iv_notification_image);
        bt_viewproduct=findViewById(R.id.bt_viewproduct);

        t_title.setTypeface(font2);
        t_main_title.setTypeface(font3);
        bt_viewproduct.setTypeface(font2);
        t_description.setTypeface(font3);

        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            NotificationDetailsParams.put("appkey",MainActivity.appkey);
            NotificationDetailsParams.put("appsecurity",MainActivity.appkey);
            NotificationDetailsParams.put("id",notification_id);
            Call_notificationdetails();
        }else
        {

        }


        bt_viewproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inprddetails=new Intent(NotificationDetails.this,ProductDetails.class);
                inprddetails.putExtra("ProdID",productid);
                startActivity(inprddetails);
            }
        });

    }

    private void Call_notificationdetails() {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                notificationdetails_url, new JSONObject(NotificationDetailsParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("NotificationDetailsResponce---> "+response.toString());
                        // pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("Appnotificationdetails");
                            int lengthJsonArr = jsonMain.length();

                                JSONObject jsonChild = jsonMain.getJSONObject(0);

                                String id=jsonChild.getString("id");
                                String title=jsonChild.getString("title");
                                String description=jsonChild.getString("longmessage");
                                String image=jsonChild.getString("image");
                                productid=jsonChild.getString("productid");

                                if (!(productid.equals(""))){
                                  bt_viewproduct.setVisibility(View.VISIBLE);
                                }
                                t_title.setText(title);
                                t_description.setText(description);
                                if(!(image.equals(""))) {
                                    Glide.with(NotificationDetails.this)
                                            .load("http://dcbookstore.tk/"+image).into(iv_notification_image);
                                }
                            if(lengthJsonArr==0)
                            {

                            }

                        } catch (JSONException e) {
                            System.out.println("JSONExceptionNotificationDetails"+e.toString());
                            e.printStackTrace();

                        }catch (NullPointerException e)
                        {
                            System.out.println("NullPointerExceptionNotificationDetails"+e.toString());
                            e.printStackTrace();
                        }


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                NotificationDetails.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
