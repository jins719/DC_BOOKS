package app.com.dc_books;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Filter extends AppCompatActivity {

    Typeface custom_bold,custom_normal;
    TextView tv_apply,tv_clear_all,tv_filter;
    ImageButton ib_close;
    RecyclerView rv_filter_left,rv_filter_right;
    filterLeftAdapter filterLftAdpater;
    filterRightAdapter filterRghtAdpater;
    ArrayList<filterLeftItems> test=new ArrayList<>();
    ArrayList<filterRightItems> test1=new ArrayList<>();
    LinearLayoutManager linearLayoutManagerLft,linearLayoutManagerRght;
    static String selectedId = "none";
    static ArrayList<rlValueSet> relativeValue=new ArrayList<>();
    static String currentSelected = "none";
    ArrayList<ArrayList<filterRightItems>> allOptionList=new ArrayList<>();
    ArrayList<allValueSet> alltest=new ArrayList<>();
    ArrayList<String> allonetest=new ArrayList<>();
    ArrayList<filterRightItems> righttest=new ArrayList<>();
    static ArrayList<ArrayList<selectedItems>> selectedItemList=new ArrayList<>();
    ArrayList<selectedItems> selectedInnerList=new ArrayList<>();
    ArrayList<selectedItems> testInnerArray=new ArrayList<>();
    CheckBox testInnerElement;


    String fetch_filter_url ;
    String fetch_brand_url;

    Map<String, String> FetchFilterParams = new HashMap<>();
    Map<String, String> FetchBrandParams = new HashMap<>();
    ArrayList<filterLeftItems> filterLeftAttributes=new ArrayList<>();
    ArrayList<filterRightItems> filterRightAttributes=new ArrayList<>();
    ArrayList<ArrayList<filterRightItems>> mainRightAttributeArray=new ArrayList<>();
    ArrayList<ArrayList<filterSelectionTracking>> mainRightSelectionArray=new ArrayList<>();
    ArrayList<allSelectedIds> allSelectedIdList=new ArrayList<>();
    CheckBox cb_track;
    JSONObject brandResponse;
    List<String> allIds;
    List<String> allAttributeIds;
    List<String> allBrandIds;
    int NETCONNECTION;
    String cat_id;
    String appkey="TWF5dXJpc2VjdXJpdHlrZXkyMDE4";
    String appsecurity="TWF5dXJpc2VjdXJpdHljaGVjazIwMTg=";
    String noattristatus,allidcheck;

    public ProgressDialog pDialog;
    Handler pDialoghandler = new Handler();
    String selectlang;
    public static final String mp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //For activity
        SharedPreferences s = getSharedPreferences(Filter.mp, 0);

        fetch_filter_url ="https://dcbookstore.tk/api/giftandtoys/attributefilter";
        //fetch_brand_url=ip_head+"ckadministrator/services/AppBrandfiltersearch";

        Bundle extras = getIntent().getExtras();
        cat_id="0";
        if (extras != null) {
            cat_id = extras.getString("CatID");
        }

        isNetworkConnected();
        if(NETCONNECTION==1)
        {
            FetchBrandParams.put("category_id",cat_id);
//            FetchBrandData();
        }
        else
        {
//            String message;
//            message=getString(R.string.nointernet);
//            salert.Dialog(
//                    message,
//                    false,
//                    Filter.this
//            );
        }

        isNetworkConnected();
        if(NETCONNECTION==1)
        {

            FetchFilterParams.put("appkey",MainActivity.appkey);
            FetchFilterParams.put("appsecurity",MainActivity.appsecurity);
            FetchFilterParams.put("category_id",cat_id);
            FetchFilterData();
        }
        else
        {
            new SweetAlertDialog(Filter.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Check your internet connectivity and try again")
                    .show();
        }

        custom_normal = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");
        custom_bold = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Bold.ttf");
        tv_apply = findViewById(R.id.tv_apply_label);
        tv_clear_all = findViewById(R.id.tv_clear_all);
        tv_filter = findViewById(R.id.tv_filter_label);
        ib_close = findViewById(R.id.ib_close);
        rv_filter_left = findViewById(R.id.rv_filter_left);
        rv_filter_right = findViewById(R.id.rv_filter_right);
        tv_clear_all.setTypeface(custom_bold);
        tv_apply.setTypeface(custom_bold);
        tv_filter.setTypeface(custom_bold);

         tv_apply.setText("APPLY");
         tv_clear_all.setText("CLEAR ALL");
         tv_filter.setText("FILTER");



        relativeValue.add(new rlValueSet("0", null));
        relativeValue.add(new rlValueSet("0", null));

//        for(int l=0; l<=255; l++){
//            String txt="MATERIAL "+String.valueOf(l);
//            test.add(new filterLeftItems(String.valueOf(l),txt));
//            selectedItemList.add(selectedInnerList);
//        }

//        for(int j=0; j<=255; j++){
//            if(righttest!=null) {
//                righttest.clear();
//            }
//            for(int k=0; k<=30; k++){
//                String txt="MATERIAL "+String.valueOf(j)+" item"+String.valueOf(k);
//                righttest.add(new filterRightItems(String.valueOf(k),"8",txt));
//            }
//            allOptionList.add(new ArrayList(righttest));
//        }

//        filterRightItems obj1=new filterRightItems("5","5","MATERIAL");
//        for(int l=0; l<=15; l++){
//            test1.add(obj1);
//        }

//        testInnerArray.add(new selectedItems("5",testInnerElement));
//        testInnerArray.add(new selectedItems("6",testInnerElement));
//        testInnerArray.add(new selectedItems("7",testInnerElement));
//        testInnerArray.add(new selectedItems("8",testInnerElement));
//        testInnerArray.add(new selectedItems("9",testInnerElement));
//        testInnerArray.add(new selectedItems("10",testInnerElement));
//        testInnerArray.add(new selectedItems("11",testInnerElement));
//        testInnerArray.add(new selectedItems("12",testInnerElement));

    }
    public void clear(View view){
        selectedId="none";
        currentSelected= "none";
        mainRightSelectionArray.clear();
        mainRightSelectionArray.clear();
        mainRightAttributeArray.clear();
        filterLeftAttributes.clear();
        allSelectedIdList.clear();
        FetchFilterData();
    }
    public void apply(View view){
        if(allSelectedIdList.isEmpty()){
            finish();
            selectedId="none";
            currentSelected="none";
            overridePendingTransition(R.anim.hold, R.anim.slide_out);
        }else {
            sumUpSelectedIds();
            String allids = stringMaker(allIds);
            String allattributes = stringMaker(allAttributeIds);
            String allbrandid = stringMaker(allBrandIds);
            System.out.println("jkfjaskjfdkjakfj"+allids+"   "+allattributes+"   "+allbrandid);
            if(allids.equals("") && allattributes.equals("") && allbrandid.equals("")){
                finish();
                selectedId="none";
                currentSelected="none";
                overridePendingTransition(R.anim.hold, R.anim.slide_out);
            }else {
                selectedId = "none";
                currentSelected = "none";
                Intent in = new Intent(Filter.this, ProductListing.class);
                in.putExtra("attrid", allattributes);
                in.putExtra("attrivalue", allids);
                in.putExtra("brandid", allbrandid);
                in.putExtra("Identifier", "10");
                in.putExtra("CatID", cat_id);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.hold, R.anim.slide_out);
                System.out.println("allidscollection" + allids);
                System.out.println("allattributesselected" + allattributes);
            }
        }
    }
    public void close(View view){
        finish();
        selectedId="none";
        currentSelected="none";
        overridePendingTransition(R.anim.hold, R.anim.slide_out);
    }

    public class filterLeftAdapter extends RecyclerView.Adapter<filterLeftAdapter.ViewHolder> {

        private ArrayList<filterLeftItems> values;
        private ArrayList<filterRightItems> loadValues;
        private Integer selectedPosition=0;
        Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        //        Typeface custom_semi_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item,
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView tvFilterItem;
            RelativeLayout rlFilterItemLayout;

            //            public TextView txtFooter;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                tvFilterItem = v.findViewById(R.id.tv_filter_item);
                rlFilterItemLayout = v.findViewById(R.id.rl_filter_recycler_item);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent in =new Intent(Filter.this,ProductDetails.class);
//                        in.putExtra("ProdID",values.get(getAdapterPosition()).getProdId());
//                        startActivity(in);
                    }
                });
            }

        }

        public void add(int position, filterLeftItems item) {
            values.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            values.remove(position);
            notifyItemRemoved(position);
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public filterLeftAdapter(ArrayList<filterLeftItems> myDataset) {
            values = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.filter_recycler_left_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            String filterItems = values.get(position).getAttributeName();
            if(selectedId.equals(String.valueOf(position))){
                relativeValue.get(0).component.setBackgroundColor(Color.parseColor("#f2f2f2"));
            }
//            if(selectedId.equals(String.valueOf(position))){
//                holder.rlFilterItemLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
//            }
            else {
                holder.rlFilterItemLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                if (selectedPosition == position){
                    holder.rlFilterItemLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
                    filterRghtAdpater=new filterRightAdapter(mainRightAttributeArray.get(position));
                    rv_filter_right.setAdapter(filterRghtAdpater);
                    selectedId=String.valueOf(position);
                    relativeValue.set(0, new rlValueSet("1", holder.rlFilterItemLayout));
                }
            }
            holder.tvFilterItem.setText(filterItems);
            holder.tvFilterItem.setTypeface(custom_bold);


            holder.rlFilterItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    filterRghtAdpater=new filterRightAdapter(mainRightAttributeArray.get(position));
                    rv_filter_right.setAdapter(filterRghtAdpater);

//                    if(selectedPosition == position){
//                        holder.rlFilterItemLayout.setBackgroundColor(Color.parseColor("#81ca56"));
//                    }

                    if (loadValues != null){
                        loadValues.clear();
//                        filterRghtAdpater.notifyDataSetChanged();
                    }
                    if (relativeValue.get(0).status == "0") {
                        relativeValue.set(0, new rlValueSet("1", holder.rlFilterItemLayout));
                    } else {
                        relativeValue.set(1, new rlValueSet(relativeValue.get(0).status, relativeValue.get(0).component));
                        relativeValue.get(1).component.setBackgroundColor(Color.parseColor("#ffffff"));
                        relativeValue.set(0, new rlValueSet("1", holder.rlFilterItemLayout));
                    }
                    currentSelected=String.valueOf(position);
                    selectedId = String.valueOf(position);

                    holder.rlFilterItemLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
//                    loadValues = new ArrayList<>(allOptionList.get(position));
//                    filterRghtAdpater=new filterRightAdapter(loadValues);
//                    rv_filter_right.setAdapter(filterRghtAdpater);
//                        selectedArr[0]=holder.rlFilterItemLayout;
//                        holder.rlFilterItemLayout.setBackgroundColor(Color.parseColor("#81ca56"));
//                    }else{
//                        if(selectedArr[1]!=null && selectedArr[0] != selectedArr[1]){
//                            selectedArr[1].setBackgroundColor(Color.parseColor("#81ca56"));
//                        }else{
//                            selectedArr[0].setBackgroundColor(Color.parseColor("#f2f2f2"));
//                        }
//                        selectedArr[1]=selectedArr[0];
//                        selectedArr[0]= holder.rlFilterItemLayout;
//                    }
                }
            });

