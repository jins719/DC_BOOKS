package app.com.dc_books;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by JINS on 10/9/2017.
 */

public class Home extends Fragment {


    Typeface font,font2,font3;
    String Banner_Url="http://192.168.1.18:8080/dcbooks/api/homescreen/show_slider";
    String Category_Url="http://192.168.1.18:8080/dcbooks/api/homescreen/top_categories";
    String Newlaunches_Url="http://192.168.1.18:8080/dcbooks/api/homescreen/new_releases";
    String Topselling_Url="http://192.168.1.18:8080/dcbooks/api/homescreen/best_sellers";
    Map<String,String> HomeParams = new HashMap<String, String>();

    ArrayList<String> BannerID = new ArrayList<>();
    ArrayList<String> BannerImage = new ArrayList<>();
    ArrayList<String> BannerLink = new ArrayList<>();

    ArrayList<String> CategoryID = new ArrayList<>();
    ArrayList<String> CategoryName = new ArrayList<>();
    ArrayList<String> CategoryImage = new ArrayList<>();

    ArrayList<String> NewLaunchID = new ArrayList<>();
    ArrayList<String> NewLaunchName = new ArrayList<>();
    ArrayList<String> NewLaunchImage = new ArrayList<>();
    ArrayList<String> NewLaunchBadge= new ArrayList<>();

    ArrayList<String> TopSellID = new ArrayList<>();
    ArrayList<String> TopSellName = new ArrayList<>();
    ArrayList<String> TopSellImage = new ArrayList<>();
    ArrayList<String> TopSellBadge = new ArrayList<>();

    ViewPager viewPager;
    CircleIndicator indicator;
    TextView t_shopcategory,t_newlaunches,t_topselling;

    RecyclerView rcv_categoryList;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ArrayList<ItemData> arraylist = new ArrayList<ItemData>();
    MyAdapter mAdapter;


    ArrayList<ItemData1> NewLaunchesarraylist = new ArrayList<ItemData1>();
    MyAdapter1 mAdapter1;
    RecyclerView rcv_NewlauchList;

    ArrayList<ItemData2> Topselling_arraylist = new ArrayList<ItemData2>();
    MyAdapter2 mAdapter2;
    RecyclerView rcv_topselling;


    String appkey="aec2a0b15161ae445865b32bbefef972";
    String appsecurity="928e6af859edef918313aac98d5d48ee";

    RelativeLayout ril_shopcategory,ril_newlaunches,ril_topselling;
    int NETCONNECTION;

    CardView cardView1;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Montserrat-Bold.ttf");
        font3 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Montserrat-SemiBold.ttf");


        viewPager=view.findViewById(R.id.viewPager);
        indicator =view.findViewById(R.id.indicator);
        t_shopcategory=view.findViewById(R.id.textView2);
        t_newlaunches=view.findViewById(R.id.textView3);
        t_topselling=view.findViewById(R.id.textView4);
        rcv_categoryList=view.findViewById(R.id.recycleview);
        rcv_NewlauchList=view.findViewById(R.id.recycleview2);
        rcv_topselling=view.findViewById(R.id.recycleview3);
        ril_shopcategory=view.findViewById(R.id.relativeLayout3);
        ril_newlaunches=view.findViewById(R.id.relativeLayout4);
        ril_topselling=view.findViewById(R.id.relativeLayout5);
        cardView1=view.findViewById(R.id.cardview);

        cardView1.setVisibility(View.INVISIBLE);


        HomeParams.put("appkey",appkey);
        HomeParams.put("appsecurity",appsecurity);


        //recyclerViewLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerViewLayoutManager = new GridLayoutManager(getActivity(),3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rcv_categoryList.setLayoutManager(recyclerViewLayoutManager);

        t_shopcategory.setTypeface(font);
        t_newlaunches.setTypeface(font);
        t_topselling.setTypeface(font);

        float density = getResources().getDisplayMetrics().density;

        System.out.println("density  "+density);


        isNetworkConnected();

        if(NETCONNECTION==1) {
            Call_BannerImages();
            Call_Category();
            Call_NewLaunches();
            Call_TopSelling();

        }else
        {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("No internet")
                    .setContentText("Internet not available, Cross check your internet connectivity and try again")
                    .show();
        }


