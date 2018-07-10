package app.com.dc_books.Expand;

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
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.com.dc_books.AppController;
import app.com.dc_books.Cart;
import app.com.dc_books.LoginActivity;
import app.com.dc_books.MainActivity;
import app.com.dc_books.ProductListing;
import app.com.dc_books.Profile;
import app.com.dc_books.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by JINS on 10/25/2017.
 */

public class ShopByCategory extends AppCompatActivity {


    List<String> listDataHeader;
    List<String> listDataHeaderID;
    List<String> emptyList;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listDataChild_ID;

    HashMap<String, List<String>> listDataChild2;
    HashMap<String, List<String>> listDataChild_ID2;

    ArrayList<ExpandableClass> parentarr=new ArrayList<>();
    ArrayList<ExpandableCollectiveClass> secondmain=new ArrayList<>();
    ArrayList<ExpandableAllDataCollection> alldatacollection=new ArrayList<>();
    ArrayList<ExpandableAllData> secondnode = new ArrayList<>();


    String shopbucategoryurl="https://dcbookstore.tk/api/category/shop_by_category";
    Map<String, String> CategoryParams = new HashMap<String, String>();


    ExpandableListAdapter listAdapter;
    Toolbar toolbar;
    int lastExpandedPosition=-1;
    int NETCONNECTION;
    String User_ID;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    public static final String mp = "";

    ImageButton img_account;


    ExpandableListView expandableListView;

    LinkedHashMap<String, String[]> thirdLevelMovies = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> thirdLevelGames = new LinkedHashMap<>();
    List<String[]> secondLevel = new ArrayList<>();
    List<LinkedHashMap<String, String[]>> data = new ArrayList<>();
    String subcatval2;
    String subcatmain_id2;
    String dist_name;
    String main_id1;
    String subcatval;
    String submain_id;
    String selectlang;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopbycategory);



        img_account=findViewById(R.id.account);

        CategoryParams.put("appkey", MainActivity.appkey);
        CategoryParams.put("appsecurity",MainActivity.appsecurity);
        CategoryParams.put("parent_id","1");


        toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }






        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
                System.out.println("clicked " +String.valueOf(groupPosition));
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                isNetworkConnected();
                if (NETCONNECTION == 1) {
                    Intent in=new Intent(ShopByCategory.this,ProductListing.class);
                    in.putExtra("CatName",listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                    in.putExtra("CatID", listDataChild_ID.get(listDataHeader.get(groupPosition)).get(childPosition));
                    in.putExtra("Identifier","1");
                    startActivity(in);
                }
                else {

                }

                return false;
            }
        });





        sp = getSharedPreferences(mp, 0);
        edit = sp.edit();
        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);
        User_ID=s.getString("User_id","");



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
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(true);
        pDialog.show();


        listDataHeader = new ArrayList<String>();
        listDataHeaderID=new ArrayList<String>();
        emptyList=new ArrayList<>();
        emptyList.add("empty");

        listDataChild = new HashMap<String, List<String>>();
        listDataChild_ID = new HashMap<String, List<String>>();
        listDataChild2 = new HashMap<>();
        listDataChild_ID2=new HashMap<>();



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
                        dist_name = jsonChild.getString("name");
                        main_id1 = jsonChild.getString("parent_id");


                        parentarr.add(new ExpandableClass(dist_id,dist_name,main_id1));
                        listDataHeader.add(dist_name);
                        listDataHeaderID.add(dist_id);
                        System.out.println("ssssss " + listDataHeaderID);

                        List<String> SubcategoryVal = new ArrayList<String>();
                        List<String> SubcategoryID = new ArrayList<String>();


                        JSONArray jsonSub = jsonChild.getJSONArray("child_categories");
                        int lengthJsonArrSub = jsonSub.length();
                        ArrayList<ExpandableClass> secondarr=new ArrayList<>();
                        ArrayList<ExpandableAllData> secondnode = new ArrayList<>();
                        secondnode.clear();
                        secondarr.clear();
                        for (int j = 0; j < lengthJsonArrSub; j++) {

                            JSONObject jsonChildSub = jsonSub.getJSONObject(j);
                            String subcatID = jsonChildSub.getString("id");
                            String subcatMainID = jsonChildSub.getString("parent_id");
                            subcatval = jsonChildSub.getString("name");


                            System.out.println("detailsmap"+subcatID+"  "+subcatval+"  "+String.valueOf(i)+"   "+String.valueOf(j));
                            secondarr.add(new ExpandableClass(subcatID,subcatval,submain_id));
                            SubcategoryVal.add(subcatval);
                            SubcategoryID.add(subcatID);
                            List<String> SubcategoryVal2 = new ArrayList<String>();
                            List<String> SubcategoryID2 = new ArrayList<String>();

                            JSONArray jsonSub2 = jsonChildSub.getJSONArray("child1_categories");
                            int lengthJsonArrSub2 = jsonSub2.length();
                            ArrayList<ExpandableCollectiveClass> thirdmain=new ArrayList<>();
                            ArrayList<ExpandableClass> thirdarr=new ArrayList<>();
                            thirdmain.clear();
                            thirdarr.clear();
                            if (lengthJsonArr>0) {
                                for (int k = 0; k < lengthJsonArrSub2; k++) {
                                    JSONObject jsonChildSub2 = jsonSub2.getJSONObject(k);
                                    String subcatID2 = jsonChildSub2.getString("id");
                                    subcatval2 = jsonChildSub2.getString("name");
                                    subcatmain_id2 = jsonChildSub2.getString("parent_id");


                                    ExpandableClass obj3=new ExpandableClass(subcatID2,subcatval2,subcatmain_id2);
                                    thirdarr.add(obj3);
                                    SubcategoryVal2.add(subcatval2);
                                    SubcategoryID2.add(subcatID2);
                                    System.out.println("alldatareceivedasfollows  " + subcatID2 + "  " + subcatval2);
                                }
                                thirdmain.add(new ExpandableCollectiveClass(subcatID,thirdarr));
                                System.out.println("herestarts"+subcatval);
                                listDataChild2.put(subcatval, SubcategoryVal2);
                                listDataChild_ID2.put(subcatval, SubcategoryID2);
                                System.out.println("subvaluelistprintout"+SubcategoryVal2.toString());
                                System.out.println("subidslistprintout"+SubcategoryID2.toString());
                            }
                            secondnode.add(new ExpandableAllData(thirdmain,subcatID,subcatMainID));
                        }
                        ExpandableCollectiveClass obj=new ExpandableCollectiveClass(dist_id,secondarr);
                        secondmain.add(obj);
                        alldatacollection.add(new ExpandableAllDataCollection(dist_id,secondnode));
                        listDataChild.put(listDataHeader.get(i), SubcategoryVal);
                        listDataChild_ID.put(listDataHeader.get(i), SubcategoryID);

                        ThreeLevelListAdapter threeLevelListAdapterAdapter = new ThreeLevelListAdapter(ShopByCategory.this, parentarr, secondmain, alldatacollection);
                        expandableListView.setAdapter(threeLevelListAdapterAdapter );
                        listAdapter = new ExpandableListAdapt(ShopByCategory.this, listDataHeader, listDataChild,listDataChild2);
