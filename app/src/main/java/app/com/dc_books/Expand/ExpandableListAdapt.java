package app.com.dc_books.Expand;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import app.com.dc_books.R;

/**
 * Created by good on 9/17/2016.
 */
public class ExpandableListAdapt extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, List<String>> _listDataChild1;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    Typeface font,font2;


    public ExpandableListAdapt(Context context, List<String> listDataHeader,
                               HashMap<String, List<String>> listChildData, HashMap<String, List<String>> listChildData1) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._listDataChild1 = listChildData1;

        font = Typeface.createFromAsset(_context.getAssets(),"fonts/Montserrat-Regular.ttf");
        font2 = Typeface.createFromAsset(_context.getAssets(),"fonts/Montserrat-Bold.ttf");

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);


            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.category_child1, null);
            }

            ImageView img = (ImageView) convertView.findViewById(R.id.arrowimgRow1);
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.childHeaderRow1);
            if (!(_listDataChild1.get(childText).isEmpty())) {
                System.out.println("clickedvalue" + _listDataChild1.get(childText).get(0));
            }
            txtListChild.setTypeface(font);

            txtListChild.setText(childText);

            img.setImageResource(R.mipmap.arrow_right);


            return convertView;
        }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            final String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.category_group, null);
            }

            ImageView img = (ImageView) convertView.findViewById(R.id.imageView);


            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.ListHeader);

            lblListHeader.setTypeface(font);

            lblListHeader.setText(headerTitle);

            if (isExpanded) {
                img.setImageResource(R.mipmap.arrow_down);
            } else {
                img.setImageResource(R.mipmap.arrow_right);
            }



            return convertView;
        }


        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
}