        return view;
    }
    // Webservice call banner
    public void Call_BannerImages()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Banner_Url, new JSONObject(HomeParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("HomeResponce---> "+response.toString());
                        pDialog.dismissWithAnimation();
                        cardView1.setVisibility(View.VISIBLE);

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("slider");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChild = jsonMain.getJSONObject(i);
                                String banner_id=jsonChild.getString("id");
                                String banner_image=jsonChild.getString("image");
                                String banner_link=jsonChild.getString("url");


                                BannerID.add(banner_id);
                                BannerImage.add("http://192.168.1.18:8080/dcbooks/"+banner_image);
                                BannerLink.add(banner_link);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }

                        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getActivity(),BannerImage);
                        viewPager.setAdapter(mCustomPagerAdapter);
                        indicator.setViewPager(viewPager);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                System.out.println("HomeResponceError Responce---> "+error.toString());
                // pDialog.dismiss();
                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Network error")
                        .show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<String> BannerImages = new ArrayList<String>();
        ImageView ImageView;

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

            Glide.with(getActivity())
                    .load(BannerImages.get(position)).into(ImageView);

            ImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                }
            });
            
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {
            container.removeView((RelativeLayout) obj);
        }
    }
   // Webservice call categories
    public void Call_Category()
    {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Category_Url, new JSONObject(HomeParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce---> "+response.toString());
                       /* pDialog.dismiss();*/

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("categories");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChild = jsonMain.getJSONObject(i);
                                String categoryid=jsonChild.getString("id");
                                String categoryname=jsonChild.getString("name");
                                String categoryimage=jsonChild.getString("image");

                                CategoryID.add(categoryid);
                                CategoryName.add(categoryname);
                                CategoryImage.add("http://192.168.1.18:8080/dcbooks/"+categoryimage);

                                ItemData itemsData = new ItemData(CategoryID.get(i), CategoryName.get(i),CategoryImage.get(i));
                                arraylist.add(itemsData);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }

                        rcv_categoryList.setLayoutManager(recyclerViewLayoutManager);
                        mAdapter = new MyAdapter((FragmentActivity) getActivity(), arraylist);
                        rcv_categoryList.setAdapter(mAdapter);

                        ril_shopcategory.setVisibility(View.VISIBLE);






                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    public class ItemData {

        String cat_id,cat_name,cat_image;
        public ItemData(String CatID,String CatName,String CatImage){

            this.cat_id = CatID;
            this.cat_name = CatName;
            this.cat_image = CatImage;
        }

        public String getcat_id()
        {
            return this.cat_id;
        }

        public String getcat_name()
        {
            return this.cat_name;
        }

        public String getcat_image()
        {
            return this.cat_image;
        }
    }
    
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<ItemData> Categorylist = null;
        ViewHolder viewHolder;
        Activity a;

        public MyAdapter(FragmentActivity activity, ArrayList<ItemData> ItemsData) {
            Categorylist = ItemsData;
            a = activity;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_adapter, null);
            viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            viewHolder.t_categoryname.setTypeface(font);

            viewHolder.t_categoryname.setText(Categorylist.get(position).getcat_name());
            Glide.with(getActivity())
                    .load(Categorylist.get(position).getcat_image()).into(viewHolder.img_categoryimage);

        }

        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView t_categoryname;
            public ImageView img_categoryimage;


            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                t_categoryname =itemLayoutView.findViewById(R.id.textView);
                img_categoryimage=itemLayoutView.findViewById(R.id.imageView5);

                itemLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in=new Intent(getActivity(),ProductListing.class);
                        in.putExtra("CatID",Categorylist.get(getAdapterPosition()).getcat_id());
                        in.putExtra("CatName",Categorylist.get(getAdapterPosition()).getcat_name());
                        in.putExtra("Identifier","1");
                        startActivity(in);

                    }
                });


            }
        }// Return the size of your itemsData (invoked by the layout manager)

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return Categorylist.size();
        }
    }


    // Webservice call new launches

    public void Call_NewLaunches()
    {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Newlaunches_Url, new JSONObject(HomeParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce---> "+response.toString());
                       /* pDialog.dismiss();*/

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("products");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChild = jsonMain.getJSONObject(i);
                                String newlaunchid=jsonChild.getString("id");
                               // String newlaunchname=jsonChild.getString("productname");
                                String newlaunchimage=jsonChild.getString("image");

                                NewLaunchID.add(newlaunchid);
                                NewLaunchName.add("");
                                NewLaunchImage.add("http://192.168.1.18:8080/dcbooks/"+newlaunchimage);
                                NewLaunchBadge.add("");

                                ItemData1 itemsData = new ItemData1(NewLaunchID.get(i), NewLaunchName.get(i),NewLaunchImage.get(i),NewLaunchBadge.get(i));
                                NewLaunchesarraylist.add(itemsData);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }

                        rcv_NewlauchList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        mAdapter1 = new MyAdapter1((FragmentActivity) getActivity(), NewLaunchesarraylist);
                        rcv_NewlauchList.setAdapter(mAdapter1);

                        ril_newlaunches.setVisibility(View.VISIBLE);





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    public class ItemData1 {

        String newlaunch_id,newlaunch_name,newlaunch_image,newlaunch_badge;
        public ItemData1(String NewlaunchID,String NewlaunchName,String NewlaunchImage,String Newlunch_badge){

            this.newlaunch_id = NewlaunchID;
            this.newlaunch_name = NewlaunchName;
            this.newlaunch_image = NewlaunchImage;
            this.newlaunch_badge=Newlunch_badge;
        }

        public String getnewlaunch_id()
        {
            return this.newlaunch_id;
        }

        public String getnewlaunch_name()
        {
            return this.newlaunch_name;
        }

        public String getnewlaunch_image()
        {
            return this.newlaunch_image;
        }
        public String getnewlaunch_badge()
        {
            return this.newlaunch_badge;
        }
    }

    public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.ViewHolder> {
        private ArrayList<ItemData1> Newlauncheslist = null;
        ViewHolder viewHolder;
        Activity a;

        public MyAdapter1(FragmentActivity activity, ArrayList<ItemData1> ItemsData) {
            Newlauncheslist = ItemsData;
            a = activity;
        }

        @Override
        public MyAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.newlaunches_adapter, null);
            viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            viewHolder.t_categoryname.setTypeface(font);

            viewHolder.t_categoryname.setText(Newlauncheslist.get(position).getnewlaunch_name());
            Glide.with(getActivity())
                    .load(Newlauncheslist.get(position).getnewlaunch_image()).into(viewHolder.img_categoryimage);

            Glide.with(getActivity())
                    .load(Newlauncheslist.get(position).getnewlaunch_badge()).into(viewHolder.img_badgeview);

        }

        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView t_categoryname;
            public ImageView img_categoryimage,img_badgeview;


            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                t_categoryname =itemLayoutView.findViewById(R.id.textView);
                img_categoryimage=itemLayoutView.findViewById(R.id.imageView5);
                img_badgeview=itemLayoutView.findViewById(R.id.imageView10);

                itemLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in=new Intent(getActivity(),ProductDetails.class);
                        in.putExtra("ProdID",Newlauncheslist.get(getAdapterPosition()).getnewlaunch_id());
                        startActivity(in);

                    }
                });


            }
        }// Return the size of your itemsData (invoked by the layout manager)

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return Newlauncheslist.size();
        }
    }


    //Call Top selling

    public void Call_TopSelling()
    {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Topselling_Url, new JSONObject(HomeParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Responce---> "+response.toString());
                       /* pDialog.dismiss();*/

                        try {
                            JSONObject jsonResponse = new JSONObject(String.valueOf(response));
                            JSONArray jsonMain = jsonResponse.getJSONArray("products");
                            int lengthJsonArr = jsonMain.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChild = jsonMain.getJSONObject(i);
                                String topsellid=jsonChild.getString("id");
                            //    String topsellname=jsonChild.getString("productname");
                                String topsellimage=jsonChild.getString("image");

                                TopSellID.add(topsellid);
                                TopSellName.add("");
                                TopSellImage.add("http://192.168.1.18:8080/dcbooks/"+topsellimage);
                                TopSellBadge.add("");

                                ItemData2 itemsData = new ItemData2(TopSellID.get(i), TopSellName.get(i),TopSellImage.get(i),TopSellBadge.get(i));
                                Topselling_arraylist.add(itemsData);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();

                        }

                        rcv_topselling.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        mAdapter2 = new MyAdapter2((FragmentActivity) getActivity(), Topselling_arraylist);
                        rcv_topselling.setAdapter(mAdapter2);

                        ril_topselling.setVisibility(View.VISIBLE);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  pDialog.dismiss();
                System.out.println("Error Responce---> "+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "jobj_req");
    }
    public class ItemData2 {

        String topsell_id,topsell_name,topsell_image,topsell_badge;
        public ItemData2(String TopSellID,String TopSellName,String TopSellImage,String TopsellBadge){

            this.topsell_id = TopSellID;
            this.topsell_name = TopSellName;
            this.topsell_image = TopSellImage;
            this.topsell_badge=TopsellBadge;
        }

        public String gettopsell_id()
        {
            return this.topsell_id;
        }

        public String gettopsell_name()
        {
            return this.topsell_name;
        }

        public String gettopsell_image()
        {
            return this.topsell_image;
        }

        public String gettopsell_badge()
        {
            return this.topsell_badge;
        }
    }

    public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
        private ArrayList<ItemData2> Topsellinglist = null;
        ViewHolder viewHolder;
        Activity a;

        public MyAdapter2(FragmentActivity activity, ArrayList<ItemData2> ItemsData) {
            Topsellinglist = ItemsData;
            a = activity;
        }

        @Override
        public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.newlaunches_adapter, null);
            viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            viewHolder.t_categoryname.setTypeface(font);

            viewHolder.t_categoryname.setText(Topsellinglist.get(position).gettopsell_name());
            Glide.with(getActivity())
                    .load(Topsellinglist.get(position).gettopsell_image()).into(viewHolder.img_categoryimage);

            Glide.with(getActivity())
                    .load(Topsellinglist.get(position).gettopsell_badge()).into(viewHolder.img_badgeview);

        }

        // inner class to hold a reference to each item of RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView t_categoryname;
            public ImageView img_categoryimage,img_badgeview;


            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                t_categoryname =itemLayoutView.findViewById(R.id.textView);
                img_categoryimage=itemLayoutView.findViewById(R.id.imageView5);
                img_badgeview=itemLayoutView.findViewById(R.id.imageView10);

                itemLayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(getActivity(),ProductDetails.class);
                        in.putExtra("ProdID",Topsellinglist.get(getAdapterPosition()).gettopsell_id());
                        startActivity(in);
                    }
                });


            }
        }// Return the size of your itemsData (invoked by the layout manager)

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return Topsellinglist.size();
        }
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