//                        expListView.setAdapter(listAdapter);

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

    public class ParentLevel extends BaseExpandableListAdapter {


        @Override

        public Object getChild(int arg0, int arg1) {

            return arg1;

        }



        @Override

        public long getChildId(int groupPosition, int childPosition) {

            return childPosition;

        }


        @Override

        public View getChildView(int groupPosition, int childPosition,

                                 boolean isLastChild, View convertView, ViewGroup parent) {

            CustExpListview SecondLevelexplv = new CustExpListview(ShopByCategory.this);

//            SecondLevelexplv.setAdapter(new SecondLevelAdapter());

            SecondLevelexplv.setGroupIndicator(null);

            return SecondLevelexplv;

        }


        @Override

        public int getChildrenCount(int groupPosition) {

            return 3;

        }


        @Override

        public Object getGroup(int groupPosition) {

            return groupPosition;

        }


        @Override

        public int getGroupCount() {

            return 5;

        }

        @Override

        public long getGroupId(int groupPosition) {

            return groupPosition;

        }

        @Override

        public View getGroupView(int groupPosition, boolean isExpanded,

                                 View convertView, ViewGroup parent) {

            TextView tv = new TextView(ShopByCategory.this);

            tv.setText("->FirstLevel");

            tv.setTextColor(Color.BLACK);

            tv.setTextSize(20);

            tv.setBackgroundColor(Color.BLUE);

            tv.setPadding(10, 7, 7, 7);



            return tv;

        }

        @Override

        public boolean hasStableIds() {

            return true;

        }

        @Override

        public boolean isChildSelectable(int groupPosition, int childPosition) {

            return true;

        }

    }

    public class CustExpListview extends ExpandableListView {



        int intGroupPosition, intChildPosition, intGroupid;



        public CustExpListview(Context context) {

            super(context);

        }



        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(960,

                    MeasureSpec.AT_MOST);

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(600,

                    MeasureSpec.AT_MOST);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }

    }
}
