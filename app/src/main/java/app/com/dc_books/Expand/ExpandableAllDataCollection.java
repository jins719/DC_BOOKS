package app.com.dc_books.Expand;

import java.util.ArrayList;

/**
 * Created by User on 12/28/2017.
 */

public class ExpandableAllDataCollection {
String parent_id;
ArrayList<ExpandableAllData> alldatanode;

    public ExpandableAllDataCollection(String parent_id, ArrayList<ExpandableAllData> alldatanode) {
        this.parent_id = parent_id;
        this.alldatanode = alldatanode;
    }

    public String getParent_id() {
        return parent_id;
    }

    public ArrayList<ExpandableAllData> getAlldatanode() {
        return alldatanode;
    }
}
