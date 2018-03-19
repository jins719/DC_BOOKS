package app.com.dc_books;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by JINS on 10/31/2017.
 */

public class NotificationList extends AppCompatActivity {

    RecyclerView notificationlist;
    Map<String, String> NotificationParams = new HashMap<String, String>();

    String appkey="TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc";
    String appsecurity="TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==";

    int NETCONNECTION;
    String notification_url="http://www.level10boutique.com/admin/services/Appnotificationlisting";
    ArrayList<ItemData> arraylist = new ArrayList<ItemData>();
    MyAdapter mAdapter;
    Typeface font,font2,font3;
    TextView t_header;
    LinearLayoutManager layoutManager;
    SearchView et_search;
    int SearchlayoutVisibleState=0;
    RelativeLayout rtv_searchlayout;
    Toolbar toolbar;

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    String User_ID;

    ImageButton img_account;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationlist);

        font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        font3 = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-SemiBold.ttf");

        NotificationParams.put("appkey",appkey);
        NotificationParams.put("appsecurity",appsecurity);

        notificationlist=findViewById(R.id.recycleview);
        t_header=findViewById(R.id.textView31);
        rtv_searchlayout=findViewById(R.id.relativeLayout2);
        img_account=findViewById(R.id.account);

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        User_ID=s.getString("User_id","");


        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        t_header.setTypeface(font3);

        et_search=findViewById(R.id.editText);
        et_search.setQueryHint("What are you looking for?");
        et_search.setIconified(false);
        int searchIconId = et_search.getContext().getResources().getIdentifier("android:id/search_button",null, null);
        ImageView searchIcon = (ImageView) et_search.findViewById(searchIconId);
        searchIcon.setImageResource(R.mipmap.searchicon);

        rtv_searchlayout.setVisibility(View.GONE);

        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            Call_notificationlist();
        }else
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Cross check your internet connectivity and try again")
                    .show();
        }

        et_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                Intent in=new Intent(NotificationList.this,ProductListing.class);
                in.putExtra("Keyword",query);
                in.putExtra("Identifier","0");
                startActivity(in);

                return false;
            }

        });


    }
    public void search(View v)
    {


        if(SearchlayoutVisibleState==0)
        {


            rtv_searchlayout.setVisibility(View.VISIBLE);
            SearchlayoutVisibleState=1;

        }
        else
        {
            rtv_searchlayout.setVisibility(View.GONE);
            SearchlayoutVisibleState=0;



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
        if(User_ID.equals("")) {
            Intent intent01 = new Intent(NotificationList.this, LoginActivity.class);
            intent01.putExtra("Identifier", "2");
            startActivity(intent01);
        }
        else
        {
            Intent in=new Intent(this,Profile.class);
            startActivity(in);
        }
    }

    private void Call_notificationlist() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                notification_url, new JSONObject(NotificationParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce---> "+response.toString());
                        // pDialog.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("Appnotificationview");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChild = jsonMain.getJSONObject(i);

                                String title=jsonChild.getString("title");
                                String message=jsonChild.getString("message");

                                ItemData itemsData = new ItemData(title,message);
                                arraylist.add(itemsData);
                            }
                            if(lengthJsonArr==0)
                            {
                                new SweetAlertDialog(NotificationList.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Notifications is empty")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                //finish();
                                            }
                                        })
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }catch (NullPointerException e)
                        {

                        }


                        layoutManager = new LinearLayoutManager(NotificationList.this);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        layoutManager.setReverseLayout(true);
                        notificationlist.setLayoutManager(layoutManager);
                        mAdapter = new MyAdapter(arraylist);
                        notificationlist.setAdapter(mAdapter);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(notificationlist.getContext(),
                                layoutManager.getOrientation());
                        notificationlist.addItemDecoration(dividerItemDecoration);
                        notificationlist.setItemAnimator(new DefaultItemAnimator());





                        pDialog.dismissWithAnimation();
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
    public class ItemData {

        String title,message;
        public ItemData(String Title,String Message){

            this.title = Title;
            this.message = Message;
        }
        public String gettitle()
        {
            return this.title;
        }
        public String getmessage()
        {
            return this.message;
        }

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<ItemData> notificationlist = null;
        MyAdapter.ViewHolder viewHolder;
        Activity a;

        public MyAdapter(ArrayList<ItemData> ItemsData) {
            notificationlist = ItemsData;
          //  a = activity;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_adapter, null);
            viewHolder = new MyAdapter.ViewHolder(itemLayoutView);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int position) {


            viewHolder.t_title.setTypeface(font2);
            viewHolder.t_message.setTypeface(font);

            viewHolder.t_title.setText(notificationlist.get(position).gettitle());
            viewHolder.t_message.setText(notificationlist.get(position).getmessage());




        }

        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView t_title,t_message;



            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                t_title =itemLayoutView.findViewById(R.id.textView32);
                t_message=itemLayoutView.findViewById(R.id.textView33);

               /* itemLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in=new Intent(getActivity(),ProductListing.class);
                        in.putExtra("CatID",notificationlist.get(getAdapterPosition()).getcat_id());
                        in.putExtra("Identifier","1");
                        startActivity(in);

                    }
                });*/


            }
        }// Return the size of your itemsData (invoked by the layout manager)

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return notificationlist.size();
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
                NotificationList.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onResume() {
        super.onResume();
        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        User_ID=s.getString("User_id","");

        if(User_ID.equals(""))
        {
            img_account.setImageResource(R.mipmap.topiconaccount);
        }
        else
        {
            img_account.setImageResource(R.mipmap.topicon04);
        }
    }


}
