package app.com.dc_books.Expand;

import java.util.ArrayList;

/**
 * Created by User on 12/27/2017.
 */

public class ExpandableAllData {
    ArrayList<ExpandableCollectiveClass> mainnode;
    String parent_id,main_id;

    public ExpandableAllData(ArrayList<ExpandableCollectiveClass> mainnode, String parent_id, String main_id) {
        this.mainnode = mainnode;
        this.parent_id = parent_id;
        this.main_id= main_id;
    }

    public ArrayList<ExpandableCollectiveClass> getMainnode() {
        return mainnode;
    }

    public String getParent_id() {
        return parent_id;
    }
    public String getMain_id(){
        return main_id;
    }
}
