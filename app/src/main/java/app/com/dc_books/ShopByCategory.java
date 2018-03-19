package app.com.dc_books;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by JINS on 10/25/2017.
 */

public class ShopByCategory extends AppCompatActivity {


    List<String> listDataHeader;
    List<String> listDataHeaderID;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listDataChild_ID;
    String shopbucategoryurl="http://www.level10boutique.com/admin/services/AppCategorysidebar";
    Map<String, String> CategoryParams = new HashMap<String, String>();

    String appkey="TGV2ZWwtMTBzZWN1cml0eWtleTIwMTc";
    String appsecurity="TGV2ZWwtMTBzZWN1cml0eWNoZWNrMjAxNw==";

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    Toolbar toolbar;
    int lastExpandedPosition=-1;
    int NETCONNECTION;
    String User_ID;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    ImageButton img_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopbycategory);


        expListView =findViewById(R.id.expandable_list);
        img_account=findViewById(R.id.account);

        CategoryParams.put("appkey",appkey);
        CategoryParams.put("appsecurity",appsecurity);
        CategoryParams.put("parent_id","1");


        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        User_ID=s.getString("User_id","");



        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;


                if(listDataChild_ID.get(listDataHeader.get(groupPosition)).size()==0)
                {
                    Intent in=new Intent(ShopByCategory.this,ProductListing.class);
                    in.putExtra("CatID",listDataHeaderID.get(lastExpandedPosition));
                    in.putExtra("Identifier","1");
                    startActivity(in);
                }
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                isNetworkConnected();
                if (NETCONNECTION == 1) {
                    Intent in=new Intent(ShopByCategory.this,ProductListing.class);
                    in.putExtra("CatID", listDataChild_ID.get(listDataHeader.get(groupPosition)).get(childPosition));
                    in.putExtra("Identifier","1");
                    startActivity(in);

                }
                else {

                }

                return false;
            }
        });



        prepareListData();
    }
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
            Intent intent01 = new Intent(ShopByCategory.this, LoginActivity.class);
            intent01.putExtra("Identifier", "2");
            startActivity(intent01);
        }
        else
        {
            Intent in=new Intent(this,Profile.class);
            startActivity(in);
        }
    }
    private void prepareListData() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();


        listDataHeader = new ArrayList<String>();
        listDataHeaderID=new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataChild_ID = new HashMap<String, List<String>>();



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                shopbucategoryurl, new JSONObject(CategoryParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("cccccccc "+response.toString());
                        pDialog.dismiss();

                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray jsonMain = jsonResponse.getJSONArray("categorysidebarlist");
                    int lengthJsonArr = jsonMain.length();
                    for (int i = 0; i < lengthJsonArr; i++) {


                        JSONObject jsonChild = jsonMain.getJSONObject(i);
                        String dist_id = jsonChild.getString("id");
                        String dist_name = jsonChild.getString("name");


                        listDataHeader.add(dist_name);
                        listDataHeaderID.add(dist_id);
                        System.out.println("ssssss " + listDataHeaderID);

                        List<String> SubcategoryVal = new ArrayList<String>();
                        List<String> SubcategoryID = new ArrayList<String>();


                        JSONArray jsonSub = jsonChild.getJSONArray("child_categories");
                        int lengthJsonArrSub = jsonSub.length();
                        for (int j = 0; j < lengthJsonArrSub; j++) {

                            JSONObject jsonChildSub = jsonSub.getJSONObject(j);
                            String subcatID = jsonChildSub.getString("id");
                            String subcatval = jsonChildSub.getString("name");

                           
                            SubcategoryVal.add(subcatval);
                            SubcategoryID.add(subcatID);


                        }

                        listDataChild.put(listDataHeader.get(i), SubcategoryVal);
                        listDataChild_ID.put(listDataHeader.get(i), SubcategoryID);
                        listAdapter = new ExpandableListAdapt(ShopByCategory.this, listDataHeader, listDataChild);
                        expListView.setAdapter(listAdapter);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    System.out.println("ssssss " + e.toString());
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    System.out.println("ssssss " + e.toString());
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:

                ShopByCategory.this.finish();
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
    @Override
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