//            holder.txtFooter.setText("Footer: " + firstname);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return values.size();
        }

    }

    public class filterRightAdapter extends RecyclerView.Adapter<filterRightAdapter.ViewHolder> {

        private ArrayList<filterRightItems> values;
        Typeface custom_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        //        Typeface custom_semi_bold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item,
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            CheckBox cbFilterItem,cbFilterColorItem;
            TextView tvFilterItem;
            RelativeLayout rlFilterItemLayout;

            //            public TextView txtFooter;
            public View layout;

            public ViewHolder(View v) {
                super(v);
                layout = v;
                tvFilterItem = v.findViewById(R.id.tv_filter_item);
                rlFilterItemLayout = v.findViewById(R.id.rl_filter_recycler_item);
                cbFilterItem = v.findViewById(R.id.cb_filterItem);
                cbFilterColorItem= v.findViewById(R.id.cb_filterColorItem);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent in =new Intent(Filter.this,ProductDetails.class);
//                        in.putExtra("ProdID",values.get(getAdapterPosition()).getProdId());
//                        startActivity(in);
                    }
                });
            }
        }

        public void add(int position, filterRightItems item) {
            values.add(position, item);
            notifyItemInserted(position);
        }

        public void remove(int position) {
            values.remove(position);
            notifyItemRemoved(position);
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public filterRightAdapter(ArrayList<filterRightItems> myDataset) {
            values = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.filter_recycler_right_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            String filterItems = values.get(position).getValue();
            Boolean check = checkHexCode(values.get(position).getValue());
            if ( check == true){
                holder.cbFilterColorItem.setVisibility(View.VISIBLE);
                holder.cbFilterItem.setVisibility(View.GONE);
                setCheckBoxColor(holder.cbFilterColorItem,values.get(position).getValue());
                if(!(currentSelected.equals("none"))) {
                    if (mainRightSelectionArray.get(Integer.valueOf(currentSelected)).get(position).getStatus()) {
                        holder.cbFilterColorItem.setChecked(true);
                    }
                }
                holder.cbFilterItem.setText(filterItems);
                System.out.println("InitiateTestSequence"+String.valueOf(position));
            }else {
                if(!(currentSelected.equals("none"))) {
                    if (mainRightSelectionArray.get(Integer.valueOf(currentSelected)).get(position).getStatus()) {
                        holder.cbFilterItem.setChecked(true);
                    }
                }
                holder.cbFilterItem.setText(filterItems);
                holder.cbFilterItem.setTypeface(custom_bold);
                System.out.println("InitiateTestSequence"+String.valueOf(position));
            }
            holder.cbFilterColorItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCheckBoxColor(holder.cbFilterColorItem,values.get(position).getValue());
                    if (holder.cbFilterColorItem.isChecked()){
                        allSelectedIdList.add(new allSelectedIds(values.get(position).getId(),values.get(position).getAttribute_id(),true));
                        if (allSelectedIdList.size()==0){
                            addSelectionTracker(currentSelected,String.valueOf(position),"0");
                        }else if(allSelectedIdList.size()==0){
                            addSelectionTracker(currentSelected,String.valueOf(position),"1");
                        }else{
                            addSelectionTracker(currentSelected,String.valueOf(position),String.valueOf(allSelectedIdList.size()-1));
                        }
                        System.out.println("sizeChain"+allSelectedIdList.size());
                    }else {
                        if(currentSelected.equals("none")){
                            allSelectedIdList.get(Integer.valueOf(mainRightSelectionArray.get(0).get(position).getAllSelectedLink())).setStatus(false);
                        }else {
                            allSelectedIdList.get(Integer.valueOf(mainRightSelectionArray.get(Integer.valueOf(currentSelected)).get(position).getAllSelectedLink())).setStatus(false);
                        }
                        removeSelectionTracker(currentSelected,String.valueOf(position));
                    }
                    holder.rlFilterItemLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
                }
            });
            holder.cbFilterItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.cbFilterItem.isChecked()){
                        allSelectedIdList.add(new allSelectedIds(values.get(position).getId(),values.get(position).getAttribute_id(),true));
                        if (allSelectedIdList.size()==0){
                            addSelectionTracker(currentSelected,String.valueOf(position),"0");
                        }else if(allSelectedIdList.size()==0){
                            addSelectionTracker(currentSelected,String.valueOf(position),"1");
                        }else{
                            addSelectionTracker(currentSelected,String.valueOf(position),String.valueOf(allSelectedIdList.size()-1));
                        }
                        System.out.println("sizeChain"+allSelectedIdList.size());
                    }else {
                        if(currentSelected.equals("none")){
                            allSelectedIdList.get(Integer.valueOf(mainRightSelectionArray.get(0).get(position).getAllSelectedLink())).setStatus(false);
                        }else {
                            allSelectedIdList.get(Integer.valueOf(mainRightSelectionArray.get(Integer.valueOf(currentSelected)).get(position).getAllSelectedLink())).setStatus(false);
                        }
                        removeSelectionTracker(currentSelected,String.valueOf(position));
                    }
                    holder.rlFilterItemLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
