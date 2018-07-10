package app.com.dc_books.Expand;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import app.com.dc_books.MainActivity;
import app.com.dc_books.ProductListing;
import app.com.dc_books.R;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by User on 12/27/2017.
 */

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {

    ArrayList<ExpandableClass> parentHead=new ArrayList<>();
    ArrayList<ExpandableCollectiveClass> secondHead=new ArrayList<>();
    ArrayList<ExpandableAllDataCollection> thirdHead=new ArrayList<>();
    ArrayList<ExpandableAllData> thirdmid=new ArrayList<>();
    String[] parentHeaders;
    List<String[]> secondLevel;
    private Context context;
    List<LinkedHashMap<String, String[]>> data;
    int NETCONNECTION;

    public ThreeLevelListAdapter(Context context, ArrayList<ExpandableClass> parentHead, ArrayList<ExpandableCollectiveClass> secondHead, ArrayList<ExpandableAllDataCollection> thirdHead) {
        this.context = context;

        this.parentHead = parentHead;

        this.secondHead = secondHead;

        this.thirdHead = thirdHead;
    }

    @Override
    public int getGroupCount() {
        return parentHead.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {


        // no idea why this code is working

        return 1;
    }

    public int simpleChildrenCount(int groupPosition){

        return secondHead.get(groupPosition).getCollection().size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupPosition;
    }

    @Override
    public Object getChild(int group, int child) {


        return child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.expand_row1, null);
        TextView text = (TextView) convertView.findViewById(R.id.childHeaderRow1);
        Typeface custom_semi_bold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-SemiBold.ttf");
        text.setTypeface(custom_semi_bold);
        ImageView arrowimg=convertView.findViewById(R.id.arrowimgRow1);
        text.setText(this.parentHead.get(groupPosition).getParent_value());
        if (isExpanded) {
            arrowimg.setImageResource(R.mipmap.arrow_down);
        } else {
            arrowimg.setImageResource(R.mipmap.arrow_right);
        }
        if (simpleChildrenCount(groupPosition)<=0) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isNetworkConnected();
                    if (NETCONNECTION == 1) {

                        if(parentHead.get(groupPosition).getMain_id().equals("1"))
                        {
                            MainActivity.Brand_Type=1;
                        }

                        Intent in=new Intent(context,ProductListing.class);
                        in.putExtra("CatName", parentHead.get(groupPosition).getParent_value());
                        in.putExtra("CatID", parentHead.get(groupPosition).getParent_id());
                        in.putExtra("Identifier","1");
                        context.startActivity(in);
                    }
                    else {

                    }
                }
            });
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);

        ArrayList<ExpandableClass> secondHeaders;
        ArrayList<ExpandableCollectiveClass> childHeaders=new ArrayList<>();
        secondHeaders=secondHead.get(groupPosition).getCollection();
//        String[] headers = secondLevel.get(groupPosition);


        List<String[]> childData = new ArrayList<>();
//        HashMap<String, String[]> secondLevelData=data.get(groupPosition);
        thirdmid=thirdHead.get(groupPosition).getAlldatanode();
        System.out.println("printingstreak "+String.valueOf(groupPosition)+"  "+String.valueOf(childPosition));
//        if(thirdmid.size()>0) {
//            childHeaders = thirdmid.get(childPosition).getMainnode();
//        }
//        for(String key : secondLevelData.keySet())
//        {
//            childData.add(secondLevelData.get(key));
//        }


        if(thirdmid.size()>0) {
            secondLevelELV.setAdapter(new SecondLevelAdapter(context, secondHeaders, thirdmid));
        }
        secondLevelELV.setGroupIndicator(null);

        secondLevelELV.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                System.out.println("secondmain "+String.valueOf(i));
                return false;
            }
        });

        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
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