package app.com.dc_books;

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

/**
 * Created by good on 9/17/2016.
 */
public class ExpandableListAdapt extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    Typeface font,font2;


    public ExpandableListAdapt(Context context, List<String> listDataHeader,
                               HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;

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
                convertView = infalInflater.inflate(R.layout.category_child, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.ListItem);

            txtListChild.setTypeface(font);

            txtListChild.setText(childText);


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
