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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.dc_books.MainActivity;
import app.com.dc_books.ProductListing;
import app.com.dc_books.R;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Johnett
 */

public class SecondLevelAdapter extends BaseExpandableListAdapter {

    private Context context;


//    List<String[]> data;

//    String[] headers;
    ArrayList<ExpandableClass> headers;
    ArrayList<ExpandableAllData> data;
    int NETCONNECTION;


    public SecondLevelAdapter(Context context, ArrayList<ExpandableClass> headers, ArrayList<ExpandableAllData> data) {
        this.context = context;
        this.data = data;
        this.headers = headers;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return headers.get(groupPosition).getParent_value();
    }

    @Override
    public int getGroupCount() {

        return headers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.expand_row2, null);
        TextView text = (TextView) convertView.findViewById(R.id.childHeaderRow2);
        Typeface custom_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
        text.setTypeface(custom_regular);
        ImageView arrowimg=convertView.findViewById(R.id.arrowimgRow2);
        String groupText = getGroup(groupPosition).toString();
        text.setText(groupText);

        if (isExpanded) {
            arrowimg.setImageResource(R.mipmap.arrow_down);
        } else {
            arrowimg.setImageResource(R.mipmap.arrow_right);
        }
        if(getChildrenCount(groupPosition)<=0) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isNetworkConnected();
                    if (NETCONNECTION == 1) {

                       if(data.get(groupPosition).getMain_id().equals("1"))
                       {

                               MainActivity.Brand_Type=1;
                       }
                       else
                       {
                           MainActivity.Brand_Type=2;
                       }
                       System.out.println("secondljklj"+data.get(groupPosition).getParent_id());
                       Intent in=new Intent(context,ProductListing.class);
                        in.putExtra("CatName", headers.get(groupPosition).getParent_value());
                        in.putExtra("CatID", data.get(groupPosition).getParent_id());
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
    public Object getChild(int groupPosition, int childPosition) {

//        String[] childData;
        ArrayList<ExpandableCollectiveClass> childData;

        childData = data.get(groupPosition).getMainnode();


        return childData.get(childPosition).getCollection().get(0).getParent_value();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.expand_row3, null);
        TextView textView = (TextView) convertView.findViewById(R.id.childHeaderRow3);
        Typeface custom_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
        textView.setTypeface(custom_regular);
//        String[] childArray=data.get(groupPosition);
        ArrayList<ExpandableClass> childArray=data.get(groupPosition).getMainnode().get(0).getCollection();

        String text = childArray.get(childPosition).getParent_value();

        textView.setText(text);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNetworkConnected();
                if (NETCONNECTION == 1) {

                    if(data.get(groupPosition).getMainnode().get(0).getCollection().get(childPosition).getMain_id().equals("1"))
                    {
                        MainActivity.Brand_Type=1;
                    }
                    else
                    {
                        MainActivity.Brand_Type=2;
                    }

                    Intent in=new Intent(context,ProductListing.class);
                    System.out.println("thirdfjkdsajfdk"+data.get(groupPosition).getMainnode().get(0).getCollection().get(childPosition).getMain_id());
                    in.putExtra("CatName", data.get(groupPosition).getMainnode().get(0).getCollection().get(childPosition).getParent_value());
                    in.putExtra("CatID", data.get(groupPosition).getMainnode().get(0).getCollection().get(childPosition).getParent_id());
                    in.putExtra("Identifier","1");
                    context.startActivity(in);
                }
                else {

                }
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        String[] children = data.get(groupPosition);
        ArrayList<ExpandableClass> children=data.get(groupPosition).getMainnode().get(0).getCollection();

        return children.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
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