//                    selectedItemList.get(Integer.valueOf(currentSelected)).get(1).setSelected("1");
                }
            });
//            holder.tvFilterItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    holder.rlFilterItemLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
//                }
//            });

//
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return values.size();
        }

    }

    public void FetchBrandData(){

//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(true);
//        pDialog.show();
        System.out.println("searchfetchparams"+FetchFilterParams.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                fetch_brand_url, new JSONObject(FetchBrandParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Search brand Responce---> "+response.toString());
                        try {
                            brandResponse = new JSONObject(String.valueOf(response));
                        } catch (JSONException e) {
                            Log.d("JSONExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();
                            new SweetAlertDialog(Filter.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Something went wrong")
                                    .setContentText("Please try again later")
                                    .show();


                        }
                        catch (NullPointerException e)
                        {
                            Log.d("NullExceptionLogin",e.toString());
                            e.printStackTrace();
                            new SweetAlertDialog(Filter.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Something went wrong")
                                    .setContentText("Please try again later")
                                    .show();
//                            pDialog.dismiss();

                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();

                System.out.println("Search Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    public void FetchFilterData(){

//        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(true);
//        pDialog.show();
        showProgress();
        System.out.println("searchfetchparams"+FetchFilterParams.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                fetch_filter_url, new JSONObject(FetchFilterParams),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        cancleProgress();
                        String leftarraystatus="no",rightarraystatus="no";
                        System.out.println("'---> "+response.toString());
                        JSONArray leftArray=null;
                        JSONArray rightArray=null;
                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            if (jsonResponse.has("attributesleft")) {
                                leftarraystatus="yes";
                                leftArray = jsonResponse.getJSONArray("attributesleft");
                            }
                            if (jsonResponse.has("attributes")) {
                                rightarraystatus="yes";
                                rightArray = jsonResponse.getJSONArray("attributes");
                            }
                            if (rightArray.length()<1 && (leftarraystatus.equals("no") || rightarraystatus.equals("no"))){
                                cancleProgress();
                                new SweetAlertDialog(Filter.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No filter attributes for this category.")
                                        .show();
                            }else {
                                System.out.println("reachedtilhere"+leftArray.toString()+" "+rightArray.toString());
                                for (int i = 0; i < leftArray.length(); i++) {
                                    String attribute_id = leftArray.getJSONObject(i).getString("id");
                                    String attributename = leftArray.getJSONObject(i).getString("name");
                                    System.out.println("enterinnerarray"+attribute_id+" "+attributename);
                                    filterLeftAttributes.add(new filterLeftItems(attribute_id, attributename));
                                    mainRightAttributeArray.add(new ArrayList<filterRightItems>());
                                    mainRightSelectionArray.add(new ArrayList<filterSelectionTracking>());
                                    for (int j = 0; j < rightArray.length(); j++) {
//                                        String counter = rightArray.getJSONObject(1).getString("attribute_id");
                                        if (rightArray.getJSONObject(j).getString("id").equals(attribute_id)) {
                                            mainRightAttributeArray.get(i).add(new filterRightItems(rightArray.getJSONObject(j).getString("id"), rightArray.getJSONObject(j).getString("attributevalue_id"), rightArray.getJSONObject(j).getString("value")));
                                            mainRightSelectionArray.get(i).add(new filterSelectionTracking(rightArray.getJSONObject(j).getString("id"), false, "none"));
                                        }
                                    }
                                }
                            }

//                            System.out.println("haisfafa"+brandResponse.toString());
//                            leftArray = brandResponse.getJSONArray("Brands");
//                            rightArray = brandResponse.getJSONArray("Brand22");
//
//                            System.out.println("leftarrayparsed"+leftArray.toString());
//                            System.out.println("rightarrayparsed"+rightArray.toString());
//
//                            if (rightArray.length()<1){
//                                if (noattristatus.equals("none")) {
//                                    String message;
//                                    message="This category has no filtering option.";
//                                    salert.Dialog(
//                                            message,
//                                            false,
//                                            Filter.this
//                                    );
//                                }
//                            }else {
//
//                                for (int i = 0; i < leftArray.length(); i++) {
//                                    String attribute_id = leftArray.getJSONObject(i).getString("bid");
//                                    String attributename = leftArray.getJSONObject(i).getString("brand");
//                                    filterLeftAttributes.add(new filterLeftItems(attribute_id, attributename));
//                                    mainRightAttributeArray.add(new ArrayList<filterRightItems>());
//                                    mainRightSelectionArray.add(new ArrayList<filterSelectionTracking>());
//                                    int maincount=mainRightAttributeArray.size();
//                                    if (maincount==1){
//                                        maincount=0;
//                                    }else {
//                                        maincount=maincount-1;
//                                    }
//                                    for (int j = 0; j < rightArray.length(); j++) {
////                                    String counter= rightArray.getJSONObject(1).getString("brandid");
//                                        mainRightAttributeArray.get(maincount).add(new filterRightItems(rightArray.getJSONObject(j).getString("uniqueId"), rightArray.getJSONObject(j).getString("brandid"), rightArray.getJSONObject(j).getString("brandname")));
//                                        mainRightSelectionArray.get(maincount).add(new filterSelectionTracking(rightArray.getJSONObject(j).getString("uniqueId"), false, "none"));
//                                    }
//                                }
//                                arraydismantler(filterLeftAttributes);
//                            }

                            linearLayoutManagerLft = new LinearLayoutManager(Filter.this);
                            rv_filter_left.setLayoutManager(linearLayoutManagerLft);

                            linearLayoutManagerRght = new LinearLayoutManager(Filter.this);
                            rv_filter_right.setLayoutManager(linearLayoutManagerRght);

                            filterLftAdpater=new filterLeftAdapter(filterLeftAttributes);
                            rv_filter_left.setAdapter(filterLftAdpater);

//                            pDialog.dismiss();
                            System.out.println("LeftArrayItems response "+leftArray.toString());
                            System.out.println("RightArrayItems response "+rightArray.toString());
//                            String prodCount= String.valueOf(resultsArray.length())+" Products";
//                            tvProductCount.setText(prodCount);
                            //Test
//                            for (int i=0; i<leftArray.length(); i++){
//                                String prodId=leftArray.getJSONObject(i).getString("id");
//                                String mainImg=leftArray.getJSONObject(i).getString("image");
//                                String prodTitle=leftArray.getJSONObject(i).getString("title");
//                                String prodActPric=leftArray.getJSONObject(i).getString("actualprice");
//                                String prodOffrPric=leftArray.getJSONObject(i).getString("sellingprice");
//                                String wishStatus=leftArray.getJSONObject(i).getString("wishliststatus");
//                                String badge=leftArray.getJSONObject(i).getString("badgename");
//                                ProductListing.ProductItems obj=new ProductListing.ProductItems(
//                                        prodId,
//                                        mainImg,
//                                        prodTitle,
//                                        prodActPric,
//                                        prodOffrPric,
//                                        wishStatus,
//                                        badge
//                                );
//                                ProductArray.add(obj);
//                            }
//                            Log.d("dsjfkaads",ProductArray.toString());
//                            productAdapter=new ProductListing.MyAdapter(ProductArray);
//                            rv_product_listing.setAdapter(productAdapter);

//                            String Status=jsonResponse.getString("status");
//                            String Message=jsonResponse.getString("result");
//
//                            if(Status.equals("true"))
//                            {

//                                et_login_email.getText().clear();
//                                et_login_pass.getText().clear();

//                                edit.putString("Username",jsonResponse.getString("firstname"));
//                                edit.putString("User_id",jsonResponse.getString("userId"));
//                                edit.putString("Login_Status","success");
//                                edit.putString("Firsttime","YES");
//                                edit.commit();
//
//                                Intent in=new Intent(ProductListing.this,MainActivity.class);
//                                startActivity(in);
//                                finish();
//
//                            }
//                            else
//                            {
//                                new SweetAlertDialog(ProductListing.this, SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("Oops...")
//                                        .setContentText(Message)
//                                        .show();
//                            }

                        } catch (JSONException e) {
                            Log.d("JSONExceptionLogin",e.toString());
                            e.printStackTrace();
//
                            cancleProgress();

                            new SweetAlertDialog(Filter.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("No Attributes ")
                                    .setContentText("No attributes available in this product.")
                                    .show();
//
                        }
                        catch (NullPointerException e)
                        {
                            Log.d("NullExceptionLogin",e.toString());
                            e.printStackTrace();
//                            pDialog.dismiss();
                            cancleProgress();

//                            String message;
//                            message="Something went wrong. Please try again later";
//                            salert.Dialog(
//                                    message,
//                                    false,
//                                    Filter.this
//                            );

                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                cancleProgress();
                System.out.println("Search Responce---> "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }

    @Override
    public void onBackPressed()
    {
        finish();
        selectedId="none";
        currentSelected="none";
        overridePendingTransition(R.anim.hold, R.anim.slide_out);
    }

    public class filterLeftItems {
        String attribute_id;
        String attributename;
        filterLeftItems(String attribute_id, String attributename){
            this.attribute_id=attribute_id;
            this.attributename=attributename;
        }

        public String getAttributeName() {
            return attributename;
        }

        public String getAttributeId() {
            return attribute_id;
        }
    }
    public class filterRightItems {
        String id;
        String attribute_id;
        String value;
        filterRightItems(String id,String attribute_id, String value){
            this.id=id;
            this.attribute_id=attribute_id;
            this.value=value;
        }

        public String getValue() {
            return value;
        }

        public String getAttribute_id() {
            return attribute_id;
        }

        public String getId() {
            return id;
        }
    }

    public class rlValueSet{
        String status;
        RelativeLayout component;

        rlValueSet(String status,RelativeLayout component){
            this.status=status;
            this.component=component;
        }

        public String getStatus() {
            return status;
        }

        public RelativeLayout getComponent() {
            return component;
        }
    }
    public class allValueSet{
        String status;
        RelativeLayout component;

        allValueSet(String status,RelativeLayout component){
            this.status=status;
            this.component=component;
        }

        public String getStatus() {
            return status;
        }

        public RelativeLayout getComponent() {
            return component;
        }
    }

    public class selectedItems{
        String selected;
        CheckBox selectedHolder;

        public selectedItems(String selected,CheckBox selectedHolder) {
            this.selected = selected;
            this.selectedHolder=selectedHolder;
        }

        public String getSelected() {
            return selected;
        }

        public CheckBox getSelectedHolder() {
            return selectedHolder;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }

        public void setSelectedHolder(CheckBox selectedHolder) {
            this.selectedHolder = selectedHolder;
        }
    }

    public class filterSelectionTracking{
        String id;
        Boolean status;
        String allSelectedLink;

        public filterSelectionTracking(String id, Boolean status, String allSelectedLink) {
            this.id = id;
            this.status = status;
            this.allSelectedLink = allSelectedLink;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getAllSelectedLink() {
            return allSelectedLink;
        }

        public void setAllSelectedLink(String allSelectedLink) {
            this.allSelectedLink = allSelectedLink;
        }
    }

    public class allSelectedIds{
        String id;
        String attribute_id;
        Boolean status;

        public allSelectedIds(String id, String attribute_id, Boolean status) {
            this.id = id;
            this.attribute_id = attribute_id;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getStatus() {
            return status;
        }

        public String getAttribute_id() {
            return attribute_id;
        }

        public void setAttribute_id(String attribute_id) {
            this.attribute_id = attribute_id;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }
    }

    public void addSelectionTracker(String arrpos,String inarrpos,String link) {
        if (arrpos.equals("none")) {
            mainRightSelectionArray.get(0).get(Integer.valueOf(inarrpos)).setStatus(true);
            mainRightSelectionArray.get(0).get(Integer.valueOf(inarrpos)).setAllSelectedLink(link);
        } else {
            mainRightSelectionArray.get(Integer.valueOf(arrpos)).get(Integer.valueOf(inarrpos)).setStatus(true);
            mainRightSelectionArray.get(Integer.valueOf(arrpos)).get(Integer.valueOf(inarrpos)).setAllSelectedLink(link);
        }
    }
    public void removeSelectionTracker(String arrpos,String inarrpos){
        if(arrpos.equals("none")){
            mainRightSelectionArray.get(0).get(Integer.valueOf(inarrpos)).setStatus(false);
        }else{
            mainRightSelectionArray.get(Integer.valueOf(arrpos)).get(Integer.valueOf(inarrpos)).setStatus(false);
        }
    }
    public void sumUpSelectedIds(){
        allIds = new LinkedList();
        allAttributeIds = new LinkedList();
        allBrandIds = new LinkedList();
        for (int i=0;i<allSelectedIdList.size();i++){
            if (allSelectedIdList.get(i).getStatus()){
                try {
                    System.out.println("getitall" + allSelectedIdList.get(i).getId().substring(0, 3));
                    allidcheck=allSelectedIdList.get(i).getId().substring(0, 3);
                }catch (StringIndexOutOfBoundsException e){
                    System.out.println("Exceptionhere"+e.toString());
                    allidcheck="none";
                }
                if(allidcheck.equals("001")){
                    System.out.println("printerids"+allSelectedIdList.get(i).getAttribute_id());
                    allBrandIds.add(allSelectedIdList.get(i).getAttribute_id());
                }else {
                    allIds.add(allSelectedIdList.get(i).getId());
                }
                if (!(allAttributeIds.contains(allSelectedIdList.get(i).getAttribute_id()))) {
                    if (allidcheck.equals("001")){
                        System.out.println("brandidyes" + allSelectedIdList.get(i).getAttribute_id());
//                            allAttributeIds.add("");
                    }else {
                        System.out.println("brandidno" + allSelectedIdList.get(i).getAttribute_id());
                        allAttributeIds.add(allSelectedIdList.get(i).getAttribute_id());
                    }
                    System.out.println("gettingbetterithink"+allSelectedIdList.get(i).getId());
                }
            }
        }
    }
    public String stringMaker(List<String> pass){
        StringBuilder csvBuilder = new StringBuilder();
        String SEPARATOR = ",";
        for(String city : pass){
            csvBuilder.append(city);
            csvBuilder.append(SEPARATOR);
        }
        String csv = csvBuilder.toString();
        try {
            csv = csv.substring(0, csv.length() - SEPARATOR.length());
        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return csv;
    }
    public void arraydismantler(ArrayList<filterLeftItems> gud){
        for (int i=0;i<gud.size();i++){
            System.out.println("printingstreak"+gud.get(i).getAttributeId());
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

    public void setCheckBoxColor(CheckBox cb,String selectedColor){
        LayerDrawable sd = (LayerDrawable) ContextCompat.getDrawable(Filter.this,R.drawable.unchecked);
        LayerDrawable ld = (LayerDrawable) ContextCompat.getDrawable(Filter.this,R.drawable.checked);
        GradientDrawable gradient = (GradientDrawable) ld
                .findDrawableByLayerId(R.id.stroke);
        GradientDrawable gradient2 = (GradientDrawable) sd
                .findDrawableByLayerId(R.id.bstruck);
        gradient2.setColor(Color.parseColor(selectedColor));
        gradient.setColor(Color.parseColor(selectedColor));
        StateListDrawable newStripTab = new StateListDrawable();
        newStripTab.addState(new int[] { android.R.attr.state_checked }, ld);
        newStripTab.addState(new int[0], ContextCompat.getDrawable(Filter.this,R.drawable.unchecked));
        cb.setButtonDrawable(newStripTab);
    }
    public boolean checkHexCode(String clr){
        Boolean status;
        try {
            Color.parseColor(clr);
            status=true;
            return status;
        } catch (IllegalArgumentException iae) {
            status=false;
            return status;
        }
    }
    public void showProgress()
    {
        pDialog = new ProgressDialog(Filter.this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    public void cancleProgress()
    {
        if(pDialog!=null)
            pDialoghandler.postDelayed(new Runnable() {
                public void run() {
                    pDialog.dismiss();
                }
            }, 2000);
    }
}
